package com.game.snake.components;

import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.component.Component;
import javafx.geometry.Point2D;

import static com.game.snake.Consts.*;


public class HeadMovementComponent extends Component {

    // region Data Members

    private Point2D _direction = new Point2D(1,0);

    private boolean _hasMovedAfterChange = true;

    // endregion

    // region Constructors

    public HeadMovementComponent() {

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

    /**
     * Updates the head position
     */
    public void move() {

        entity.translate(_direction.multiply(TILE_SIZE));

        _hasMovedAfterChange = true;

    }

    // endregion


}
