package cz.mg.crisp.entity;

import cz.mg.annotations.classes.Entity;
import cz.mg.annotations.requirement.Required;
import cz.mg.annotations.storage.Link;
import cz.mg.annotations.storage.Part;
import cz.mg.annotations.storage.Value;

public @Entity class Fragment {
    private Object object;
    private LocalPoint position;
    private LocalVector size;
    private Boolean selected;

    public Fragment() {
    }

    @Required @Link
    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }

    @Required @Part
    public LocalPoint getPosition() {
        return position;
    }

    public void setPosition(LocalPoint position) {
        this.position = position;
    }

    @Required @Part
    public LocalVector getSize() {
        return size;
    }

    public void setSize(LocalVector size) {
        this.size = size;
    }

    @Required @Value
    public Boolean isSelected() {
        return selected;
    }

    public void setSelected(Boolean selected) {
        this.selected = selected;
    }
}
