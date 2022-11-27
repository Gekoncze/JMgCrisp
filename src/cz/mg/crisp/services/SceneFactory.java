package cz.mg.crisp.services;

import cz.mg.annotations.classes.Service;
import cz.mg.annotations.requirement.Mandatory;
import cz.mg.annotations.requirement.Optional;
import cz.mg.crisp.entity.Scene;
import cz.mg.crisp.entity.metadata.Metadata;

public @Service class SceneFactory {
    private static @Optional SceneFactory instance;

    public static @Mandatory SceneFactory getInstance() {
        if (instance == null) {
            instance = new SceneFactory();
            instance.fragmentFactory = FragmentFactory.getInstance();
        }
        return instance;
    }

    private FragmentFactory fragmentFactory;

    private SceneFactory() {
    }

    public @Mandatory Scene create(@Mandatory Metadata metadata, @Mandatory Object root) {
        Scene scene = new Scene();
        scene.getFragments().addLast(fragmentFactory.create(metadata, root, true));
        return scene;
    }
}
