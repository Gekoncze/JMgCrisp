package cz.mg.crisp.services;

import cz.mg.annotations.classes.Service;
import cz.mg.annotations.requirement.Mandatory;
import cz.mg.annotations.requirement.Optional;
import cz.mg.crisp.entity.model.math.LocalPoint;
import cz.mg.crisp.entity.model.math.LocalVector;
import cz.mg.crisp.entity.model.Fragment;

public @Service class FragmentPositionService {
    public static final int SECTION_SIZE = 16;
    private static final int NEW_FRAGMENT_POSITION_PADDING = 32;

    private static @Optional FragmentPositionService instance;

    public static @Mandatory FragmentPositionService getInstance() {
        if (instance == null) {
            instance = new FragmentPositionService();
        }
        return instance;
    }

    private FragmentPositionService() {
    }

    public @Mandatory LocalPoint getTopLeft(@Mandatory Fragment fragment) {
        return fragment.getPosition();
    }

    public @Mandatory LocalPoint getTopRight(@Mandatory Fragment fragment) {
        return LocalPoint.move(fragment.getPosition(), new LocalVector(fragment.getSize().getX(), 0));
    }

    public @Mandatory LocalPoint getBottomLeft(@Mandatory Fragment fragment) {
        return LocalPoint.move(fragment.getPosition(), new LocalVector(0, fragment.getSize().getY()));
    }

    public @Mandatory LocalPoint getBottomRight(@Mandatory Fragment fragment) {
        return LocalPoint.move(fragment.getPosition(), fragment.getSize());
    }

    public @Mandatory LocalPoint getCenter(@Mandatory Fragment fragment) {
        return LocalPoint.move(fragment.getPosition(), LocalVector.divide(fragment.getSize(), 2));
    }

    public @Mandatory LocalPoint getNewFragmentPosition(@Mandatory Fragment parent) {
        return LocalPoint.move(
            parent.getPosition(),
            new LocalVector(parent.getSize().getX() + NEW_FRAGMENT_POSITION_PADDING, 0)
        );
    }

    public @Mandatory LocalPoint getCloseButtonPosition(@Mandatory Fragment fragment) {
        return LocalPoint.move(
            fragment.getPosition(),
            new LocalVector(fragment.getSize().getX() - SECTION_SIZE, 0)
        );
    }

    public @Mandatory LocalVector getCloseButtonSize(@Mandatory Fragment fragment) {
        return new LocalVector(SECTION_SIZE, SECTION_SIZE);
    }
}
