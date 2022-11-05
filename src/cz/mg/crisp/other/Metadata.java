package cz.mg.crisp.other;

import cz.mg.annotations.classes.Entity;
import cz.mg.annotations.requirement.Required;
import cz.mg.collections.map.Map;

@SuppressWarnings("rawtypes")
public @Entity class Metadata {
    private Map<Class, FragmentMetadata> map = new Map<>(1000);

    public Metadata() {
    }

    @Required
    public Map<Class, FragmentMetadata> getMap() {
        return map;
    }

    public void setMap(Map<Class, FragmentMetadata> map) {
        this.map = map;
    }
}
