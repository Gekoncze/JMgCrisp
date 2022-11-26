package cz.mg.crisp.services;

import cz.mg.annotations.classes.Service;
import cz.mg.annotations.requirement.Mandatory;
import cz.mg.annotations.requirement.Optional;
import cz.mg.crisp.entity.Fragment;
import cz.mg.crisp.entity.LocalPoint;
import cz.mg.crisp.entity.LocalVector;
import cz.mg.crisp.entity.metadata.Metadata;

public @Service class FragmentFactory {
    private static final LocalPoint DEFAULT_FRAGMENT_POSITION = new LocalPoint(0, 0);
    private static final LocalVector DEFAULT_FRAGMENT_SIZE = new LocalVector(128, 96);

    private static @Optional FragmentFactory instance;

    public static @Mandatory FragmentFactory getInstance() {
        if (instance == null) {
            instance = new FragmentFactory();
            instance.dataReader = DataReader.getInstance();
        }
        return instance;
    }

    private DataReader dataReader;

    private FragmentFactory() {
    }

    public @Mandatory Fragment create(@Mandatory Metadata metadata, @Mandatory Object object) {
        Fragment fragment = new Fragment();
        fragment.setObject(object);
        fragment.setHeader(dataReader.getHeader(metadata, object));
        fragment.setRows(dataReader.getRows(metadata, object));
        fragment.setPosition(new LocalPoint(DEFAULT_FRAGMENT_POSITION));
        fragment.setSize(new LocalVector(DEFAULT_FRAGMENT_SIZE));
        fragment.setSelected(false);
        return fragment;
    }
}
