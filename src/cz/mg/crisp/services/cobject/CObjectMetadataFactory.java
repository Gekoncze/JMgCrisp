package cz.mg.crisp.services.cobject;

import cz.mg.annotations.classes.Service;
import cz.mg.annotations.requirement.Mandatory;
import cz.mg.annotations.requirement.Optional;
import cz.mg.c.CObject;
import cz.mg.crisp.entity.metadata.FragmentFieldMetadata;
import cz.mg.crisp.entity.metadata.FragmentMetadata;
import cz.mg.crisp.services.MetadataFactory;

import java.lang.reflect.Method;

@SuppressWarnings("rawtypes")
public @Service class CObjectMetadataFactory implements MetadataFactory {
    private static @Optional CObjectMetadataFactory instance;

    public static @Mandatory CObjectMetadataFactory getInstance() {
        if (instance == null) {
            instance = new CObjectMetadataFactory();
        }
        return instance;
    }

    private CObjectMetadataFactory() {
    }

    @Override
    public boolean isCompatible(@Mandatory Class clazz) {
        return CObject.class.isAssignableFrom(clazz);
    }

    @Override
    public @Mandatory FragmentMetadata create(@Mandatory Class clazz) {
        checkCompatibility(clazz);

        FragmentMetadata fragmentMetadata = new FragmentMetadata();
        fragmentMetadata.setName(clazz.getSimpleName());

        for (Method method : clazz.getDeclaredMethods()) {
            if (!method.getReturnType().equals(Void.TYPE)) {
                if (method.getParameterCount() == 0) {
                    if (method.getName().startsWith("get")) {
                        FragmentFieldMetadata fragmentFieldMetadata = new FragmentFieldMetadata();
                        fragmentFieldMetadata.setName(method.getName().replaceFirst("get", ""));
                        fragmentFieldMetadata.setGetter(method);
                        fragmentMetadata.getFields().addLast(fragmentFieldMetadata);
                    }
                }
            }
        }

        return fragmentMetadata;
    }

    @Override
    public @Optional Long getIdentity(@Mandatory Object object) {
        checkCompatibility(object.getClass());
        return ((CObject)object).getAddress();
    }

    private void checkCompatibility(@Mandatory Class clazz) {
        if (!isCompatible(clazz)) {
            throw new IllegalArgumentException("Incompatible class " + clazz.getSimpleName() + ".");
        }
    }
}
