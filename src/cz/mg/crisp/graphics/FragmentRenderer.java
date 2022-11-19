package cz.mg.crisp.graphics;

import cz.mg.annotations.classes.Service;
import cz.mg.annotations.requirement.Mandatory;
import cz.mg.annotations.requirement.Optional;
import cz.mg.crisp.entity.Fragment;

import java.awt.*;
import java.awt.geom.AffineTransform;

public @Service class FragmentRenderer {
    private static final Color BACKGROUND_COLOR = new Color(255, 255, 192);
    private static final Color FOREGROUND_COLOR = Color.BLACK;
    private static final Color SELECTION_COLOR = Color.GREEN;
    private static final Font FONT = new Font("monospaced", Font.PLAIN, 12);
    private static final int PADDING = 4;

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
    }

    private void drawContent(@Mandatory Graphics2D g, @Mandatory Fragment fragment) {
        int fontHeight = g.getFontMetrics().getHeight();

        g.setFont(FONT);
        g.setColor(FOREGROUND_COLOR);
        g.drawString(fragment.getHeader(), PADDING, fontHeight);

        int base = fontHeight + PADDING;

        int i = 0;
        for (String row : fragment.getRows()) {
            i++;
            g.drawString(row, PADDING, base + PADDING + fontHeight * i);
        }

        g.setColor(fragment.isSelected() ? SELECTION_COLOR : FOREGROUND_COLOR);
        g.drawLine(0, base, fragment.getSize().getX(), base);
    }
}
