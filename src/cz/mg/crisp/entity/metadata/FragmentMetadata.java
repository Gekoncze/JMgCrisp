package cz.mg.crisp.entity.metadata;

import cz.mg.annotations.classes.Entity;
import cz.mg.annotations.requirement.Required;
import cz.mg.annotations.storage.Part;
import cz.mg.annotations.storage.Value;
import cz.mg.collections.list.List;

public @Entity class FragmentMetadata {
    private String name;
    private List<FragmentFieldMetadata> fields = new List<>();

    public FragmentMetadata() {
    }

    @Required @Value
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Required @Part
    public List<FragmentFieldMetadata> getFields() {
        return fields;
    }

    public void setFields(List<FragmentFieldMetadata> fields) {
        this.fields = fields;
    }
}
