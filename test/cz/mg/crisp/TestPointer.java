package cz.mg.crisp;

import cz.mg.annotations.classes.Entity;
import cz.mg.annotations.requirement.Optional;
import cz.mg.annotations.storage.Link;

public @Entity class TestPointer<T extends TestClass> implements TestObject {
    private T target;

    public TestPointer() {
    }

    public TestPointer(T target) {
        this.target = target;
    }

    @Optional @Link
    public T getTarget() {
        return target;
    }

    public void setTarget(T target) {
        this.target = target;
    }
}
