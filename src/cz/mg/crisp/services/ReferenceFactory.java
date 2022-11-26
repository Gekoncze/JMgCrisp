package cz.mg.crisp.services;

import cz.mg.annotations.classes.Service;
import cz.mg.annotations.requirement.Mandatory;
import cz.mg.annotations.requirement.Optional;
import cz.mg.crisp.entity.Fragment;
import cz.mg.crisp.entity.LocalPoint;
import cz.mg.crisp.entity.Reference;

public @Service class ReferenceFactory {
    private static final @Mandatory LocalPoint DEFAULT_REFERENCE_POSITION = new LocalPoint();

    private static @Optional ReferenceFactory instance;

    public static @Mandatory ReferenceFactory getInstance() {
        if (instance == null) {
            instance = getInstance();
        }
        return instance;
    }

    private ReferenceFactory() {
    }

    public @Mandatory Reference create(@Mandatory Fragment source, @Mandatory Fragment target) {
        Reference reference = new Reference();
        reference.setSource(source);
        reference.setTarget(target);
        reference.setBegin(new LocalPoint(DEFAULT_REFERENCE_POSITION));
        reference.setEnd(new LocalPoint(DEFAULT_REFERENCE_POSITION));
        reference.setSelected(false);
        return reference;
    }
}
