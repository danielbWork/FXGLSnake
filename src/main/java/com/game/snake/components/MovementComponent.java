package com.game.snake.components;

import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.component.Component;
import javafx.geometry.Point2D;

import static com.game.snake.Consts.TILE_SIZE;

public class MovementComponent extends Component {

    // region Data Members

    protected Entity _nextPart;


    // endregion

    // region Getter & Setters

    public void setNextPart(Entity nextPart) {
        _nextPart = nextPart;
    }

    // endregion

    // region Public Methods

    public void move(Point2D newPosition) {

        Point2D oldPosition = entity.getPosition();

        entity.setPosition(newPosition);
        if(_nextPart != null){

            _nextPart.getComponent(MovementComponent.class).move(oldPosition);

        }


    }

    // endregion

}
