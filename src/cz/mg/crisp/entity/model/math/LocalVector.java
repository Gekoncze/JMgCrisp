package cz.mg.crisp.entity.model.math;

import cz.mg.annotations.classes.Entity;
import cz.mg.annotations.requirement.Mandatory;
import cz.mg.annotations.requirement.Required;
import cz.mg.annotations.storage.Value;

public @Entity class LocalVector {
    private Integer x;
    private Integer y;

    public LocalVector() {
        this(0, 0);
    }

    public LocalVector(Integer x, Integer y) {
        this.x = x;
        this.y = y;
    }

    public LocalVector(LocalVector vector) {
        this.x = vector.x;
        this.y = vector.y;
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

    public static @Mandatory LocalVector inverse(@Mandatory LocalVector v) {
        return new LocalVector(-v.x, -v.y);
    }

    public static @Mandatory LocalVector divide(@Mandatory LocalVector v, int value) {
        return new LocalVector(v.x / value, v.y / value);
    }

    public static @Mandatory LocalVector multiply(@Mandatory LocalVector v, double value) {
        return new LocalVector((int)(v.x * value), (int)(v.y * value));
    }

    public static @Mandatory LocalVector plus(@Mandatory LocalVector a, @Mandatory LocalVector b) {
        return new LocalVector(a.x + b.x, a.y + b.y);
    }

    public static @Mandatory LocalVector max(@Mandatory LocalVector a, @Mandatory LocalVector b) {
        return new LocalVector(
            Math.max(a.x, b.x),
            Math.max(a.y, b.y)
        );
    }

    public static double length(@Mandatory LocalVector v) {
        return Math.sqrt(v.x * v.x + v.y * v.y);
    }
}
