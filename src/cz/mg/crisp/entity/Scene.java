package cz.mg.crisp.entity;

import cz.mg.annotations.classes.Entity;
import cz.mg.annotations.requirement.Required;
import cz.mg.collections.list.List;

public @Entity class Scene {
    private Camera camera = new Camera();
    private List<Fragment> fragments = new List<>();
    private List<Reference> references = new List<>();

    public Scene() {
    }

    @Required
    public Camera getCamera() {
        return camera;
    }

    public void setCamera(Camera camera) {
        this.camera = camera;
    }

    @Required
    public List<Fragment> getFragments() {
        return fragments;
    }

    public void setFragments(List<Fragment> fragments) {
        this.fragments = fragments;
    }

    @Required
    public List<Reference> getReferences() {
        return references;
    }

    public void setReferences(List<Reference> references) {
        this.references = references;
    }
}
