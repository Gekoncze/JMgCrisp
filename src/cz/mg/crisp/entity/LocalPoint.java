package cz.mg.crisp.entity;

import cz.mg.annotations.classes.Entity;
import cz.mg.annotations.requirement.Mandatory;
import cz.mg.annotations.requirement.Required;
import cz.mg.annotations.storage.Value;

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

    public static LocalVector minus(@Mandatory LocalPoint a, @Mandatory LocalPoint b) {
        return new LocalVector(a.x - b.x, a.y - b.y);
    }

    public static LocalPoint move(@Mandatory LocalPoint p, @Mandatory LocalVector v) {
        return new LocalPoint(p.x + v.getX(), p.y + v.getY());
    }

    public static LocalPoint center(@Mandatory LocalPoint a, @Mandatory LocalPoint b) {
        return new LocalPoint((a.x + b.x) / 2, (a.y + b.y) / 2);
    }

    public static LocalPoint combine(@Mandatory LocalPoint a, @Mandatory LocalPoint b, double t) {
        return new LocalPoint(
            combine(a.x, b.x, t),
            combine(a.y, b.y, t)
        );
    }

    private static int combine(int a, int b, double t) {
        return (int) (a * (1.0 - t) + b * t);
    }

    public static double distance(@Mandatory LocalPoint a, @Mandatory LocalPoint b) {
        int dx = a.x - b.x;
        int dy = a.y - b.y;
        return Math.sqrt(dx*dx + dy*dy);
    }
}
