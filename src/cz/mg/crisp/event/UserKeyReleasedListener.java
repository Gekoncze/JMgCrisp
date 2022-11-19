package cz.mg.crisp.event;

import cz.mg.annotations.classes.Utility;
import cz.mg.annotations.requirement.Mandatory;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public @Utility class UserKeyReleasedListener implements UserListener, KeyListener {
    private final @Mandatory Handler handler;

    public UserKeyReleasedListener(@Mandatory Handler handler) {
        this.handler = handler;
    }

    @Override
    public void keyTyped(KeyEvent event) {
    }

    @Override
    public void keyPressed(KeyEvent event) {
    }

    @Override
    public void keyReleased(KeyEvent event) {
        handleExceptions(() -> handler.run(event));
    }

    public interface Handler {
        void run(KeyEvent event);
    }
}
