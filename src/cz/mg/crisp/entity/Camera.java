package cz.mg.crisp.entity;

import cz.mg.annotations.classes.Entity;
import cz.mg.annotations.requirement.Required;

public @Entity class Camera {
    private LocalPoint position = new LocalPoint();
    private Integer zoom = 1;

    public Camera() {
    }

    @Required
    public LocalPoint getPosition() {
        return position;
    }

    public void setPosition(LocalPoint position) {
        this.position = position;
    }

    @Required
    public Integer getZoom() {
        return zoom;
    }

    public void setZoom(Integer zoom) {
        this.zoom = zoom;
    }
}
