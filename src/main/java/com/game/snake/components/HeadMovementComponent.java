package com.game.snake.components;

import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.component.Component;
import javafx.geometry.Point2D;

import static com.game.snake.Consts.*;


public class HeadMovementComponent extends MovementComponent {

    // region Data Members

    private Point2D _direction = new Point2D(1,0);

    private boolean _hasMovedAfterChange = true;

    // endregion

    // region Constructors

    public HeadMovementComponent(Entity nextPart) {
        _nextPart = nextPart;
    }


    // endregion

    // region Public Methods

    /**
     * Used to update the direction of the
     * @param direction The new direction for the head snake if possible
     */
    public void updateDirection(Point2D direction) {

        // Checks if the directions are reversed (if the result is either 0 or 1 they are not)
        if(_hasMovedAfterChange && direction.getX() *_direction.getX() != -1 && direction.getY() *_direction.getY() != -1){
            _direction = direction;
            _hasMovedAfterChange = false;
        }
    }

    @Override
    public void move(Point2D newPosition) {

        Point2D oldPosition = entity.getPosition();

        entity.translate(_direction.multiply(TILE_SIZE));

        _hasMovedAfterChange = true;

        _nextPart.getComponent(MovementComponent.class).move(oldPosition);

    }

    // endregion


}
