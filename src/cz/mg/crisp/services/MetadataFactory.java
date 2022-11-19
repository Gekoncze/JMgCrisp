package cz.mg.crisp.services;

import cz.mg.annotations.classes.Service;
import cz.mg.annotations.requirement.Mandatory;
import cz.mg.annotations.requirement.Optional;
import cz.mg.crisp.entity.metadata.ClassMetadata;

@SuppressWarnings("rawtypes")
public @Service interface MetadataFactory {
    boolean isCompatible(@Mandatory Class clazz);
    @Mandatory ClassMetadata create(@Mandatory Class clazz);
    @Optional Long getIdentity(@Mandatory Object object);
}
