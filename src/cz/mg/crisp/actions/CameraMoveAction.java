package cz.mg.crisp.actions;

import cz.mg.annotations.classes.Utility;
import cz.mg.annotations.requirement.Mandatory;
import cz.mg.crisp.entity.model.math.GlobalPoint;
import cz.mg.crisp.entity.model.math.GlobalVector;
import cz.mg.crisp.entity.model.math.LocalPoint;
import cz.mg.crisp.entity.model.math.LocalVector;
import cz.mg.crisp.entity.model.Camera;
import cz.mg.crisp.services.CoordinateService;

public @Utility class CameraMoveAction implements Action {
    private final CoordinateService coordinateService = CoordinateService.getInstance();

    private final @Mandatory Camera camera;
    private final @Mandatory GlobalPoint mouseStart;
    private final @Mandatory LocalPoint cameraStart;

    public CameraMoveAction(@Mandatory Camera camera, @Mandatory GlobalPoint mouse) {
        this.camera = camera;
        this.mouseStart = new GlobalPoint(mouse);
        this.cameraStart = new LocalPoint(camera.getPosition());
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
        LocalVector localDelta = coordinateService.globalToLocal(camera, globalDelta);
        camera.setPosition(LocalPoint.move(cameraStart, LocalVector.inverse(localDelta)));
    }
}
