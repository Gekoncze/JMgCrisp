package cz.mg.crisp.actions;

import cz.mg.annotations.classes.Utility;
import cz.mg.annotations.requirement.Mandatory;
import cz.mg.collections.map.Map;
import cz.mg.collections.pair.ReadablePair;
import cz.mg.crisp.entity.*;
import cz.mg.crisp.services.CoordinateService;
import cz.mg.crisp.services.ReferencePositionService;

public @Utility class FragmentMoveAction implements Action {
    private final CoordinateService coordinateService = CoordinateService.getInstance();
    private final ReferencePositionService referencePositionService = ReferencePositionService.getInstance();

    private final @Mandatory Scene scene;
    private final @Mandatory GlobalPoint mouseStart;
    private final @Mandatory Map<Fragment, LocalPoint> fragmentsStart = new Map<>(200);

    public FragmentMoveAction(@Mandatory Scene scene, @Mandatory GlobalPoint mouse) {
        this.scene = scene;
        this.mouseStart = new GlobalPoint(mouse);
        for (Fragment fragment : scene.getFragments()) {
            if (fragment.isSelected()) {
                fragmentsStart.set(fragment, new LocalPoint(fragment.getPosition()));
            }
        }
    }

    @Override
    public void onMouseDragged(@Mandatory GlobalPoint mouse) {
        move(mouse);
    }

    @Override
    public void onMouseReleased(@Mandatory GlobalPoint mouse) {
        move(mouse);
    }

    private void move(@Mandatory GlobalPoint mouse) {
        GlobalVector globalDelta = GlobalPoint.minus(mouse, mouseStart);
        LocalVector localDelta = coordinateService.globalToLocal(scene.getCamera(), globalDelta);
        for (ReadablePair<Fragment, LocalPoint> pair : fragmentsStart) {
            pair.getKey().setPosition(LocalPoint.move(pair.getValue(), localDelta));
        }
        referencePositionService.computePositionsForSelectedFragmentReferences(scene);
    }
}
