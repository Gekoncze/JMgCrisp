package cz.mg.crisp.entity.model;

import cz.mg.annotations.classes.Entity;
import cz.mg.annotations.requirement.Required;
import cz.mg.annotations.storage.Cache;
import cz.mg.annotations.storage.Link;
import cz.mg.annotations.storage.Part;
import cz.mg.annotations.storage.Value;
import cz.mg.collections.list.List;
import cz.mg.crisp.entity.model.math.LocalPoint;
import cz.mg.crisp.entity.model.math.LocalVector;

public @Entity class Fragment {
    private Object object;
    private LocalPoint position;
    private LocalVector size;
    private Boolean selected;
    private Boolean mandatory;
    private String header;
    private List<Row> rows;

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

    @Required @Value
    public Boolean isMandatory() {
        return mandatory;
    }

    public void setMandatory(Boolean mandatory) {
        this.mandatory = mandatory;
    }

    public Boolean getSelected() {
        return selected;
    }

    @Required @Cache
    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    @Required @Cache
    public List<Row> getRows() {
        return rows;
    }

    public void setRows(List<Row> rows) {
        this.rows = rows;
    }
}
