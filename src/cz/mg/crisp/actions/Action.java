package cz.mg.crisp.actions;

import cz.mg.annotations.classes.Utility;
import cz.mg.annotations.requirement.Mandatory;
import cz.mg.crisp.entity.model.math.GlobalPoint;

import java.awt.*;

public @Utility interface Action {
    default void draw(@Mandatory Graphics2D g){}
    default void onMouseDragged(@Mandatory GlobalPoint mouse){}
    default void onMouseReleased(@Mandatory GlobalPoint mouse){}
}
