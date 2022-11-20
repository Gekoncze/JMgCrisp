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
    private MetadataFactory metadataFactory;
    private Map<Class, ClassMetadata> map = new Map<>(1000);

    public Metadata() {
    }

    public Metadata(MetadataFactory metadataFactory) {
        this.metadataFactory = metadataFactory;
    }

    @Required @Cache
    public MetadataFactory getMetadataFactory() {
        return metadataFactory;
    }

    public void setMetadataFactory(MetadataFactory metadataFactory) {
        this.metadataFactory = metadataFactory;
    }

    @Required @Part
    public Map<Class, ClassMetadata> getMap() {
        return map;
    }

    public void setMap(Map<Class, ClassMetadata> map) {
        this.map = map;
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
