package cz.mg.crisp.entity;

import cz.mg.annotations.classes.Entity;
import cz.mg.annotations.requirement.Required;
import cz.mg.c.CObject;

public @Entity class Fragment {
    private CObject object;
    private LocalPoint position;
    private LocalVector size;
    private Boolean selected;

    public Fragment() {
    }

    @Required
    public CObject getObject() {
        return object;
    }

    public void setObject(CObject object) {
        this.object = object;
    }

    @Required
    public LocalPoint getPosition() {
        return position;
    }

    public void setPosition(LocalPoint position) {
        this.position = position;
    }

    @Required
    public LocalVector getSize() {
        return size;
    }

    public void setSize(LocalVector size) {
        this.size = size;
    }

    @Required
    public Boolean isSelected() {
        return selected;
    }

    public void setSelected(Boolean selected) {
        this.selected = selected;
    }
}
