package cz.mg.crisp.actions;

import cz.mg.annotations.classes.Utility;
import cz.mg.annotations.requirement.Mandatory;
import cz.mg.annotations.requirement.Optional;
import cz.mg.crisp.entity.*;
import cz.mg.crisp.graphics.RangeSelectionActionRenderer;
import cz.mg.crisp.services.CoordinateService;
import cz.mg.crisp.services.FragmentSelectionService;
import cz.mg.crisp.listeners.FragmentSelectListener;

import java.awt.*;

public @Utility class RangeSelectionAction implements Action {
    private final CoordinateService coordinateService = CoordinateService.getInstance();
    private final FragmentSelectionService fragmentSelectionService = FragmentSelectionService.getInstance();
    private final RangeSelectionActionRenderer renderer = RangeSelectionActionRenderer.getInstance();

    private final @Mandatory Scene scene;
    private final @Mandatory LocalPoint begin;
    private @Mandatory LocalPoint end;
    private final @Optional FragmentSelectListener fragmentSelectListener;

    public RangeSelectionAction(
        @Mandatory Scene scene,
        @Mandatory GlobalPoint mouse,
        @Optional FragmentSelectListener fragmentSelectListener
    ) {
        this.scene = scene;
        this.begin = coordinateService.globalToLocal(scene.getCamera(), mouse);
        this.end = new LocalPoint(begin);
        this.fragmentSelectListener = fragmentSelectListener;
    }

    public @Mandatory LocalPoint getBegin() {
        return begin;
    }

    public @Mandatory LocalPoint getEnd() {
        return end;
    }

    @Override
    public void onMouseDragged(@Mandatory GlobalPoint mouse) {
        update(mouse);
    }

    @Override
    public void onMouseReleased(@Mandatory GlobalPoint mouse) {
        update(mouse);
        select();
    }

    private void update(@Mandatory GlobalPoint mouse) {
        end = coordinateService.globalToLocal(scene.getCamera(), mouse);
    }

    private void select() {
        for (Fragment fragment : scene.getFragments()) {
            if (!fragment.isSelected()) {
                if (fragmentSelectionService.isInsideArea(begin, end, fragment)) {
                    fragment.setSelected(true);
                    if (fragmentSelectListener != null) {
                        fragmentSelectListener.onFragmentSelected(fragment);
                    }
                }
            }
        }
    }

    @Override
    public void draw(@Mandatory Graphics2D g) {
        renderer.draw(g, scene.getCamera(), this);
    }
}
