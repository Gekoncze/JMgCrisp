package cz.mg.crisp.services;

import cz.mg.annotations.classes.Service;
import cz.mg.annotations.requirement.Mandatory;
import cz.mg.annotations.requirement.Optional;
import cz.mg.crisp.entity.Fragment;
import cz.mg.crisp.entity.Scene;

public @Service class CloseService {
    private static @Optional CloseService instance;

    public static @Mandatory CloseService getInstance() {
        if (instance == null) {
            instance = new CloseService();
        }
        return instance;
    }

    private CloseService() {
    }

    public void close(@Mandatory Scene scene, @Mandatory Fragment fragment) {
        scene.getFragments().removeIf(f -> f == fragment);
        scene.getReferences().removeIf(r -> r.getSource() == fragment || r.getTarget() == fragment);
    }
}
