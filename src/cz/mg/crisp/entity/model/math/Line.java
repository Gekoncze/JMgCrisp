package cz.mg.crisp.entity.model.math;

import cz.mg.annotations.classes.Entity;
import cz.mg.annotations.requirement.Required;
import cz.mg.annotations.storage.Part;

public @Entity class Line {
    private LocalPoint begin;
    private LocalPoint end;

    public Line() {
    }

    public Line(LocalPoint begin, LocalPoint end) {
        this.begin = begin;
        this.end = end;
    }

    @Required @Part
    public LocalPoint getBegin() {
        return begin;
    }

    public void setBegin(LocalPoint begin) {
        this.begin = begin;
    }

    @Required @Part
    public LocalPoint getEnd() {
        return end;
    }

    public void setEnd(LocalPoint end) {
        this.end = end;
    }
}
