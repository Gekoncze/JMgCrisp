package cz.mg.crisp.ui;

import cz.mg.annotations.classes.Utility;
import cz.mg.annotations.requirement.Mandatory;
import cz.mg.annotations.requirement.Optional;
import cz.mg.crisp.entity.GlobalPoint;
import cz.mg.crisp.entity.Scene;
import cz.mg.crisp.event.*;
import cz.mg.crisp.actions.CameraMoveAction;
import cz.mg.crisp.graphics.SceneRenderer;
import cz.mg.crisp.services.CoordinateService;
import cz.mg.crisp.services.SelectionService;
import cz.mg.crisp.services.ZoomService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

public @Utility class ScenePanel extends JPanel {
    private static final Color BACKGROUND_COLOR = Color.WHITE;

    private final SceneRenderer sceneRenderer = SceneRenderer.getInstance();
    private final CoordinateService coordinateService = CoordinateService.getInstance();
    private final SelectionService selectionService = SelectionService.getInstance();
    private final ZoomService zoomService = ZoomService.getInstance();

    private @Optional Scene scene;
    private @Optional CameraMoveAction cameraMoveAction;

    public ScenePanel() {
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
                GlobalPoint point = coordinateService.convert(event.getPoint());

                if (!selectionService.select(point, scene, event.isControlDown())) {
                    cameraMoveAction = new CameraMoveAction(scene.getCamera(), point);
                }

                updateCursor(event);
            }
        }
        repaint();
    }

    private void onMouseReleased(@Mandatory MouseEvent event) {
        if (cameraMoveAction != null) {
            cameraMoveAction = null;
        }
        repaint();
    }

    private void onMouseMoved(@Mandatory MouseEvent event) {
        updateCursor(event);
    }

    private void onMouseDragged(@Mandatory MouseEvent event) {
        if (cameraMoveAction != null) {
            cameraMoveAction.onMouseDragged(coordinateService.convert(event.getPoint()));
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
            if (selectionService.isInsideSelected(coordinateService.convert(event.getPoint()), scene)) {
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
            sceneRenderer.drawScene(g, scene);
        }
    }
}
