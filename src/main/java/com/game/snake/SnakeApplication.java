package com.game.snake;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.input.Input;
import com.almasb.fxgl.input.UserAction;
import com.game.snake.components.BodyMovementComponent;
import com.game.snake.components.FruitLocationComponent;
import com.game.snake.components.HeadMovementComponent;
import javafx.geometry.Point2D;
import javafx.scene.input.KeyCode;
import javafx.util.Duration;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.almasb.fxgl.dsl.FXGL.*;
import static com.game.snake.Consts.*;

public class SnakeApplication extends GameApplication {

    // region Data Members

    private Entity _head;
    private Entity _tail;
    private List<Entity> _body = new ArrayList<>();
    private Entity _fruit;

    // endregion

    // region Main

    public static void main(String[] args) {
        launch(args);
    }

    // endregion

    // region Private Methods

    private void resetSnake() {
        int width = getAppWidth();
        int height = getAppHeight();

        _head = spawn(SnakeFactory.SNAKE_HEAD_SPAWNER, width / 2, height / 2);
        _body.clear();

        _tail = spawn(SnakeFactory.SNAKE_BODY_SPAWNER, (width / 2) - TILE_SIZE, height / 2);

        _body.add(_tail);

        _head.addComponent(new HeadMovementComponent());
    }

    // endregion

    // region GameApplication


    @Override
    protected void initSettings(GameSettings gameSettings) {
        gameSettings.setTitle("Snake");
        gameSettings.setWidth(GAME_SIZE * TILE_SIZE);
        gameSettings.setHeight(GAME_SIZE * TILE_SIZE);
    }


    @Override
    protected void initGame() {
        getGameWorld().addEntityFactory(new SnakeFactory());

        int width = getAppWidth();
        int height = getAppHeight();

        resetSnake();

        // Spawns border
        for (int x = 0; x <= width; x += TILE_SIZE) {
            spawn(SnakeFactory.BRICK_SPAWNER, x, 0);
            spawn(SnakeFactory.BRICK_SPAWNER, x, height - TILE_SIZE);
        }
        for (int y = 0; y <= height; y += TILE_SIZE) {
            spawn(SnakeFactory.BRICK_SPAWNER, 0, y);
            spawn(SnakeFactory.BRICK_SPAWNER, width - TILE_SIZE, y);
        }

        spawn(SnakeFactory.GRID_SPAWNER);

        spawn(SnakeFactory.BORDER_SPAWNER);

        _fruit = spawn(SnakeFactory.FRUIT_SPAWNER);

        _fruit.getComponent(FruitLocationComponent.class).resetFruitPosition();

        getGameTimer().runAtInterval(() -> {

            if (_head.isActive() && getWorldProperties().booleanProperty(GAME_STARTED).get()) {

                Point2D newBodyPosition = _head.getPosition();

                _head.getComponent(HeadMovementComponent.class).move();

                // Moves the last block to the old head place to optimize the snake
                _tail.setPosition(newBodyPosition);
                BodyMovementComponent tailComponent = _tail.getComponent(BodyMovementComponent.class);

                if (tailComponent.getNextBodyPart() != null) {
                    _tail = tailComponent.getNextBodyPart();
                }
            }

        }, Duration.seconds(0.15));

    }

    @Override
    protected void onUpdate(double tpf) {
        super.onUpdate(tpf);

        List<Entity> _headLocation = getGameWorld().getEntitiesAt(_head.getPosition());

        if (_headLocation.size() > 1 && _head.isActive()) {

            if (_headLocation.contains(_fruit)) {

                Entity spawn = spawn(SnakeFactory.SNAKE_BODY_SPAWNER, _tail.getPosition());

                BodyMovementComponent tailComponent = _tail.getComponent(BodyMovementComponent.class);

                // For first body parts needs to set the tail itself
                if(tailComponent.getNextBodyPart() == null){
                    spawn.getComponent(BodyMovementComponent.class).setNextBodyPart(_tail);
                }
                else {
                    spawn.getComponent(BodyMovementComponent.class).setNextBodyPart(tailComponent.getNextBodyPart());
                }
                tailComponent.setNextBodyPart(spawn);

                _body.add(spawn);

                _fruit.removeFromWorld();
                _fruit.getComponent(FruitLocationComponent.class).resetFruitPosition();
                getGameWorld().addEntity(_fruit);

            } else {

                int score = _body.size() - 1;

                System.out.println("Got " + score + " Fruits");

                _head.removeFromWorld();
                _body.forEach(Entity::removeFromWorld);
                System.out.println("death");
                getWorldProperties().booleanProperty(GAME_STARTED).set(false);

                getDialogService().showConfirmationBox(
                        "You got " + score + " fruit! \n Do you want to continue?", answer -> {

                            if(answer){
                                resetSnake();
                            }
                            else {

                                getGameController().exit();
                            }

                        });

            }

        }

    }

    @Override
    protected void initInput() {
        super.initInput();

        SnakeInput up = new SnakeInput("Up", new Point2D(0, -1));
        SnakeInput down = new SnakeInput("Down", new Point2D(0, 1));
        SnakeInput left = new SnakeInput("Left", new Point2D(-1, 0));
        SnakeInput right = new SnakeInput("Right", new Point2D(1, 0));

        final Input input = getInput();

        //WASD
//        input.addAction(up, KeyCode.W);
//        input.addAction(down, KeyCode.S);
//        input.addAction(left, KeyCode.A);
//        input.addAction(right, KeyCode.D);

        // Arrows
        input.addAction(up, KeyCode.UP);
        input.addAction(down, KeyCode.DOWN);
        input.addAction(left, KeyCode.LEFT);
        input.addAction(right, KeyCode.RIGHT);


    }


    @Override
    protected void initGameVars(Map<String, Object> vars) {
        vars.put(GAME_STARTED, false);
    }

    // endregion

    // region SnakeInput

    private class SnakeInput extends UserAction {

        private final Point2D direction;

        public SnakeInput(@NotNull String name, Point2D direction) {
            super(name);
            this.direction = direction;
        }

        // region User Action

        @Override
        protected void onActionBegin() {
            super.onActionBegin();

            if (_head.isActive()) {
                getWorldProperties().booleanProperty(GAME_STARTED).set(true);
                _head.getComponent(HeadMovementComponent.class).updateDirection(this.direction);
            }

        }

        @Override
        protected void onAction() {
            super.onAction();
        }

        @Override
        protected void onActionEnd() {
            super.onActionEnd();
        }

        // endregion

    }

    // endregion

}
