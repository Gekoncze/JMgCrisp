package cz.mg.crisp.services;

import cz.mg.annotations.classes.Service;
import cz.mg.annotations.requirement.Mandatory;
import cz.mg.annotations.requirement.Optional;
import cz.mg.crisp.entity.Camera;
import cz.mg.crisp.entity.GlobalPoint;
import cz.mg.crisp.entity.LocalPoint;
import cz.mg.crisp.entity.LocalVector;

public @Service class ZoomService {
    private static @Optional ZoomService instance;

    public static @Mandatory ZoomService getInstance() {
        if (instance == null) {
            instance = new ZoomService();
            instance.coordinateService = CoordinateService.getInstance();
        }
        return instance;
    }

    private CoordinateService coordinateService;

    private ZoomService() {
    }

    public void zoom(@Mandatory Camera camera, @Mandatory GlobalPoint point, int direction) {
        LocalPoint localBefore = coordinateService.globalToLocal(camera, point);

        if (direction > 0) {
            if (camera.getZoom() < 10) {
                camera.setZoom(
                    camera.getZoom() + 1
                );
            }
        } else {
            if (camera.getZoom() > 1) {
                camera.setZoom(
                    camera.getZoom() - 1
                );
            }
        }

        LocalPoint localAfter = coordinateService.globalToLocal(camera, point);

        camera.setPosition(
            LocalPoint.move(
                camera.getPosition(),
                LocalVector.inverse(
                    LocalPoint.minus(localAfter, localBefore)
                )
            )
        );
    }
}
