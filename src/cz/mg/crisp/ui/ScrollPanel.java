package cz.mg.crisp.ui;

import cz.mg.annotations.classes.Utility;

import javax.swing.*;
import java.awt.*;

public @Utility class ScrollPanel extends JScrollPane {
    public ScrollPanel(Component view) {
        super(view);
        getHorizontalScrollBar().setUnitIncrement(10);
        getVerticalScrollBar().setUnitIncrement(10);
    }
}
