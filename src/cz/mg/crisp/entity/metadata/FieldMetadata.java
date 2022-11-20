package cz.mg.crisp.entity.metadata;

import cz.mg.annotations.classes.Entity;
import cz.mg.annotations.requirement.Mandatory;
import cz.mg.annotations.requirement.Optional;
import cz.mg.annotations.requirement.Required;
import cz.mg.annotations.storage.Link;
import cz.mg.annotations.storage.Value;

public @Entity class FieldMetadata {
    private String name;
    private FieldReader fieldReader;

    public FieldMetadata() {
    }

    @Required @Value
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Required @Link
    public FieldReader getFieldReader() {
        return fieldReader;
    }

    public void setFieldReader(FieldReader fieldReader) {
        this.fieldReader = fieldReader;
    }

    public @Optional Object getValue(@Mandatory Object object) {
        return fieldReader.read(object);
    }
}
