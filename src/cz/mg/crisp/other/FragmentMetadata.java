package cz.mg.crisp.other;

import cz.mg.annotations.classes.Entity;
import cz.mg.annotations.requirement.Required;
import cz.mg.collections.list.List;

public @Entity class FragmentMetadata {
    private List<FragmentFieldMetadata> fields = new List<>();

    public FragmentMetadata() {
    }

    @Required
    public List<FragmentFieldMetadata> getFields() {
        return fields;
    }

    public void setFields(List<FragmentFieldMetadata> fields) {
        this.fields = fields;
    }
}
