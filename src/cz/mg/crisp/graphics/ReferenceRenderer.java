package cz.mg.crisp.graphics;

import cz.mg.annotations.classes.Service;
import cz.mg.annotations.requirement.Mandatory;
import cz.mg.annotations.requirement.Optional;
import cz.mg.crisp.entity.LocalPoint;
import cz.mg.crisp.entity.Reference;

import java.awt.*;

public @Service class ReferenceRenderer {
    private static final Color REFERENCE_COLOR = Color.BLACK;
    private static final Color SELECTION_COLOR = Color.GREEN;

    private static @Optional ReferenceRenderer instance;

    public static @Mandatory ReferenceRenderer getInstance() {
        if (instance == null) {
            instance = new ReferenceRenderer();
        }
        return instance;
    }

    private ReferenceRenderer() {
    }

    public void draw(@Mandatory Graphics2D g, @Mandatory Reference reference) {
        g.setColor(reference.isSelected() ? SELECTION_COLOR : REFERENCE_COLOR);
        drawLine(g, reference.getBegin(), reference.getEnd());
    }

    private void drawLine(@Mandatory Graphics2D g, @Mandatory LocalPoint p1, @Mandatory LocalPoint p2) {
        g.drawLine(p1.getX(), p1.getY(), p2.getX(), p2.getY());
    }
}