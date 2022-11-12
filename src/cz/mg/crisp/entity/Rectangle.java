package cz.mg.crisp.entity;

import cz.mg.annotations.classes.Entity;
import cz.mg.annotations.requirement.Required;

public @Entity class Rectangle {
    private LocalPoint topLeft;
    private LocalPoint topRight;
    private LocalPoint bottomLeft;
    private LocalPoint bottomRight;
    private Line top;
    private Line bottom;
    private Line left;
    private Line right;

    public Rectangle() {
    }

    public Rectangle(LocalPoint topLeft, LocalPoint topRight, LocalPoint bottomLeft, LocalPoint bottomRight) {
        this.topLeft = topLeft;
        this.topRight = topRight;
        this.bottomLeft = bottomLeft;
        this.bottomRight = bottomRight;
        this.top = new Line(topLeft, topRight);
        this.bottom = new Line(bottomLeft, bottomRight);
        this.left = new Line(topLeft, bottomLeft);
        this.right = new Line(topRight, bottomRight);
    }

    @Required
    public LocalPoint getTopLeft() {
        return topLeft;
    }

    public void setTopLeft(LocalPoint topLeft) {
        this.topLeft = topLeft;
    }

    @Required
    public LocalPoint getTopRight() {
        return topRight;
    }

    public void setTopRight(LocalPoint topRight) {
        this.topRight = topRight;
    }

    @Required
    public LocalPoint getBottomLeft() {
        return bottomLeft;
    }

    public void setBottomLeft(LocalPoint bottomLeft) {
        this.bottomLeft = bottomLeft;
    }

    @Required
    public LocalPoint getBottomRight() {
        return bottomRight;
    }

    public void setBottomRight(LocalPoint bottomRight) {
        this.bottomRight = bottomRight;
    }

    @Required
    public Line getTop() {
        return top;
    }

    public void setTop(Line top) {
        this.top = top;
    }

    @Required
    public Line getBottom() {
        return bottom;
    }

    public void setBottom(Line bottom) {
        this.bottom = bottom;
    }

    @Required
    public Line getLeft() {
        return left;
    }

    public void setLeft(Line left) {
        this.left = left;
    }

    @Required
    public Line getRight() {
        return right;
    }

    public void setRight(Line right) {
        this.right = right;
    }
}
