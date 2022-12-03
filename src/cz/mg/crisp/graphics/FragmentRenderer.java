package cz.mg.crisp.graphics;

import cz.mg.annotations.classes.Service;
import cz.mg.annotations.requirement.Mandatory;
import cz.mg.annotations.requirement.Optional;
import cz.mg.crisp.entity.model.Fragment;
import cz.mg.crisp.entity.model.Row;

import java.awt.*;
import java.awt.geom.AffineTransform;

import static cz.mg.crisp.services.FragmentPositionService.SECTION_SIZE;

public @Service class FragmentRenderer {
    private static final int FONT_SIZE = 12;
    private static final Color BACKGROUND_COLOR = new Color(255, 255, 192);
    private static final Color FOREGROUND_COLOR = Color.BLACK;
    private static final Color SELECTION_COLOR = Color.GREEN;
    private static final Font FONT = new Font("monospaced", Font.PLAIN, FONT_SIZE);
    private static final int PADDING = 4;

    public static final int SECTION_PADDING = (SECTION_SIZE - FONT_SIZE) / 2;

    private static @Optional FragmentRenderer instance;

    public static @Mandatory FragmentRenderer getInstance() {
        if (instance == null) {
            instance = new FragmentRenderer();
        }
        return instance;
    }

    private FragmentRenderer() {
    }

    public void draw(@Mandatory Graphics2D g, @Mandatory Fragment fragment) {
        AffineTransform transform = g.getTransform();
        g.translate(fragment.getPosition().getX(), fragment.getPosition().getY());
        g.setClip(0, 0, fragment.getSize().getX() + 1, fragment.getSize().getY() + 1);

        drawBackground(g, fragment);
        drawContent(g, fragment);
        drawForeground(g, fragment);

        g.setClip(null);
        g.setTransform(transform);
    }

    private void drawBackground(@Mandatory Graphics2D g, @Mandatory Fragment fragment) {
        g.setColor(BACKGROUND_COLOR);
        g.fillRect(0, 0, fragment.getSize().getX(), fragment.getSize().getY());
    }

    private void drawForeground(@Mandatory Graphics2D g, @Mandatory Fragment fragment) {
        g.setColor(fragment.isSelected() ? SELECTION_COLOR : FOREGROUND_COLOR);
        g.drawRect(0, 0, fragment.getSize().getX(), fragment.getSize().getY());
        g.drawLine(0, SECTION_SIZE, fragment.getSize().getX(), SECTION_SIZE);
        if (!fragment.isMandatory()) {
            drawCloseButton(g, fragment);
        }
    }

    private void drawCloseButton(@Mandatory Graphics2D g, @Mandatory Fragment fragment) {
        int x = fragment.getSize().getX() - SECTION_SIZE;
        int p = PADDING;
        g.drawLine(x, 0, x, SECTION_SIZE);
        g.drawLine(x + p, p, x + SECTION_SIZE - p, SECTION_SIZE - p);
        g.drawLine(x + p, SECTION_SIZE - p, x + SECTION_SIZE - p, p);
    }

    private void drawContent(@Mandatory Graphics2D g, @Mandatory Fragment fragment) {
        g.setFont(FONT);
        g.setColor(FOREGROUND_COLOR);

        int y = 0;
        drawString(g, fragment.getHeader(), PADDING, y + SECTION_PADDING);

        for (Row row : fragment.getRows()) {
            y += SECTION_SIZE;
            drawString(g, row.getName() + ": " + row.getValue(), PADDING, y + SECTION_PADDING);
        }
    }

    private void drawString(@Mandatory Graphics2D g, @Mandatory String s, int x, int y) {
        FontMetrics fm = g.getFontMetrics(FONT);
        g.drawString(s, x, y + fm.getHeight() - fm.getDescent());
    }
}
