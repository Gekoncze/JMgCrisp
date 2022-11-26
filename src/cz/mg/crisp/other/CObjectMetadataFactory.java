package cz.mg.crisp.other;

import cz.mg.annotations.classes.Service;
import cz.mg.annotations.requirement.Mandatory;
import cz.mg.annotations.requirement.Optional;
import cz.mg.c.CObject;
import cz.mg.c.CPointer;
import cz.mg.crisp.entity.metadata.ClassMetadata;
import cz.mg.crisp.entity.metadata.FieldMetadata;
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
    public @Mandatory ClassMetadata create(@Mandatory Class clazz) {
        checkCompatibility(clazz);

        ClassMetadata classMetadata = new ClassMetadata();
        classMetadata.setName(clazz.getSimpleName());

        for (Method method : clazz.getDeclaredMethods()) {
            if (!method.getReturnType().equals(Void.TYPE)) {
                if (method.getParameterCount() == 0) {
                    if (method.getName().startsWith("get")) {
                        FieldMetadata fieldMetadata = new FieldMetadata();
                        fieldMetadata.setName(method.getName().replaceFirst("get", ""));
                        fieldMetadata.setFieldReader(object -> {
                            try {
                                return method.invoke(object);
                            } catch (ReflectiveOperationException e) {
                                throw new RuntimeException(e);
                            }
                        });
                        classMetadata.getFields().addLast(fieldMetadata);
                    }
                }
            }
        }

        return classMetadata;
    }

    @Override
    public @Optional Long getIdentity(@Mandatory Object object) {
        checkCompatibility(object.getClass());
        if (object instanceof CPointer) {
            CPointer pointer = (CPointer) object;
            CObject target = pointer.getTarget();
            return target != null ? target.getAddress() : null;
        } else {
            return ((CObject)object).getAddress();
        }
    }

    @Override
    public @Optional Object open(@Mandatory Object field) {
        if (field instanceof CPointer) {
            CPointer pointer = (CPointer) field;
            return pointer.getTarget();
        } else if (isCompatible(field.getClass())) {
            return field;
        } else {
            return null;
        }
    }
}
