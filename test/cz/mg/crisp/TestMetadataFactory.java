package cz.mg.crisp;

import cz.mg.annotations.requirement.Mandatory;
import cz.mg.annotations.requirement.Optional;
import cz.mg.crisp.entity.metadata.ClassMetadata;
import cz.mg.crisp.entity.metadata.FieldMetadata;
import cz.mg.crisp.services.MetadataFactory;

import java.lang.reflect.Method;

@SuppressWarnings({"rawtypes"})
public class TestMetadataFactory implements MetadataFactory {
    private static @Optional TestMetadataFactory instance;

    public static @Mandatory TestMetadataFactory getInstance() {
        if (instance == null) {
            instance = new TestMetadataFactory();
        }
        return instance;
    }

    private TestMetadataFactory() {
    }

    @Override
    public boolean isCompatible(@Mandatory Class clazz) {
        return TestClass.class.isAssignableFrom(clazz);
    }

    @Override
    public @Mandatory ClassMetadata create(@Mandatory Class clazz) {
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
        return (long) ((TestClass) object).getId();
    }

    @Override
    public @Optional Object open(@Mandatory Object parent, @Mandatory Object field) {
        if (!isCompatible(parent.getClass()) || !isCompatible(field.getClass())) return null;
        if (field instanceof TestPointer) {
            TestPointer pointer = (TestPointer) field;
            return pointer.getTarget();
        } else {
            return field;
        }
    }
}
