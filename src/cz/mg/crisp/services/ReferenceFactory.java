package cz.mg.crisp.services;

import cz.mg.annotations.classes.Service;
import cz.mg.annotations.requirement.Mandatory;
import cz.mg.annotations.requirement.Optional;
import cz.mg.crisp.entity.model.Fragment;
import cz.mg.crisp.entity.model.Row;
import cz.mg.crisp.entity.model.math.LocalPoint;
import cz.mg.crisp.entity.model.Reference;

public @Service class ReferenceFactory {
    private static final @Mandatory LocalPoint DEFAULT_REFERENCE_POSITION = new LocalPoint();

    private static @Optional ReferenceFactory instance;

    public static @Mandatory ReferenceFactory getInstance() {
        if (instance == null) {
            instance = new ReferenceFactory();
        }
        return instance;
    }

    private ReferenceFactory() {
    }

    public @Mandatory Reference create(@Mandatory Row row, @Mandatory Fragment source, @Mandatory Fragment target) {
        if (source == target) {
            throw new UnsupportedOperationException(
                "Reference source fragment cannot be the same object as target fragment."
            );
        }

        Reference reference = new Reference();
        reference.setRow(row);
        reference.setSource(source);
        reference.setTarget(target);
        reference.setBegin(new LocalPoint(DEFAULT_REFERENCE_POSITION));
        reference.setEnd(new LocalPoint(DEFAULT_REFERENCE_POSITION));
        reference.setSelected(false);
        return reference;
    }
}
