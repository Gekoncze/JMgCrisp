package cz.mg.crisp.entity.metadata;

import cz.mg.annotations.classes.Entity;
import cz.mg.annotations.requirement.Required;
import cz.mg.annotations.storage.Link;
import cz.mg.annotations.storage.Value;

import java.lang.reflect.Method;

@SuppressWarnings("unchecked")
public @Entity class FieldMetadata {
    private String name;
    private Method getter;

    public FieldMetadata() {
    }

    @Required @Value
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Required @Link
    public Method getGetter() {
        return getter;
    }

    public void setGetter(Method getter) {
        this.getter = getter;
    }

    public <T> T getValue(Object object) {
        try {
            return (T) getter.invoke(object);
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }
}
