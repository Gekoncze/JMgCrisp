package cz.mg.crisp.services;

import cz.mg.annotations.classes.Service;
import cz.mg.annotations.requirement.Mandatory;
import cz.mg.annotations.requirement.Optional;
import cz.mg.crisp.entity.Fragment;
import cz.mg.crisp.entity.GlobalPoint;
import cz.mg.crisp.entity.LocalPoint;
import cz.mg.crisp.entity.Scene;

public @Service class SelectionService {
    private static @Optional SelectionService instance;

    public static @Mandatory SelectionService getInstance() {
        if (instance == null) {
            instance = new SelectionService();
            instance.coordinateService = CoordinateService.getInstance();
        }
        return instance;
    }

    private CoordinateService coordinateService;

    private SelectionService() {
    }

    public boolean isSelectedAt(@Mandatory GlobalPoint point, @Mandatory Scene scene) {
        LocalPoint local = coordinateService.globalToLocal(scene.getCamera(), point);

        for (Fragment fragment : scene.getFragments()) {
            if (fragment.isSelected()) {
                if (isInside(local, fragment)) {
                    return true;
                }
            }
        }

        return false;
    }

    public boolean select(@Mandatory GlobalPoint point, @Mandatory Scene scene, boolean incremental) {
        LocalPoint local = coordinateService.globalToLocal(scene.getCamera(), point);

        if (incremental) {
            for (Fragment fragment : scene.getFragments()) {
                if (isInside(local, fragment)) {
                    fragment.setSelected(!fragment.isSelected());
                }
            }
            return true;
        } else {
            for (Fragment fragment : scene.getFragments()) {
                fragment.setSelected(false);
            }

            for (Fragment fragment : scene.getFragments()) {
                if (isInside(local, fragment)) {
                    fragment.setSelected(true);
                    return true;
                }
            }
        }

        return false;
    }

    public boolean isInside(@Mandatory LocalPoint point, @Mandatory Fragment fragment) {
        int fx1 = fragment.getPosition().getX();
        int fy1 = fragment.getPosition().getY();
        int fx2 = fragment.getPosition().getX() + fragment.getSize().getX();
        int fy2 = fragment.getPosition().getY() + fragment.getSize().getY();
        int px = point.getX();
        int py = point.getY();
        return px >= fx1 && py >= fy1 && px < fx2 && py < fy2;
    }

    public boolean isInsideArea(@Mandatory LocalPoint begin, @Mandatory LocalPoint end, @Mandatory Fragment fragment) {
        int fx1 = fragment.getPosition().getX();
        int fy1 = fragment.getPosition().getY();
        int fx2 = fragment.getPosition().getX() + fragment.getSize().getX();
        int fy2 = fragment.getPosition().getY() + fragment.getSize().getY();

        int minX = Math.min(begin.getX(), end.getX());
        int maxX = Math.max(begin.getX(), end.getX());
        int minY = Math.min(begin.getY(), end.getY());
        int maxY = Math.max(begin.getY(), end.getY());

        return fx1 >= minX && fy1 >= minY && fx2 <= maxX && fy2 <= maxY;
    }
}
