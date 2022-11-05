package cz.mg.crisp.services;

import cz.mg.annotations.classes.Service;
import cz.mg.annotations.requirement.Mandatory;
import cz.mg.annotations.requirement.Optional;
import cz.mg.crisp.other.FragmentFieldMetadata;
import cz.mg.crisp.other.FragmentMetadata;

import java.lang.reflect.Method;

@SuppressWarnings("rawtypes")
public @Service class MetadataFactory {
    private static @Optional MetadataFactory instance;

    public static @Mandatory MetadataFactory getInstance() {
        if (instance == null) {
            instance = new MetadataFactory();
        }
        return instance;
    }

    private MetadataFactory() {
    }

    public @Mandatory FragmentMetadata create(@Mandatory Class clazz) {
        FragmentMetadata fragmentMetadata = new FragmentMetadata();
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
}
