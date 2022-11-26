package cz.mg.crisp.services;

import cz.mg.annotations.classes.Service;
import cz.mg.annotations.requirement.Mandatory;
import cz.mg.annotations.requirement.Optional;
import cz.mg.crisp.entity.metadata.Metadata;
import cz.mg.crisp.ui.CrispWindow;

public @Service class CrispWindowFactory {
    private static @Optional CrispWindowFactory instance;

    public static @Mandatory CrispWindowFactory getInstance() {
        if (instance == null) {
            instance = new CrispWindowFactory();
            instance.sceneFactory = SceneFactory.getInstance();
        }
        return instance;
    }

    private SceneFactory sceneFactory;

    private CrispWindowFactory() {
    }

    public @Mandatory CrispWindow create(@Mandatory MetadataFactory metadataFactory, @Mandatory Object root) {
        Metadata metadata = new Metadata(metadataFactory);
        CrispWindow window = new CrispWindow(metadata);
        window.getScenePanel().setScene(sceneFactory.create(metadata, root));
        window.setVisible(true);
        return window;
    }
}
