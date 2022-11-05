package cz.mg.crisp.ui;

import cz.mg.annotations.classes.Utility;

import javax.swing.*;

public @Utility class SplitPanel extends JSplitPane {
    public SplitPanel() {
        setOrientation(JSplitPane.HORIZONTAL_SPLIT);
        setResizeWeight(1.0);
    }
}
