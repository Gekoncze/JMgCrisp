package cz.mg.crisp.graphics;

import cz.mg.annotations.classes.Service;
import cz.mg.annotations.requirement.Mandatory;
import cz.mg.annotations.requirement.Optional;
import cz.mg.crisp.entity.*;
import cz.mg.crisp.entity.metadata.SceneMetadata;

import java.awt.*;
import java.awt.geom.AffineTransform;

public @Service class SceneRenderer {
    private static @Optional SceneRenderer instance;

    public static @Mandatory SceneRenderer getInstance() {
        if (instance == null) {
            instance = new SceneRenderer();
            instance.fragmentRenderer = FragmentRenderer.getInstance();
            instance.referenceRenderer = ReferenceRenderer.getInstance();
        }
        return instance;
    }

    private FragmentRenderer fragmentRenderer;
    private ReferenceRenderer referenceRenderer;

    private SceneRenderer() {
    }

    public void drawScene(@Mandatory Graphics2D g, @Mandatory SceneMetadata metadata, @Mandatory Scene scene) {
        AffineTransform transform = g.getTransform();

        Camera camera = scene.getCamera();
        g.scale(1.0 / camera.getZoom(), 1.0 / camera.getZoom());
        g.translate(-camera.getPosition().getX(), -camera.getPosition().getY());

        drawContent(g, metadata, scene);

        g.setTransform(transform);
    }

    private void drawContent(@Mandatory Graphics2D g, @Mandatory SceneMetadata metadata, @Mandatory Scene scene) {
        for (Fragment fragment : scene.getFragments()) {
            fragmentRenderer.draw(g, metadata, fragment);
        }

        for (Reference reference : scene.getReferences()) {
            referenceRenderer.draw(g, reference);
        }
    }
}
