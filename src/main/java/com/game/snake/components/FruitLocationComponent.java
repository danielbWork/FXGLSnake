package com.game.snake.components;

import com.almasb.fxgl.core.math.FXGLMath;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.component.Component;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;

import static com.game.snake.Consts.*;

public class FruitLocationComponent extends Component {

    public void resetFruitPosition() {

        Point2D point = null;

        while (point == null) {

            Point2D randomPoint = FXGLMath.randomPoint(new Rectangle2D(1, 1,
                    GAME_SIZE-2, GAME_SIZE-2));

            randomPoint = new Point2D(Math.round(randomPoint.getX()),Math.round(randomPoint.getY()));

            randomPoint = randomPoint.multiply(TILE_SIZE);

            if (FXGL.getGameWorld().getEntitiesAt(randomPoint).isEmpty()) {
                point = randomPoint;
            }

        }

        entity.setPosition(point);

    }

}
