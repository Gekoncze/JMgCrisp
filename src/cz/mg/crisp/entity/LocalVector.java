package cz.mg.crisp.entity;

import cz.mg.annotations.classes.Entity;
import cz.mg.annotations.requirement.Mandatory;
import cz.mg.annotations.requirement.Required;

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

    public static @Mandatory LocalVector inverse(@Mandatory LocalVector v) {
        return new LocalVector(-v.x, -v.y);
    }

    public static @Mandatory LocalVector divide(@Mandatory LocalVector v, int value) {
        return new LocalVector(v.x / value, v.y / value);
    }

    public static @Mandatory LocalVector multiply(@Mandatory LocalVector v, double value) {
        return new LocalVector((int)(v.x * value), (int)(v.y * value));
    }
}
