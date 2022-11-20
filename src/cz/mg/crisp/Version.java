package cz.mg.crisp;

import cz.mg.annotations.classes.Entity;
import cz.mg.annotations.requirement.Required;
import cz.mg.annotations.storage.Value;

public @Entity class Version {
    private Integer major;
    private Integer minor;
    private Integer patch;

    public Version() {
    }

    public Version(Integer major, Integer minor, Integer patch) {
        this.major = major;
        this.minor = minor;
        this.patch = patch;
    }

    @Required @Value
    public Integer getMajor() {
        return major;
    }

    public void setMajor(Integer major) {
        this.major = major;
    }

    @Required @Value
    public Integer getMinor() {
        return minor;
    }

    public void setMinor(Integer minor) {
        this.minor = minor;
    }

    @Required @Value
    public Integer getPatch() {
        return patch;
    }

    public void setPatch(Integer patch) {
        this.patch = patch;
    }

    @Override
    public String toString() {
        return major + "." + minor + "." + patch;
    }
}
