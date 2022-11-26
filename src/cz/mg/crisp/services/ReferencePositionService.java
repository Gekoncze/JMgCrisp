package cz.mg.crisp.services;

import cz.mg.annotations.classes.Service;
import cz.mg.annotations.classes.Utility;
import cz.mg.annotations.requirement.Mandatory;
import cz.mg.annotations.requirement.Optional;
import cz.mg.collections.array.Array;
import cz.mg.collections.list.List;
import cz.mg.collections.map.Map;
import cz.mg.collections.pair.ReadablePair;
import cz.mg.crisp.entity.*;

public @Service class ReferencePositionService {
    private static final int CACHE = 100;

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
        computePositionsForSelectedFragmentReferences(scene, Fragment::isSelected);
    }

    public void computePositionsForSelectedFragmentReferences(@Mandatory Scene scene, @Mandatory Selector selector) {
        Map<Fragment, Boolean> affectedFragments = computeAffectedFragments(scene, selector);

        Map<Fragment, Rectangle> rectangles = new Map<>(CACHE);
        computeRectangles(scene, rectangles, affectedFragments);

        Map<Fragment, Array<List<Reference>>> sides = new Map<>(CACHE);
        computeSides(scene, sides, rectangles, affectedFragments);

        computePositions(sides, rectangles);
    }

    private @Mandatory Map<Fragment, Boolean> computeAffectedFragments(
        @Mandatory Scene scene,
        @Mandatory Selector selector
    ) {
        Map<Fragment, Boolean> map = new Map<>(CACHE);

        for (Fragment fragment : scene.getFragments()) {
            if (selector.isSelected(fragment)) {
                map.set(fragment, true);
            }
        }

        for (Reference reference : scene.getReferences()) {
            if (selector.isSelected(reference.getSource()) || selector.isSelected(reference.getTarget())) {
                map.set(reference.getSource(), true);
                map.set(reference.getTarget(), true);
            }
        }

        return map;
    }

    private void computeRectangles(
        @Mandatory Scene scene,
        @Mandatory Map<Fragment, Rectangle> rectangles,
        @Mandatory Map<Fragment, Boolean> affectedFragments
    ) {
        for (Reference reference : scene.getReferences()) {
            boolean sourceAffected = affectedFragments.getOptional(reference.getSource()) != null;
            boolean targetAffected = affectedFragments.getOptional(reference.getTarget()) != null;
            if (sourceAffected || targetAffected) {
                computeRectangle(reference.getSource(), rectangles);
                computeRectangle(reference.getTarget(), rectangles);
            }
        }
    }

    private void computeRectangle(
        @Mandatory Fragment fragment,
        @Mandatory Map<Fragment, Rectangle> rectangles
    ) {
        if (rectangles.getOptional(fragment) == null) {
            rectangles.set(
                fragment,
                new Rectangle(
                    fragmentPositionService.getTopLeft(fragment),
                    fragmentPositionService.getTopRight(fragment),
                    fragmentPositionService.getBottomLeft(fragment),
                    fragmentPositionService.getBottomRight(fragment)
                )
            );
        }
    }

    private void computePositions(
        @Mandatory Map<Fragment, Array<List<Reference>>> sides,
        @Mandatory Map<Fragment, Rectangle> rectangles
    ) {
        for (ReadablePair<Fragment, Array<List<Reference>>> pair : sides) {
            Fragment fragment = pair.getKey();
            Rectangle rectangle = rectangles.get(fragment);
            distributePositions(fragment, rectangle.getTop(), pair.getValue().get(Side.TOP.ordinal()));
            distributePositions(fragment, rectangle.getBottom(), pair.getValue().get(Side.BOTTOM.ordinal()));
            distributePositions(fragment, rectangle.getLeft(), pair.getValue().get(Side.LEFT.ordinal()));
            distributePositions(fragment, rectangle.getRight(), pair.getValue().get(Side.RIGHT.ordinal()));
        }
    }

    private void distributePositions(
        @Mandatory Fragment fragment,
        @Mandatory Line line,
        @Mandatory List<Reference> references
    ) {
        int i = 0;
        for (Reference reference : references) {
            double t = (double) (i + 1) / (references.count() + 1);
            LocalPoint point = LocalPoint.combine(line.getBegin(), line.getEnd(), t);

            if (reference.getSource() == fragment) {
                reference.setBegin(point);
            }

            if (reference.getTarget() == fragment) {
                reference.setEnd(point);
            }

            i++;
        }
    }

    private void computeSides(
        @Mandatory Scene scene,
        @Mandatory Map<Fragment, Array<List<Reference>>> sides,
        @Mandatory Map<Fragment, Rectangle> rectangles,
        @Mandatory Map<Fragment, Boolean> affectedFragments
    ) {
        for (Reference reference : scene.getReferences()) {
            boolean sourceAffected = affectedFragments.getOptional(reference.getSource()) != null;
            boolean targetAffected = affectedFragments.getOptional(reference.getTarget()) != null;
            if (sourceAffected || targetAffected) {
                computeSide(reference, sides, rectangles);
            }
        }
    }

    private void computeSide(
        @Mandatory Reference reference,
        @Mandatory Map<Fragment, Array<List<Reference>>> sides,
        @Mandatory Map<Fragment, Rectangle> rectangles
    ) {
        LocalPoint sourceCenter = fragmentPositionService.getCenter(reference.getSource());
        LocalPoint targetCenter = fragmentPositionService.getCenter(reference.getTarget());

        reference.getBegin().setX(sourceCenter.getX());
        reference.getBegin().setY(sourceCenter.getY());
        reference.getEnd().setX(targetCenter.getX());
        reference.getEnd().setY(targetCenter.getY());

        Line line = new Line(sourceCenter, targetCenter);

        Side sourceSide = collision(reference.getSource(), line, rectangles);
        if (sourceSide != null) {
            setToMap(sides, reference.getSource(), sourceSide, reference);
        }

        Side targetSide = collision(reference.getTarget(), line, rectangles);
        if (targetSide != null) {
            setToMap(sides, reference.getTarget(), targetSide, reference);
        }
    }

    private void setToMap(
        @Mandatory Map<Fragment, Array<List<Reference>>> map,
        @Mandatory Fragment fragment,
        @Mandatory Side side,
        @Mandatory Reference reference
    ) {
        Array<List<Reference>> sides = map.getOptional(fragment);

        if (sides == null) {
            sides = new Array<>(4);
            sides.set(Side.TOP.ordinal(), new List<>());
            sides.set(Side.BOTTOM.ordinal(), new List<>());
            sides.set(Side.LEFT.ordinal(), new List<>());
            sides.set(Side.RIGHT.ordinal(), new List<>());
            map.set(fragment, sides);
        }

        sides.get(side.ordinal()).addLast(reference);
    }

    private @Optional Side collision(
        @Mandatory Fragment fragment,
        @Mandatory Line line,
        @Mandatory Map<Fragment, Rectangle> rectangles
    ) {
        Rectangle rectangle = rectangles.get(fragment);

        if (lineCollisionService.collision(line, rectangle.getTop())) {
            return Side.TOP;
        }

        if (lineCollisionService.collision(line, rectangle.getBottom())) {
            return Side.BOTTOM;
        }

        if (lineCollisionService.collision(line, rectangle.getLeft())) {
            return Side.LEFT;
        }

        if (lineCollisionService.collision(line, rectangle.getRight())) {
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

    public @Utility interface Selector {
        boolean isSelected(@Mandatory Fragment fragment);
    }
}
