package com.game.snake;

import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;
import com.game.snake.components.FruitLocationComponent;
import com.game.snake.components.MovementComponent;
import javafx.collections.ObservableList;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;

import static com.almasb.fxgl.dsl.FXGL.*;
import static com.game.snake.Consts.TILE_SIZE;


public class SnakeFactory implements EntityFactory {

    // region Constants

    public static final String SNAKE_HEAD_SPAWNER = "head";
    public static final String SNAKE_BODY_SPAWNER = "body";
    public static final String FRUIT_SPAWNER = "fruit";
    public static final String BRICK_SPAWNER = "brick";
    public static final String BORDER_SPAWNER = "border";


    // endregion

    // region Public Methods

    @Spawns(SNAKE_HEAD_SPAWNER)
    public Entity newSnakeHead(SpawnData data){
        return build(data,EntityType.SNAKE_HEAD,Color.GREENYELLOW);
    }

    @Spawns(SNAKE_BODY_SPAWNER)
    public Entity newSnakeBody(SpawnData data){
        Entity entity = build(data, EntityType.SNAKE_BODY, Color.GREENYELLOW);
        entity.addComponent(new MovementComponent());
        return entity;
    }

    @Spawns(FRUIT_SPAWNER)
    public Entity newFruit(SpawnData data){
        Entity fruit = build(data, EntityType.FRUIT, Color.RED);
        fruit.addComponent(new FruitLocationComponent());
        return fruit;
    }


    @Spawns(BRICK_SPAWNER)
    public Entity newBrick(SpawnData data){
        return entityBuilder(data)
                .type(EntityType.BRICK)
                .viewWithBBox(new Rectangle(TILE_SIZE, TILE_SIZE, Color.BLACK))
                .zIndex(-2)
                .build();
    }

    @Spawns(BORDER_SPAWNER)
    public Entity createBorder(SpawnData data){

        Path path = new Path();

        path.setStroke(Color.BLACK);
        path.setStrokeWidth(TILE_SIZE* 2);

        ObservableList<PathElement> elements = path.getElements();

        // Sets Start point
        elements.add(new MoveTo(0,0));

        // Creates border lines
        HLineTo top = new HLineTo(getAppWidth());
        VLineTo right = new VLineTo(getAppHeight());

        HLineTo bottom = new HLineTo(0);
        VLineTo left = new VLineTo(0);

        // Actually adds the lines
        elements.add(top);
        elements.add(right);
        elements.add(bottom);
        elements.add(left);

        return entityBuilder(data)
                .type(EntityType.BRICK)
                .viewWithBBox(path)
                .zIndex(-1)
                .build();

    }


    // endregion

    // region Private Methods

    private Entity build(SpawnData data, EntityType type,Color color){

        return entityBuilder(data)
                .type(type)
                .viewWithBBox(new Rectangle(TILE_SIZE, TILE_SIZE, color))
                .collidable()
                .build();

    }

    // endregion


}
