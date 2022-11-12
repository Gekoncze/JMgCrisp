package cz.mg.crisp.graphics;

import cz.mg.annotations.classes.Service;
import cz.mg.annotations.requirement.Mandatory;
import cz.mg.annotations.requirement.Optional;
import cz.mg.c.CObject;
import cz.mg.crisp.entity.*;
import cz.mg.crisp.other.FragmentFieldMetadata;
import cz.mg.crisp.other.Metadata;
import cz.mg.crisp.services.MetadataProvider;

import java.awt.*;
import java.awt.geom.AffineTransform;

public @Service class SceneRenderer {
    private static final Color FRAGMENT_BACKGROUND_COLOR = new Color(255, 255, 192);
    private static final Color FRAGMENT_FOREGROUND_COLOR = Color.BLACK;
    private static final Color REFERENCE_COLOR = Color.BLACK;
    private static final Color SELECTION_COLOR = Color.GREEN;
    private static final Font FONT = new Font("monospaced", Font.PLAIN, 12);

    private static @Optional SceneRenderer instance;

    public static @Mandatory SceneRenderer getInstance() {
        if (instance == null) {
            instance = new SceneRenderer();
            instance.metadataProvider = MetadataProvider.getInstance();
        }
        return instance;
    }

    private MetadataProvider metadataProvider;

    private final Metadata metadata = new Metadata();

    private SceneRenderer() {
    }

    public void drawScene(@Mandatory Graphics2D g, @Mandatory Scene scene) {
        AffineTransform transform = g.getTransform();

        Camera camera = scene.getCamera();
        g.scale(1.0 / camera.getZoom(), 1.0 / camera.getZoom());
        g.translate(-camera.getPosition().getX(), -camera.getPosition().getY());

        for (Fragment fragment : scene.getFragments()) {
            drawFragment(g, fragment);
        }

        for (Reference reference : scene.getReferences()) {
            drawReference(g, reference);
        }

        g.setTransform(transform);
    }

    private void drawFragment(@Mandatory Graphics2D g, @Mandatory Fragment fragment) {
        g.setClip(
            fragment.getPosition().getX(),
            fragment.getPosition().getY(),
            fragment.getSize().getX() + 1,
            fragment.getSize().getY() + 1
        );

        g.setColor(FRAGMENT_BACKGROUND_COLOR);
        g.fillRect(
            fragment.getPosition().getX(),
            fragment.getPosition().getY(),
            fragment.getSize().getX(),
            fragment.getSize().getY()
        );

        g.setColor(fragment.isSelected() ? SELECTION_COLOR : FRAGMENT_FOREGROUND_COLOR);
        g.drawRect(
            fragment.getPosition().getX(),
            fragment.getPosition().getY(),
            fragment.getSize().getX(),
            fragment.getSize().getY()
        );

        AffineTransform transform = g.getTransform();
        g.translate(fragment.getPosition().getX(), fragment.getPosition().getY());

        drawFragmentContent(g, fragment.getObject());

        g.setTransform(transform);

        g.setClip(null);
    }

    private void drawFragmentContent(@Mandatory Graphics2D g, @Mandatory CObject object) {
        String header = object.getClass().getSimpleName() + " " + object.getAddress();
        int fontHeight = g.getFontMetrics().getHeight();
        int padding = 4;

        g.setFont(FONT);
        g.setColor(FRAGMENT_FOREGROUND_COLOR);
        g.drawString(header, padding, fontHeight);

        int i = 1;
        for (FragmentFieldMetadata field : metadataProvider.get(metadata, object).getFields()) {
            i++;
            Object value = field.getValue(object);
            String stringValue = value instanceof CObject
                ? String.valueOf(((CObject) value).getAddress())
                : String.valueOf(value);
            g.drawString(field.getName() + ": " + stringValue, padding, fontHeight * i);
        }
    }

    private void drawReference(@Mandatory Graphics2D g, @Mandatory Reference reference) {
        g.setColor(reference.isSelected() ? SELECTION_COLOR : REFERENCE_COLOR);
        drawLine(g, reference.getBegin(), reference.getEnd());
    }

    private void drawLine(@Mandatory Graphics2D g, @Mandatory LocalPoint p1, @Mandatory LocalPoint p2) {
        g.drawLine(p1.getX(), p1.getY(), p2.getX(), p2.getY());
    }
}
