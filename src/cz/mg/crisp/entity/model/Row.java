package cz.mg.crisp.entity.model;

import cz.mg.annotations.classes.Entity;
import cz.mg.annotations.requirement.Required;
import cz.mg.annotations.storage.Value;

public @Entity class Row {
    private String name;
    private String value;

    public Row() {
    }

    public Row(String name, String value) {
        this.name = name;
        this.value = value;
    }

    @Required @Value
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Required @Value
    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
