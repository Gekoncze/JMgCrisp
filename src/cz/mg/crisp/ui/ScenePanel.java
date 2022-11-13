package cz.mg.crisp.ui;

import cz.mg.annotations.classes.Utility;
import cz.mg.annotations.requirement.Mandatory;
import cz.mg.annotations.requirement.Optional;
import cz.mg.crisp.actions.CameraMoveAction;
import cz.mg.crisp.actions.FragmentMoveAction;
import cz.mg.crisp.entity.GlobalPoint;
import cz.mg.crisp.entity.Scene;
import cz.mg.crisp.entity.metadata.SceneMetadata;
import cz.mg.crisp.event.*;
import cz.mg.crisp.graphics.SceneRenderer;
import cz.mg.crisp.services.CoordinateService;
import cz.mg.crisp.services.MetadataFactory;
import cz.mg.crisp.services.SelectionService;
import cz.mg.crisp.services.ZoomService;
import cz.mg.crisp.services.cobject.CObjectMetadataFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

public @Utility class ScenePanel extends JPanel {
    private static final Color BACKGROUND_COLOR = Color.WHITE;

    private final @Mandatory SceneRenderer sceneRenderer = SceneRenderer.getInstance();
    private final @Mandatory CoordinateService coordinateService = CoordinateService.getInstance();
    private final @Mandatory SelectionService selectionService = SelectionService.getInstance();
    private final @Mandatory ZoomService zoomService = ZoomService.getInstance();
    private final @Mandatory MetadataFactory metadataFactory = CObjectMetadataFactory.getInstance();

    private final @Mandatory SceneMetadata sceneMetadata = new SceneMetadata();

    private @Optional Scene scene;
    private @Optional CameraMoveAction cameraMoveAction;
    private @Optional FragmentMoveAction fragmentMoveAction;

    public ScenePanel() {
        sceneMetadata.setMetadataFactory(metadataFactory);
        addMouseListener(new UserMousePressedListener(this::onMousePressed));
        addMouseListener(new UserMouseReleasedListener(this::onMouseReleased));
        addMouseMotionListener(new UserMouseMovedListener(this::onMouseMoved));
        addMouseMotionListener(new UserMouseDraggedListener(this::onMouseDragged));
        addMouseWheelListener(new UserMouseWheelListener(this::onMouseWheelMoved));
    }

    public @Optional Scene getScene() {
        return scene;
    }

    public void setScene(@Optional Scene scene) {
        this.scene = scene;
    }

    public void cancel() {
        cameraMoveAction = null;
    }

    private void onMousePressed(@Mandatory MouseEvent event) {
        if (scene != null) {
            if (event.getButton() == MouseEvent.BUTTON1) {
                GlobalPoint mouse = coordinateService.convert(event.getPoint());
                boolean incremental = event.isControlDown();

                if (selectionService.isSelectedAt(mouse, scene) && !incremental) {
                    fragmentMoveAction = new FragmentMoveAction(scene, mouse);
                } else if (!selectionService.select(mouse, scene, incremental)) {
                    cameraMoveAction = new CameraMoveAction(scene.getCamera(), mouse);
                }

                updateCursor(event);
                repaint();
            }
        }
    }

    private void onMouseReleased(@Mandatory MouseEvent event) {
        if (cameraMoveAction != null) {
            cameraMoveAction = null;
            repaint();
        }

        if (fragmentMoveAction != null) {
            fragmentMoveAction = null;
            repaint();
        }
    }

    private void onMouseMoved(@Mandatory MouseEvent event) {
        updateCursor(event);
    }

    private void onMouseDragged(@Mandatory MouseEvent event) {
        GlobalPoint mouse = coordinateService.convert(event.getPoint());

        if (cameraMoveAction != null) {
            cameraMoveAction.onMouseDragged(mouse);
            repaint();
        }

        if (fragmentMoveAction != null) {
            fragmentMoveAction.onMouseDragged(mouse);
            repaint();
        }
    }

    private void onMouseWheelMoved(@Mandatory MouseWheelEvent event) {
        if (scene != null) {
            zoomService.zoom(
                scene.getCamera(),
                coordinateService.convert(event.getPoint()),
                event.getWheelRotation()
            );
            repaint();
        }
    }

    private void updateCursor(@Mandatory MouseEvent event) {
        if (scene != null) {
            GlobalPoint mouse = coordinateService.convert(event.getPoint());
            if (selectionService.isSelectedAt(mouse, scene)) {
                setCursor(new Cursor(Cursor.MOVE_CURSOR));
            } else {
                setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        draw((Graphics2D) g);
    }

    private void draw(@Mandatory Graphics2D g) {
        g.setColor(BACKGROUND_COLOR);
        g.fillRect(0, 0, getWidth(), getHeight());

        if (scene != null) {
            sceneRenderer.drawScene(g, sceneMetadata, scene);
        }
    }
}
