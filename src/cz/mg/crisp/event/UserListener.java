package cz.mg.crisp.event;

import cz.mg.annotations.classes.Utility;
import cz.mg.annotations.requirement.Mandatory;

import javax.swing.*;

public @Utility interface UserListener {
    default void handleExceptions(@Mandatory Runnable runnable) {
        try {
            runnable.run();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                null,
                e.getMessage(),
                e.getClass().getSimpleName(),
                JOptionPane.ERROR_MESSAGE
            );

            e.printStackTrace();
        }
    }
}
