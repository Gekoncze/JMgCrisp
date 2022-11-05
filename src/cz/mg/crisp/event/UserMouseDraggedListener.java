package cz.mg.crisp.event;

import cz.mg.annotations.classes.Utility;
import cz.mg.annotations.requirement.Mandatory;

import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

public @Utility class UserMouseDraggedListener implements UserListener, MouseMotionListener {
    private final @Mandatory Handler handler;

    public UserMouseDraggedListener(@Mandatory Handler handler) {
        this.handler = handler;
    }

    @Override
    public void mouseDragged(MouseEvent event) {
        handleExceptions(() -> handler.run(event));
    }

    @Override
    public void mouseMoved(MouseEvent event) {
    }

    public interface Handler {
        void run(MouseEvent event);
    }
}
