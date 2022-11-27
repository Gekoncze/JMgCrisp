package cz.mg.crisp.services;

import cz.mg.annotations.classes.Service;
import cz.mg.annotations.requirement.Mandatory;
import cz.mg.annotations.requirement.Optional;
import cz.mg.crisp.entity.Fragment;
import cz.mg.crisp.entity.Reference;
import cz.mg.crisp.entity.Scene;
import cz.mg.crisp.entity.metadata.Metadata;

import java.util.Objects;

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

    public void open(@Mandatory Metadata metadata, @Mandatory Scene scene, @Mandatory Fragment parent, @Mandatory Object field) {
        Object target = metadata.getMetadataFactory().open(field);
        if (target != null) {
            Fragment existingFragment = getFragment(metadata, scene, target);
            if (existingFragment == null) {
                Fragment targetFragment = fragmentFactory.create(metadata, target, false);
                scene.getFragments().addLast(targetFragment);

                Reference reference = referenceFactory.create(parent, targetFragment);
                scene.getReferences().addLast(reference);

                targetFragment.setPosition(fragmentPositionService.getNewFragmentPosition(parent));

                referencePositionService.computePositionsForSelectedFragmentReferences(
                    scene, fragment -> fragment == parent || fragment == targetFragment
                );
            } else {
                Reference existingReference = getReference(scene, parent, existingFragment);
                if (existingReference == null) {
                    if (existingFragment != parent) {
                        Reference reference = referenceFactory.create(parent, existingFragment);
                        scene.getReferences().addLast(reference);

                        referencePositionService.computePositionsForSelectedFragmentReferences(
                            scene, fragment -> fragment == parent || fragment == existingFragment
                        );
                    }
                }
            }
        }
    }

    private @Optional Fragment getFragment(
        @Mandatory Metadata metadata,
        @Mandatory Scene scene,
        @Mandatory Object object
    ) {
        for (Fragment fragment : scene.getFragments()) {
            Long id1 = metadata.getMetadataFactory().getIdentity(fragment.getObject());
            Long id2 = metadata.getMetadataFactory().getIdentity(object);
            if (Objects.equals(id1, id2)) {
                return fragment;
            }
        }
        return null;
    }

    private @Optional Reference getReference(
        @Mandatory Scene scene,
        @Mandatory Fragment source,
        @Mandatory Fragment target
    ) {
        for (Reference reference : scene.getReferences()) {
            if (reference.getSource() == source && reference.getTarget() == target) {
                return reference;
            }
        }
        return null;
    }
}
