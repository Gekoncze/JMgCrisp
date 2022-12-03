package cz.mg.crisp.entity.model;

import cz.mg.annotations.classes.Entity;
import cz.mg.annotations.requirement.Required;
import cz.mg.annotations.storage.Link;
import cz.mg.annotations.storage.Part;
import cz.mg.annotations.storage.Value;
import cz.mg.crisp.entity.model.math.LocalPoint;

public @Entity class Reference {
    private Row row;
    private Fragment source;
    private Fragment target;
    private LocalPoint begin;
    private LocalPoint end;
    private Boolean selected;

    public Reference() {
    }

    @Required @Link
    public Row getRow() {
        return row;
    }

    public void setRow(Row row) {
        this.row = row;
    }

    @Required @Link
    public Fragment getSource() {
        return source;
    }

    public void setSource(Fragment source) {
        this.source = source;
    }

    @Required @Link
    public Fragment getTarget() {
        return target;
    }

    public void setTarget(Fragment target) {
        this.target = target;
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

    @Required @Value
    public Boolean isSelected() {
        return selected;
    }

    public void setSelected(Boolean selected) {
        this.selected = selected;
    }
}
