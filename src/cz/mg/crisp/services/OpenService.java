package cz.mg.crisp.services;

import cz.mg.annotations.classes.Service;
import cz.mg.annotations.requirement.Mandatory;
import cz.mg.annotations.requirement.Optional;
import cz.mg.crisp.entity.Fragment;
import cz.mg.crisp.entity.Reference;
import cz.mg.crisp.entity.Scene;
import cz.mg.crisp.entity.metadata.Metadata;

public @Service class OpenService {
    private static @Optional OpenService instance;

    public static @Mandatory OpenService getInstance() {
        if (instance == null) {
            instance = new OpenService();
            instance.fragmentFactory = FragmentFactory.getInstance();
            instance.referenceFactory = ReferenceFactory.getInstance();
            instance.fragmentPositionService = FragmentPositionService.getInstance();
            instance.referencePositionService = ReferencePositionService.getInstance();
        }
        return instance;
    }

    private FragmentFactory fragmentFactory;
    private ReferenceFactory referenceFactory;
    private FragmentPositionService fragmentPositionService;
    private ReferencePositionService referencePositionService;

    private OpenService() {
    }

    public void open(@Mandatory Metadata metadata, @Mandatory Scene scene, @Mandatory Fragment parent, @Mandatory Object object) {
        if (!isOpened(scene, object)) {
            Object target = metadata.getMetadataFactory().open(parent.getObject(), object);
            if (target != null) {
                Fragment targetFragment = fragmentFactory.create(metadata, target);
                scene.getFragments().addLast(targetFragment);

                Reference reference = referenceFactory.create(parent, targetFragment);
                scene.getReferences().addLast(reference);

                targetFragment.setPosition(fragmentPositionService.getNewFragmentPosition(parent));
                referencePositionService.computePositionsForSelectedFragmentReferences(
                    scene, fragment -> fragment == parent || fragment == targetFragment
                );
            }
        }
    }

    private boolean isOpened(@Mandatory Scene scene, @Mandatory Object object) {
        for (Fragment fragment : scene.getFragments()) {
            if (fragment.getObject() == object) {
                return true;
            }
        }
        return false;
    }
}