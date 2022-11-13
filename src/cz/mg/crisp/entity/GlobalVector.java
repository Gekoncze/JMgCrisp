package cz.mg.crisp.entity;

import cz.mg.annotations.classes.Entity;
import cz.mg.annotations.requirement.Mandatory;
import cz.mg.annotations.requirement.Required;
import cz.mg.annotations.storage.Value;

public @Entity class GlobalVector {
    private Integer x;
    private Integer y;

    public GlobalVector() {
        this(0, 0);
    }

    public GlobalVector(Integer x, Integer y) {
        this.x = x;
        this.y = y;
    }

    public GlobalVector(GlobalVector vector) {
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

    public static @Mandatory GlobalVector inverse(@Mandatory GlobalVector v) {
        return new GlobalVector(-v.x, -v.y);
    }
}
