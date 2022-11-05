package cz.mg.crisp.services;

import cz.mg.annotations.classes.Service;
import cz.mg.annotations.requirement.Mandatory;
import cz.mg.annotations.requirement.Optional;
import cz.mg.crisp.entity.*;

import java.awt.*;

public @Service class CoordinateService {
    private static @Optional CoordinateService instance;

    public static @Mandatory CoordinateService getInstance() {
        if (instance == null) {
            instance = new CoordinateService();
        }
        return instance;
    }

    private CoordinateService() {
    }

    public @Mandatory GlobalPoint convert(@Mandatory Point point) {
        return new GlobalPoint(point.x, point.y);
    }

    public @Mandatory LocalPoint globalToLocal(@Mandatory Camera camera, @Mandatory GlobalPoint point) {
        return new LocalPoint(
            point.getX() * camera.getZoom() + camera.getPosition().getX(),
            point.getY() * camera.getZoom() + camera.getPosition().getY()
        );
    }

    public @Mandatory GlobalPoint localToGlobal(@Mandatory Camera camera, @Mandatory LocalPoint point) {
        return new GlobalPoint(
            (point.getX() - camera.getPosition().getX()) / camera.getZoom(),
            (point.getY() - camera.getPosition().getY()) / camera.getZoom()
        );
    }

    public @Mandatory LocalVector globalToLocal(@Mandatory Camera camera, @Mandatory GlobalVector vector) {
        return new LocalVector(
            vector.getX() * camera.getZoom(),
            vector.getY() * camera.getZoom()
        );
    }

    public @Mandatory GlobalPoint localToGlobal(@Mandatory Camera camera, @Mandatory LocalVector vector) {
        return new GlobalPoint(
            vector.getX() / camera.getZoom(),
            vector.getY() / camera.getZoom()
        );
    }
}
