package cz.mg.crisp.services;

import cz.mg.annotations.classes.Service;
import cz.mg.annotations.requirement.Mandatory;
import cz.mg.annotations.requirement.Optional;
import cz.mg.crisp.entity.*;
import cz.mg.crisp.listeners.FragmentSelectListener;

public @Service class FragmentSelectionService {
    private static @Optional FragmentSelectionService instance;

    public static @Mandatory FragmentSelectionService getInstance() {
        if (instance == null) {
            instance = new FragmentSelectionService();
            instance.coordinateService = CoordinateService.getInstance();
            instance.fragmentPositionService = FragmentPositionService.getInstance();
        }
        return instance;
    }

    private CoordinateService coordinateService;
    private FragmentPositionService fragmentPositionService;

    private FragmentSelectionService() {
    }

    public boolean isSelectedAt(@Mandatory Scene scene, @Mandatory GlobalPoint point) {
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

    public boolean isSelectedResizableAt(@Mandatory Scene scene, @Mandatory GlobalPoint point, int radius) {
        LocalPoint local = coordinateService.globalToLocal(scene.getCamera(), point);

        for (Fragment fragment : scene.getFragments()) {
            if (fragment.isSelected()) {
                if (isResizableAt(fragment, local, radius)) {
                    return true;
                }
            }
        }

        return false;
    }

    private boolean isResizableAt(@Mandatory Fragment fragment, @Mandatory LocalPoint local, int radius) {
        LocalPoint target = LocalPoint.move(fragment.getPosition(), fragment.getSize());
        double distance = LocalPoint.distance(local, target);
        return distance <= radius;
    }

    public @Optional Fragment getCloseableAt(@Mandatory Scene scene, @Mandatory GlobalPoint point) {
        LocalPoint local = coordinateService.globalToLocal(scene.getCamera(), point);

        Fragment closeable = null;

        for (Fragment fragment : scene.getFragments()) {
            if (fragment.isSelected()) {
                if (isCloseableAt(fragment, local)) {
                    closeable = fragment;
                }
            }
        }

        return closeable;
    }

    private boolean isCloseableAt(@Mandatory Fragment fragment, @Mandatory LocalPoint local) {
        return isInside(
            local,
            fragmentPositionService.getCloseButtonPosition(fragment),
            fragmentPositionService.getCloseButtonSize(fragment)
        );
    }

    public @Optional Fragment getSelectedAt(@Mandatory Scene scene, @Mandatory GlobalPoint point) {
        LocalPoint local = coordinateService.globalToLocal(scene.getCamera(), point);

        Fragment selectedFragment = null;

        for (Fragment fragment : scene.getFragments()) {
            if (fragment.isSelected()) {
                if (isInside(local, fragment)) {
                    selectedFragment = fragment;
                }
            }
        }

        return selectedFragment;
    }

    public boolean select(
        @Mandatory Scene scene,
        @Mandatory GlobalPoint point,
        boolean incremental,
        @Optional FragmentSelectListener fragmentSelectListener
    ) {
        LocalPoint local = coordinateService.globalToLocal(scene.getCamera(), point);

        if (incremental) {
            for (Fragment fragment : scene.getFragments()) {
                if (isInside(local, fragment)) {
                    switchSelect(fragment, fragmentSelectListener);
                }
            }
            return true;
        } else {
            for (Fragment fragment : scene.getFragments()) {
                unselect(fragment, fragmentSelectListener);
            }

            Fragment selectedFragment = null;

            for (Fragment fragment : scene.getFragments()) {
                if (isInside(local, fragment)) {
                    selectedFragment = fragment;
                }
            }

            if (selectedFragment != null) {
                select(selectedFragment, fragmentSelectListener);
            }
        }

        return false;
    }

    private void select(@Mandatory Fragment fragment, @Optional FragmentSelectListener fragmentSelectListener) {
        fragment.setSelected(true);
        if (fragmentSelectListener != null) {
            fragmentSelectListener.onFragmentSelected(fragment);
        }
    }

    private void unselect(@Mandatory Fragment fragment, @Optional FragmentSelectListener fragmentSelectListener) {
        fragment.setSelected(false);
        if (fragmentSelectListener != null) {
            fragmentSelectListener.onFragmentSelected(null);
        }
    }

    private void switchSelect(@Mandatory Fragment fragment, @Optional FragmentSelectListener fragmentSelectListener) {
        if (fragment.isSelected()) {
            unselect(fragment, fragmentSelectListener);
        } else {
            select(fragment, fragmentSelectListener);
        }
    }

    public boolean isInside(@Mandatory LocalPoint point, @Mandatory Fragment fragment) {
        return isInside(point, fragment.getPosition(), fragment.getSize());
    }

    public boolean isInside(@Mandatory LocalPoint point, @Mandatory LocalPoint position, @Mandatory LocalVector size) {
        int fx1 = position.getX();
        int fy1 = position.getY();
        int fx2 = position.getX() + size.getX();
        int fy2 = position.getY() + size.getY();
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
