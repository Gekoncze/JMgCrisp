package cz.mg.crisp.event;

import cz.mg.annotations.classes.Utility;
import cz.mg.annotations.requirement.Mandatory;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public @Utility class UserKeyPressedListener implements UserListener, KeyListener {
    private final @Mandatory Handler handler;

    public UserKeyPressedListener(@Mandatory Handler handler) {
        this.handler = handler;
    }

    @Override
    public void keyTyped(KeyEvent event) {
    }

    @Override
    public void keyPressed(KeyEvent event) {
        handleExceptions(() -> handler.run(event));
    }

    @Override
    public void keyReleased(KeyEvent event) {
    }

    public interface Handler {
        void run(KeyEvent event);
    }
}
