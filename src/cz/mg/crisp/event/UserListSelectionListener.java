package cz.mg.crisp.event;

import cz.mg.annotations.classes.Utility;
import cz.mg.annotations.requirement.Mandatory;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public @Utility class UserListSelectionListener implements UserListener, ListSelectionListener {
    private final @Mandatory Handler handler;

    public UserListSelectionListener(@Mandatory Handler handler) {
        this.handler = handler;
    }

    @Override
    public void valueChanged(ListSelectionEvent event) {
        handleExceptions(() -> handler.run(event));
    }

    public interface Handler {
        void run(ListSelectionEvent event);
    }
}
