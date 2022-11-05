package cz.mg.crisp.ui;

import cz.mg.annotations.classes.Utility;
import cz.mg.annotations.requirement.Optional;
import cz.mg.c.CObject;

import javax.swing.*;

public @Utility class PropertiesPanel extends JPanel {
    private @Optional CObject object;

    public PropertiesPanel() {
    }

    public @Optional CObject getObject() {
        return object;
    }

    public void setObject(@Optional CObject object) {
        this.object = object;
    }
}
