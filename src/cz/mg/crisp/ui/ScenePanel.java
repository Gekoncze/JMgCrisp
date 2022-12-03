package cz.mg.crisp.ui;

import cz.mg.annotations.classes.Utility;
import cz.mg.annotations.requirement.Mandatory;
import cz.mg.annotations.requirement.Optional;
import cz.mg.collections.pair.Pair;
import cz.mg.crisp.actions.*;
import cz.mg.crisp.actions.Action;
import cz.mg.crisp.entity.model.Fragment;
import cz.mg.crisp.entity.model.math.GlobalPoint;
import cz.mg.crisp.entity.model.Reference;
import cz.mg.crisp.entity.model.Scene;
import cz.mg.crisp.event.*;
import cz.mg.crisp.graphics.SceneRenderer;
import cz.mg.crisp.graphics.SceneRenderingHints;
import cz.mg.crisp.listeners.FragmentOpenListener;
import cz.mg.crisp.listeners.FragmentSelectListener;
import cz.mg.crisp.services.*;
import cz.mg.crisp.utilities.Timer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

public @Utility class ScenePanel extends JPanel {
    private static final RenderingHints RENDERING_HINTS = new SceneRenderingHints();
    private static final Color BACKGROUND_COLOR = Color.WHITE;

    private static final int DEFAULT_FPS_LIMIT = 25;
    private static final int DEFAULT_DELAY = Math.max(1, 1000 / DEFAULT_FPS_LIMIT);
    private static final int RESIZE_RADIUS = 4;

    private static final Cursor DEFAULT_CURSOR = new Cursor(Cursor.DEFAULT_CURSOR);
    private static final Cursor RESIZE_CURSOR = new Cursor(Cursor.SE_RESIZE_CURSOR);
    private static final Cursor MOVE_CURSOR = new Cursor(Cursor.MOVE_CURSOR);

    private final @Mandatory SceneRenderer sceneRenderer = SceneRenderer.getInstance();
    private final @Mandatory CoordinateService coordinateService = CoordinateService.getInstance();
    private final @Mandatory FragmentSelectionService fragmentSelectionService = FragmentSelectionService.getInstance();
    private final @Mandatory ZoomService zoomService = ZoomService.getInstance();
    private final @Mandatory DataReader dataReader = DataReader.getInstance();
    private final @Mandatory ReferencePositionService referencePositionService = ReferencePositionService.getInstance();
    private final @Mandatory CloseService closeService = CloseService.getInstance();

    private final @Mandatory Timer timer = new Timer(DEFAULT_DELAY);

    private @Optional Scene scene;
    private @Optional Action action;
    private @Mandatory GlobalPoint mouse = new GlobalPoint();
    private @Optional FragmentSelectListener fragmentSelectListener;
    private @Optional FragmentOpenListener fragmentOpenListener;

    public ScenePanel() {
        setFocusable(true);
        addMouseListener(new UserMousePressedListener(this::onMousePressed));
        addMouseListener(new UserMouseReleasedListener(this::onMouseReleased));
        addMouseListener(new UserMouseClickedListener(this::onMouseClicked));
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
        refreshReferencePositions();
        cancel();
    }

    public @Optional FragmentSelectListener getFragmentSingleSelectListener() {
        return fragmentSelectListener;
    }

    public void setFragmentSingleSelectListener(@Optional FragmentSelectListener fragmentSelectListener) {
        this.fragmentSelectListener = fragmentSelectListener;
    }

    public @Optional FragmentOpenListener getFragmentOpenListener() {
        return fragmentOpenListener;
    }

    public void setFragmentOpenListener(@Optional FragmentOpenListener fragmentOpenListener) {
        this.fragmentOpenListener = fragmentOpenListener;
    }

    public void cancel() {
        action = null;
        clearSelection();
        repaint();
    }

    private void refreshReferencePositions() {
        if (scene != null) {
            referencePositionService.computePositionsForSelectedFragmentReferences(scene, fragment -> true);
        }
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
                Fragment closeableFragment;

                if (range) {
                    action = new RangeSelectionAction(scene, mouse, fragmentSelectListener);
                } else if(fragmentSelectionService.isSelectedResizableAt(scene, mouse, RESIZE_RADIUS) && !incremental) {
                    action = new FragmentResizeAction(scene, mouse);
                } else if ((closeableFragment = fragmentSelectionService.getCloseableAt(scene, mouse)) != null) {
                    closeFragment(closeableFragment);
                } else if (fragmentSelectionService.isSelectedAt(scene, mouse) && !incremental) {
                    if (fragmentSelectListener != null) {
                        fragmentSelectListener.onFragmentSelected(fragmentSelectionService.getSelectedAt(scene, mouse));
                    }
                    action = new FragmentMoveAction(scene, mouse);
                } else if (!fragmentSelectionService.select(scene, mouse, incremental, fragmentSelectListener)) {
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

    private void onMouseClicked(@Mandatory MouseEvent event) {
        if (!event.isControlDown() && !event.isShiftDown() && event.getButton() == MouseEvent.BUTTON1) {
            if (event.getClickCount() == 1) {
                if (trySelectField()) {
                    event.consume();
                }
            } else if (event.getClickCount() == 2) {
                if (tryOpenField()) {
                    event.consume();
                }
            }
        }
    }

    private boolean trySelectField() {
        if (scene != null) {
            // TODO - select field
        }
        return false;
    }

    private boolean tryOpenField() {
        if (scene != null) {
            Pair<Fragment, Integer> fragmentField = fragmentSelectionService.getFragmentFieldAt(scene, mouse);
            if (fragmentField != null) {
                if (fragmentOpenListener != null) {
                    fragmentOpenListener.onFragmentOpened(
                        fragmentField.getKey(),
                        fragmentField.getValue()
                    );
                    return true;
                }
            }
        }
        return false;
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
            if (event.isControlDown()) {
                zoomService.zoom(scene.getCamera(), mouse, event.getWheelRotation());
                repaint();
            }
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
            } else if (fragmentSelectionService.isSelectedResizableAt(scene, mouse, RESIZE_RADIUS) && !incremental) {
                setCursor(RESIZE_CURSOR);
            } else if (fragmentSelectionService.getCloseableAt(scene, mouse) != null) {
                setCursor(DEFAULT_CURSOR);
            } else if (fragmentSelectionService.isSelectedAt(scene, mouse) && !incremental) {
                setCursor(MOVE_CURSOR);
            } else {
                setCursor(DEFAULT_CURSOR);
            }
        }
    }

    private void closeFragment(@Mandatory Fragment fragment) {
        if (scene != null) {
            closeService.close(scene, fragment);
            referencePositionService.computePositionsForSelectedFragmentReferences(scene, f -> true);
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        draw((Graphics2D) g);
    }

    private void draw(@Mandatory Graphics2D g) {
        g.setRenderingHints(RENDERING_HINTS);
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
