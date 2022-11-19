package cz.mg.crisp.graphics;

import cz.mg.annotations.classes.Service;
import cz.mg.annotations.requirement.Mandatory;
import cz.mg.annotations.requirement.Optional;
import cz.mg.crisp.entity.LocalPoint;
import cz.mg.crisp.entity.LocalVector;
import cz.mg.crisp.entity.Reference;

import java.awt.*;

public @Service class ReferenceRenderer {
    private static final Color REFERENCE_COLOR = Color.BLACK;
    private static final Color SELECTION_COLOR = Color.GREEN;
    private static final int ARROW_LENGTH = 8;
    private static final int ARROW_WIDTH = 4;

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
        drawArrows(g, reference);
    }

    private void drawArrows(@Mandatory Graphics2D g, @Mandatory Reference reference) {
        LocalVector backward = LocalPoint.minus(reference.getBegin(), reference.getEnd());
        double length = LocalVector.length(backward);
        double nx = backward.getX() / length;
        double ny = backward.getY() / length;
        int ex = reference.getEnd().getX();
        int ey = reference.getEnd().getY();
        LocalPoint first = new LocalPoint(
            (int) (ex + nx * ARROW_LENGTH - ny * ARROW_WIDTH),
            (int) (ey + ny * ARROW_LENGTH + nx * ARROW_WIDTH)
        );
        LocalPoint second = new LocalPoint(
            (int) (ex + nx * ARROW_LENGTH + ny * ARROW_WIDTH),
            (int) (ey + ny * ARROW_LENGTH - nx * ARROW_WIDTH)
        );
        drawLine(g, reference.getEnd(), first);
        drawLine(g, reference.getEnd(), second);
    }

    private void drawLine(@Mandatory Graphics2D g, @Mandatory LocalPoint p1, @Mandatory LocalPoint p2) {
        g.drawLine(p1.getX(), p1.getY(), p2.getX(), p2.getY());
    }
}
