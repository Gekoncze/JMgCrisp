package cz.mg.crisp.entity.model.math;

import cz.mg.annotations.classes.Entity;
import cz.mg.annotations.requirement.Mandatory;
import cz.mg.annotations.requirement.Required;
import cz.mg.annotations.storage.Value;

public @Entity class GlobalPoint {
    private Integer x;
    private Integer y;

    public GlobalPoint() {
        this(0, 0);
    }

    public GlobalPoint(Integer x, Integer y) {
        this.x = x;
        this.y = y;
    }

    public GlobalPoint(GlobalPoint point) {
        this.x = point.x;
        this.y = point.y;
    }

    @Required @Value
    public Integer getX() {
        return x;
    }

    public void setX(Integer x) {
        this.x = x;
    }

    @Required @Value
    public Integer getY() {
        return y;
    }

    public void setY(Integer y) {
        this.y = y;
    }

    public static GlobalVector minus(@Mandatory GlobalPoint a, @Mandatory GlobalPoint b) {
        return new GlobalVector(a.x - b.x, a.y - b.y);
    }

    public static GlobalPoint move(@Mandatory GlobalPoint p, @Mandatory GlobalVector v) {
        return new GlobalPoint(p.x + v.getX(), p.y + v.getY());
    }
}
