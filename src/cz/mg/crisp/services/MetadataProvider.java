package cz.mg.crisp.services;

import cz.mg.annotations.classes.Service;
import cz.mg.annotations.requirement.Mandatory;
import cz.mg.annotations.requirement.Optional;
import cz.mg.c.CObject;
import cz.mg.crisp.other.FragmentMetadata;
import cz.mg.crisp.other.Metadata;

public @Service class MetadataProvider {
    private static @Optional MetadataProvider instance;

    public static @Mandatory MetadataProvider getInstance() {
        if (instance == null) {
            instance = new MetadataProvider();
            instance.metadataFactory = MetadataFactory.getInstance();
        }
        return instance;
    }

    private MetadataFactory metadataFactory;

    private MetadataProvider() {
    }

    public @Mandatory FragmentMetadata get(@Mandatory Metadata metadata, @Mandatory CObject object) {
        FragmentMetadata fragmentMetadata = metadata.getMap().getOptional(object.getClass());
        if (fragmentMetadata == null) {
            fragmentMetadata = metadataFactory.create(object.getClass());
            metadata.getMap().set(object.getClass(), fragmentMetadata);
        }
        return fragmentMetadata;
    }
}
