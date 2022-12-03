package cz.mg.crisp.entity.model;

import cz.mg.annotations.classes.Entity;
import cz.mg.annotations.requirement.Required;
import cz.mg.annotations.storage.Part;
import cz.mg.annotations.storage.Value;
import cz.mg.crisp.entity.model.math.LocalPoint;

public @Entity class Camera {
    private LocalPoint position = new LocalPoint();
    private Integer zoom = 1;

    public Camera() {
    }

    @Required @Part
    public LocalPoint getPosition() {
        return position;
    }

    public void setPosition(LocalPoint position) {
        this.position = position;
    }

    @Required @Value
    public Integer getZoom() {
        return zoom;
    }

    public void setZoom(Integer zoom) {
        this.zoom = zoom;
    }
}
