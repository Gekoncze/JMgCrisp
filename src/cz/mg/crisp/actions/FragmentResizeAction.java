package cz.mg.crisp.actions;

import cz.mg.annotations.classes.Utility;
import cz.mg.annotations.requirement.Mandatory;
import cz.mg.collections.map.Map;
import cz.mg.collections.pair.ReadablePair;
import cz.mg.crisp.entity.*;
import cz.mg.crisp.services.CoordinateService;
import cz.mg.crisp.services.ReferencePositionService;

public @Utility class FragmentResizeAction implements Action {
    private static final LocalVector MIN_SIZE = new LocalVector(32, 32);

    private final CoordinateService coordinateService = CoordinateService.getInstance();
    private final ReferencePositionService referencePositionService = ReferencePositionService.getInstance();

    private final @Mandatory Scene scene;
    private final @Mandatory GlobalPoint mouseStart;
    private final @Mandatory Map<Fragment, LocalVector> fragmentOriginalSizes = new Map<>(200);

    public FragmentResizeAction(@Mandatory Scene scene, @Mandatory GlobalPoint mouse) {
        this.scene = scene;
        this.mouseStart = new GlobalPoint(mouse);
        for (Fragment fragment : scene.getFragments()) {
            if (fragment.isSelected()) {
                fragmentOriginalSizes.set(fragment, new LocalVector(fragment.getSize()));
            }
        }
    }

    @Override
    public void onMouseDragged(@Mandatory GlobalPoint mouse) {
        resize(mouse);
    }

    @Override
    public void onMouseReleased(@Mandatory GlobalPoint mouse) {
        resize(mouse);
    }

    private void resize(@Mandatory GlobalPoint mouse) {
        GlobalVector globalDelta = GlobalPoint.minus(mouse, mouseStart);
        LocalVector localDelta = coordinateService.globalToLocal(scene.getCamera(), globalDelta);
        for (ReadablePair<Fragment, LocalVector> pair : fragmentOriginalSizes) {
            pair.getKey().setSize(
                LocalVector.max(
                    LocalVector.plus(pair.getValue(), localDelta),
                    MIN_SIZE
                )
            );
        }
        referencePositionService.computePositionsForSelectedFragmentReferences(scene);
    }
}
