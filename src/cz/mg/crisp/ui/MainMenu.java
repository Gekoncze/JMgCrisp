package cz.mg.crisp.ui;

import cz.mg.annotations.classes.Utility;
import cz.mg.annotations.requirement.Mandatory;
import cz.mg.crisp.event.UserActionListener;

import javax.swing.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

public @Utility class MainMenu extends JMenuBar {
    public MainMenu(@Mandatory CrispWindow window) {
        JMenu fileMenu = new JMenu("File");
        fileMenu.setMnemonic('F');

        JMenuItem exitItem = new JMenuItem("Exit");
        exitItem.setMnemonic('E');
        exitItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F4, InputEvent.ALT_DOWN_MASK));
        exitItem.addActionListener(new UserActionListener(window::dispose));
        fileMenu.add(exitItem);

        add(fileMenu);

        JMenu editMenu = new JMenu("Edit");
        editMenu.setMnemonic('E');

        JMenuItem cancelCurrentActionItem = new JMenuItem("Cancel current action");
        cancelCurrentActionItem.setMnemonic('C');
        cancelCurrentActionItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0));
        cancelCurrentActionItem.addActionListener(new UserActionListener(() -> window.getScenePanel().cancel()));
        editMenu.add(cancelCurrentActionItem);

        add(editMenu);
    }
}
