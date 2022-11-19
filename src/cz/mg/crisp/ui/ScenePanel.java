package cz.mg.crisp.ui;

import cz.mg.annotations.classes.Utility;
import cz.mg.annotations.requirement.Mandatory;
import cz.mg.annotations.requirement.Optional;
import cz.mg.crisp.actions.*;
import cz.mg.crisp.actions.Action;
import cz.mg.crisp.entity.Fragment;
import cz.mg.crisp.entity.GlobalPoint;
import cz.mg.crisp.entity.Reference;
import cz.mg.crisp.entity.Scene;
import cz.mg.crisp.entity.metadata.Metadata;
import cz.mg.crisp.event.*;
import cz.mg.crisp.graphics.SceneRenderer;
import cz.mg.crisp.graphics.SceneRenderingHints;
import cz.mg.crisp.services.*;
import cz.mg.crisp.services.cobject.CObjectMetadataFactory;
import cz.mg.crisp.utilities.Timer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

public @Utility class ScenePanel extends JPanel {
    private static final RenderingHints RENDEREING_HINTS = new SceneRenderingHints();
    private static final Color BACKGROUND_COLOR = Color.WHITE;

    private static final int DEFAULT_FPS_LIMIT = 25;
    private static final int DEFAULT_DELAY = Math.max(1, 1000 / DEFAULT_FPS_LIMIT);
    private static final int RESIZE_RADIUS = 4;

    private static final Cursor DEFAULT_CURSOR = new Cursor(Cursor.DEFAULT_CURSOR);
    private static final Cursor RESIZE_CURSOR = new Cursor(Cursor.SE_RESIZE_CURSOR);
    private static final Cursor MOVE_CURSOR = new Cursor(Cursor.MOVE_CURSOR);

    private final @Mandatory SceneRenderer sceneRenderer = SceneRenderer.getInstance();
    private final @Mandatory CoordinateService coordinateService = CoordinateService.getInstance();
    private final @Mandatory SelectionService selectionService = SelectionService.getInstance();
    private final @Mandatory ZoomService zoomService = ZoomService.getInstance();
    private final @Mandatory MetadataFactory metadataFactory = CObjectMetadataFactory.getInstance();
    private final @Mandatory FragmentDataReader fragmentDataReader = FragmentDataReader.getInstance();

    private final @Mandatory Metadata metadata = new Metadata();
    private final @Mandatory Timer timer = new Timer(DEFAULT_DELAY);

    private @Optional Scene scene;
    private @Optional Action action;
    private @Mandatory GlobalPoint mouse = new GlobalPoint();

    public ScenePanel() {
        setFocusable(true);
        metadata.setMetadataFactory(metadataFactory);
        addMouseListener(new UserMousePressedListener(this::onMousePressed));
        addMouseListener(new UserMouseReleasedListener(this::onMouseReleased));
        addMouseMotionListener(new UserMouseMovedListener(this::onMouseMoved));
        addMouseMotionListener(new UserMouseDraggedListener(this::onMouseDragged));
        addMouseWheelListener(new UserMouseWheelListener(this::onMouseWheelMoved));
        addKeyListener(new UserKeyPressedListener(this::onKeyPressed));
        addKeyListener(new UserKeyReleasedListener(this::onKeyReleased));
    }

    public @Optional Scene getScene() {
        return scene;
    }

    public void setScene(@Optional Scene scene) {
        this.scene = scene;
        refreshFragmentData();
        cancel();
    }

    public void cancel() {
        action = null;
        clearSelection();
        repaint();
    }

    private void refreshFragmentData() {
        if (scene != null) {
            for (Fragment fragment : scene.getFragments()) {
                fragmentDataReader.update(fragment, metadata);
            }
        }
        repaint();
    }

    private void clearSelection() {
        if (scene != null) {
            for (Fragment fragment : scene.getFragments()) {
                fragment.setSelected(false);
            }

            for (Reference reference : scene.getReferences()) {
                reference.setSelected(false);
            }
        }
        repaint();
    }

    private void onMousePressed(@Mandatory MouseEvent event) {
        requestFocus();
        updateMouse(event);

        if (scene != null) {
            if (event.getButton() == MouseEvent.BUTTON1) {
                boolean incremental = event.isControlDown();
                boolean range = event.isShiftDown();

                if (range) {
                    action = new RangeSelectionAction(scene, mouse);
                } else if(selectionService.isSelectedResizableAt(scene, mouse, RESIZE_RADIUS) && !incremental) {
                    action = new FragmentResizeAction(scene, mouse);
                } else if (selectionService.isSelectedAt(scene, mouse) && !incremental) {
                    action = new FragmentMoveAction(scene, mouse);
                } else if (!selectionService.select(scene, mouse, incremental)) {
                    action = new CameraMoveAction(scene.getCamera(), mouse);
                }

                updateCursor(event);
                repaint();
            }
        } else {
            cancel();
        }
    }

    private void onMouseReleased(@Mandatory MouseEvent event) {
        updateMouse(event);

        if (scene != null) {
            if (action != null) {
                action.onMouseReleased(mouse);
                action = null;
                repaint();
            }
        } else {
            cancel();
        }
    }

    private void onMouseMoved(@Mandatory MouseEvent event) {
        updateMouse(event);
        updateCursor(event);
    }

    private void onMouseDragged(@Mandatory MouseEvent event) {
        updateMouse(event);

        if (scene != null) {
            if (action != null) {
                if (timer.tick()) {
                    action.onMouseDragged(mouse);
                    repaint();
                }
            }
        } else {
            cancel();
        }
    }

    private void onMouseWheelMoved(@Mandatory MouseWheelEvent event) {
        if (scene != null) {
            zoomService.zoom(scene.getCamera(), mouse, event.getWheelRotation());
            repaint();
        } else {
            cancel();
        }
    }

    private void onKeyPressed(@Mandatory KeyEvent event) {
        requestFocus();

        if (event.getKeyCode() == KeyEvent.VK_CONTROL || event.getKeyCode() == KeyEvent.VK_SHIFT) {
            updateCursor(event);
        }
    }

    private void onKeyReleased(@Mandatory KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.VK_CONTROL || event.getKeyCode() == KeyEvent.VK_SHIFT) {
            updateCursor(event);
        }
    }

    private void updateMouse(@Mandatory MouseEvent event) {
        mouse = coordinateService.convert(event.getPoint());
    }

    private void updateCursor(@Mandatory InputEvent event) {
        if (scene != null) {
            boolean incremental = event.isControlDown();
            boolean range = event.isShiftDown();

            if (range) {
                setCursor(DEFAULT_CURSOR);
            } else if (selectionService.isSelectedResizableAt(scene, mouse, RESIZE_RADIUS) && !incremental) {
                setCursor(RESIZE_CURSOR);
            } else if (selectionService.isSelectedAt(scene, mouse) && !incremental) {
                setCursor(MOVE_CURSOR);
            } else {
                setCursor(DEFAULT_CURSOR);
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        draw((Graphics2D) g);
    }

    private void draw(@Mandatory Graphics2D g) {
        g.setRenderingHints(RENDEREING_HINTS);
        g.setColor(BACKGROUND_COLOR);
        g.fillRect(0, 0, getWidth(), getHeight());

        if (scene != null) {
            sceneRenderer.drawScene(g, scene);
        }

        if (action != null) {
            action.draw(g);
        }
    }
}
