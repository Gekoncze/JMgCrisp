package cz.mg.crisp.graphics;

import cz.mg.annotations.classes.Service;
import cz.mg.annotations.requirement.Mandatory;
import cz.mg.annotations.requirement.Optional;
import cz.mg.crisp.entity.Fragment;
import cz.mg.crisp.entity.metadata.FragmentFieldMetadata;
import cz.mg.crisp.entity.metadata.FragmentMetadata;
import cz.mg.crisp.entity.metadata.SceneMetadata;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.Objects;

public @Service class FragmentRenderer {
    private static final Color BACKGROUND_COLOR = new Color(255, 255, 192);
    private static final Color FOREGROUND_COLOR = Color.BLACK;
    private static final Color SELECTION_COLOR = Color.GREEN;
    private static final Font FONT = new Font("monospaced", Font.PLAIN, 12);

    private static @Optional FragmentRenderer instance;

    public static @Mandatory FragmentRenderer getInstance() {
        if (instance == null) {
            instance = new FragmentRenderer();
        }
        return instance;
    }

    private FragmentRenderer() {
    }

    public void draw(@Mandatory Graphics2D g, @Mandatory SceneMetadata metadata, @Mandatory Fragment fragment) {
        AffineTransform transform = g.getTransform();
        g.translate(fragment.getPosition().getX(), fragment.getPosition().getY());
        g.setClip(0, 0, fragment.getSize().getX() + 1, fragment.getSize().getY() + 1);

        drawBackground(g, fragment);
        drawContent(g, metadata, fragment.getObject());
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

    private void drawContent(@Mandatory Graphics2D g, @Mandatory SceneMetadata metadata, @Mandatory Object object) {
        FragmentMetadata fragmentMetadata = metadata.get(object);

        String name = fragmentMetadata.getName();
        String identity = getObjectIdentity(metadata, object);
        String header = name + " " + identity;
        int fontHeight = g.getFontMetrics().getHeight();
        int padding = 4;

        g.setFont(FONT);
        g.setColor(FOREGROUND_COLOR);
        g.drawString(header, padding, fontHeight);

        int i = 1;
        for (FragmentFieldMetadata fieldMetadata : fragmentMetadata.getFields()) {
            i++;
            String stringValue = valueToString(metadata, fieldMetadata.getValue(object));
            g.drawString(fieldMetadata.getName() + ": " + stringValue, padding, fontHeight * i);
        }
    }

    private @Mandatory String valueToString(@Mandatory SceneMetadata metadata, @Optional Object object) {
        if (object != null) {
            return getObjectIdentity(metadata, object);
        } else {
            return "null";
        }
    }

    private @Mandatory String getObjectIdentity(@Mandatory SceneMetadata metadata, @Mandatory Object object) {
        if (metadata.getMetadataFactory().isCompatible(object.getClass())) {
            Long identity = metadata.getMetadataFactory().getIdentity(object);
            if (identity != null) {
                return identity.toString();
            } else {
                return Long.toHexString(System.identityHashCode(object));
            }
        } else {
            return Long.toHexString(System.identityHashCode(object));
        }
    }
}
