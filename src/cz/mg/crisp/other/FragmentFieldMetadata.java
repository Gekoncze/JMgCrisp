package cz.mg.crisp.other;

import cz.mg.annotations.classes.Entity;
import cz.mg.annotations.requirement.Required;
import cz.mg.c.CObject;

import java.lang.reflect.Method;

@SuppressWarnings("unchecked")
public @Entity class FragmentFieldMetadata {
    private String name;
    private Method getter;

    public FragmentFieldMetadata() {
    }

    @Required
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Required
    public Method getGetter() {
        return getter;
    }

    public void setGetter(Method getter) {
        this.getter = getter;
    }

    public <T> T getValue(CObject object) {
        try {
            return (T) getter.invoke(object);
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }
}
