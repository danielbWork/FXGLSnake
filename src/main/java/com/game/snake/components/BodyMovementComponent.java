package com.game.snake.components;

import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.component.Component;

public class BodyMovementComponent extends Component {

    // region Data Members

    private Entity _nextBodyPart = null;

    // endregion

    // region Properties

    public Entity getNextBodyPart() {
        return _nextBodyPart;
    }

    public void setNextBodyPart(Entity nextBodyPart) {
        _nextBodyPart = nextBodyPart;
    }


    // endregion

}
