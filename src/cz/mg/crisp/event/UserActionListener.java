package cz.mg.crisp.event;

import cz.mg.annotations.classes.Utility;
import cz.mg.annotations.requirement.Mandatory;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public @Utility class UserActionListener implements UserListener, ActionListener {
    private final @Mandatory Handler handler;

    public UserActionListener(@Mandatory Handler handler) {
        this.handler = handler;
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        handleExceptions(handler::run);
    }

    public interface Handler {
        void run();
    }
}
