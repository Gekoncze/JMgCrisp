package cz.mg.crisp.event;

import cz.mg.annotations.classes.Utility;
import cz.mg.annotations.requirement.Mandatory;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public @Utility class UserMouseReleasedListener implements UserListener, MouseListener {
    private final @Mandatory Handler handler;

    public UserMouseReleasedListener(@Mandatory Handler handler) {
        this.handler = handler;
    }

    @Override
    public void mouseClicked(MouseEvent event) {
    }

    @Override
    public void mousePressed(MouseEvent event) {
    }

    @Override
    public void mouseReleased(MouseEvent event) {
        handleExceptions(() -> handler.run(event));
    }

    @Override
    public void mouseEntered(MouseEvent event) {
    }

    @Override
    public void mouseExited(MouseEvent event) {
    }

    public interface Handler {
        void run(MouseEvent event);
    }
}
