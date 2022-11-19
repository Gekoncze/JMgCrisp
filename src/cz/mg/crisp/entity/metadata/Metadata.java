package cz.mg.crisp.entity.metadata;

import cz.mg.annotations.classes.Utility;
import cz.mg.annotations.requirement.Mandatory;
import cz.mg.annotations.requirement.Required;
import cz.mg.annotations.storage.Cache;
import cz.mg.annotations.storage.Part;
import cz.mg.collections.map.Map;
import cz.mg.crisp.services.MetadataFactory;

@SuppressWarnings("rawtypes")
public @Utility class Metadata {
    private Map<Class, ClassMetadata> map = new Map<>(1000);
    private MetadataFactory metadataFactory;

    public Metadata() {
    }

    @Required @Part
    public Map<Class, ClassMetadata> getMap() {
        return map;
    }

    public void setMap(Map<Class, ClassMetadata> map) {
        this.map = map;
    }

    @Mandatory @Cache
    public MetadataFactory getMetadataFactory() {
        return metadataFactory;
    }

    public void setMetadataFactory(MetadataFactory metadataFactory) {
        this.metadataFactory = metadataFactory;
    }

    public @Mandatory ClassMetadata get(@Mandatory Object object) {
        ClassMetadata classMetadata = map.getOptional(object.getClass());
        if (classMetadata == null) {
            classMetadata = metadataFactory.create(object.getClass());
            map.set(object.getClass(), classMetadata);
        }
        return classMetadata;
    }
}
