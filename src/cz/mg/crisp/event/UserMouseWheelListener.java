package cz.mg.crisp.event;

import cz.mg.annotations.classes.Utility;
import cz.mg.annotations.requirement.Mandatory;

import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

public @Utility class UserMouseWheelListener implements UserListener, MouseWheelListener {
    private final @Mandatory Handler handler;

    public UserMouseWheelListener(@Mandatory Handler handler) {
        this.handler = handler;
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent event) {
        handleExceptions(() -> handler.run(event));
    }

    public interface Handler {
        void run(MouseWheelEvent event);
    }
}
