package cz.mg.crisp.entity.metadata;

import cz.mg.annotations.classes.Utility;
import cz.mg.annotations.requirement.Mandatory;
import cz.mg.annotations.requirement.Optional;
import cz.mg.annotations.requirement.Required;
import cz.mg.annotations.storage.Cache;
import cz.mg.annotations.storage.Part;
import cz.mg.collections.map.Map;
import cz.mg.crisp.services.MetadataFactory;

@SuppressWarnings("rawtypes")
public @Utility class SceneMetadata {
    private Map<Class, FragmentMetadata> map = new Map<>(1000);
    private MetadataFactory metadataFactory;

    public SceneMetadata() {
    }

    @Required @Part
    public Map<Class, FragmentMetadata> getMap() {
        return map;
    }

    public void setMap(Map<Class, FragmentMetadata> map) {
        this.map = map;
    }

    @Mandatory @Cache
    public MetadataFactory getMetadataFactory() {
        return metadataFactory;
    }

    public void setMetadataFactory(MetadataFactory metadataFactory) {
        this.metadataFactory = metadataFactory;
    }

    public @Mandatory FragmentMetadata get(@Mandatory Object object) {
        FragmentMetadata fragmentMetadata = map.getOptional(object.getClass());
        if (fragmentMetadata == null) {
            fragmentMetadata = metadataFactory.create(object.getClass());
            map.set(object.getClass(), fragmentMetadata);
        }
        return fragmentMetadata;
    }
}
