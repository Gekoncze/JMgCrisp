package cz.mg.crisp.services;

import cz.mg.annotations.classes.Service;
import cz.mg.annotations.requirement.Mandatory;
import cz.mg.annotations.requirement.Optional;
import cz.mg.crisp.entity.*;

public @Service class ReferencePositionService {
    private static @Optional ReferencePositionService instance;

    public static @Mandatory ReferencePositionService getInstance() {
        if (instance == null) {
            instance = new ReferencePositionService();
            instance.fragmentPositionService = FragmentPositionService.getInstance();
            instance.lineCollisionService = LineCollisionService.getInstance();
        }
        return instance;
    }

    private FragmentPositionService fragmentPositionService;
    private LineCollisionService lineCollisionService;

    private ReferencePositionService() {
    }

    public void computePositionsForSelectedFragmentReferences(@Mandatory Scene scene) {
        for (Reference reference : scene.getReferences()) {
            if (reference.getSource().isSelected() || reference.getTarget().isSelected()) {
                computePosition(reference);
            }
        }
    }

    private void computePosition(Reference reference) {
        Line line = new Line(
            fragmentPositionService.getCenter(reference.getSource()),
            fragmentPositionService.getCenter(reference.getTarget())
        );

        reference.setBegin(collisionPoint(reference.getSource(), line));
        reference.setEnd(collisionPoint(reference.getTarget(), line));

        if (reference.getBegin() == null) {
            reference.setBegin(line.getBegin());
        }

        if (reference.getEnd() == null) {
            reference.setEnd(line.getEnd());
        }
    }

    private @Mandatory LocalPoint collisionPoint(@Mandatory Fragment fragment, @Mandatory Line line) {
        LocalPoint topLeft = fragmentPositionService.getTopLeft(fragment);
        LocalPoint topRight = fragmentPositionService.getTopRight(fragment);
        LocalPoint bottomLeft = fragmentPositionService.getBottomLeft(fragment);
        LocalPoint bottomRight = fragmentPositionService.getBottomRight(fragment);
        Side size = collision(topLeft, topRight, bottomLeft, bottomRight, line);

        if (size == null) {
            return fragmentPositionService.getCenter(fragment);
        }

        switch (size) {
            case TOP: return LocalPoint.center(topLeft, topRight); // TODO - temporary
            case BOTTOM: return LocalPoint.center(bottomLeft, bottomRight); // TODO - temporary
            case LEFT: return LocalPoint.center(topLeft, bottomLeft); // TODO - temporary
            case RIGHT: return LocalPoint.center(topRight, bottomRight); // TODO - temporary
            default: throw new IllegalStateException();
        }
    }

    private @Optional Side collision(
        @Mandatory LocalPoint topLeft,
        @Mandatory LocalPoint topRight,
        @Mandatory LocalPoint bottomLeft,
        @Mandatory LocalPoint bottomRight,
        @Mandatory Line line
    ) {
        if (lineCollisionService.collision(line, new Line(topLeft, topRight))) {
            return Side.TOP;
        }

        if (lineCollisionService.collision(line, new Line(bottomLeft, bottomRight))) {
            return Side.BOTTOM;
        }

        if (lineCollisionService.collision(line, new Line(topLeft, bottomLeft))) {
            return Side.LEFT;
        }

        if (lineCollisionService.collision(line, new Line(topRight, bottomRight))) {
            return Side.RIGHT;
        }

        return null;
    }

    private enum Side {
        TOP,
        BOTTOM,
        LEFT,
        RIGHT
    }
}
