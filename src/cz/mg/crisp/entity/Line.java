package cz.mg.crisp.entity;

import cz.mg.annotations.classes.Entity;
import cz.mg.annotations.requirement.Required;

public @Entity class Line {
    private LocalPoint begin;
    private LocalPoint end;

    public Line() {
    }

    public Line(LocalPoint begin, LocalPoint end) {
        this.begin = begin;
        this.end = end;
    }

    @Required
    public LocalPoint getBegin() {
        return begin;
    }

    public void setBegin(LocalPoint begin) {
        this.begin = begin;
    }

    @Required
    public LocalPoint getEnd() {
        return end;
    }

    public void setEnd(LocalPoint end) {
        this.end = end;
    }
}
