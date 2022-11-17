package cz.mg.crisp.graphics;

import cz.mg.annotations.classes.Service;
import cz.mg.annotations.requirement.Mandatory;
import cz.mg.annotations.requirement.Optional;
import cz.mg.crisp.actions.RangeSelectionAction;
import cz.mg.crisp.entity.Camera;
import cz.mg.crisp.entity.GlobalPoint;
import cz.mg.crisp.services.CoordinateService;

import java.awt.*;
import java.awt.geom.AffineTransform;

public @Service class RangeSelectionActionRenderer {
    private static final Stroke STROKE = new BasicStroke(
        1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 1, new float[]{ 1 }, 1
    );

    private static @Optional RangeSelectionActionRenderer instance;

    public static @Mandatory RangeSelectionActionRenderer getInstance() {
        if (instance == null) {
            instance = new RangeSelectionActionRenderer();
            instance.coordinateService = CoordinateService.getInstance();
        }
        return instance;
    }

    private CoordinateService coordinateService;

    private RangeSelectionActionRenderer() {
    }

    public void draw(@Mandatory Graphics2D g, @Mandatory Camera camera, @Mandatory RangeSelectionAction action) {
        Stroke originalStroke = g.getStroke();
        g.setXORMode(Color.WHITE);

        GlobalPoint begin = coordinateService.localToGlobal(camera, action.getBegin());
        GlobalPoint end = coordinateService.localToGlobal(camera, action.getEnd());

        int minX = Math.min(begin.getX(), end.getX());
        int maxX = Math.max(begin.getX(), end.getX());
        int minY = Math.min(begin.getY(), end.getY());
        int maxY = Math.max(begin.getY(), end.getY());

        g.setStroke(STROKE);

        g.drawRect(minX, minY, maxX - minX, maxY - minY);

        g.setXORMode(Color.BLACK);
        g.setStroke(originalStroke);
    }
}
