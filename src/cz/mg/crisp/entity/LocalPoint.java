package cz.mg.crisp.entity;

import cz.mg.annotations.classes.Entity;
import cz.mg.annotations.requirement.Mandatory;
import cz.mg.annotations.requirement.Required;

public @Entity class LocalPoint {
    private Integer x;
    private Integer y;

    public LocalPoint() {
        this(0, 0);
    }

    public LocalPoint(Integer x, Integer y) {
        this.x = x;
        this.y = y;
    }

    public LocalPoint(LocalPoint point) {
        this.x = point.x;
        this.y = point.y;
    }

    @Required
    public Integer getX() {
        return x;
    }

    public void setX(Integer x) {
        this.x = x;
    }

    @Required
    public Integer getY() {
        return y;
    }

    public void setY(Integer y) {
        this.y = y;
    }

    public static LocalVector minus(@Mandatory LocalPoint a, @Mandatory LocalPoint b) {
        return new LocalVector(a.x - b.x, a.y - b.y);
    }

    public static LocalPoint move(@Mandatory LocalPoint p, @Mandatory LocalVector v) {
        return new LocalPoint(p.x + v.getX(), p.y + v.getY());
    }
}
