package cz.mg.crisp.services;

import cz.mg.annotations.classes.Service;
import cz.mg.annotations.requirement.Mandatory;
import cz.mg.annotations.requirement.Optional;
import cz.mg.collections.list.List;
import cz.mg.crisp.entity.metadata.ClassMetadata;
import cz.mg.crisp.entity.metadata.FieldMetadata;
import cz.mg.crisp.entity.metadata.Metadata;
import cz.mg.crisp.entity.model.Row;

public @Service class DataReader {
    private static @Optional DataReader instance;

    public static @Mandatory DataReader getInstance() {
        if (instance == null) {
            instance = new DataReader();
        }
        return instance;
    }

    private DataReader() {
    }

    public @Mandatory String getHeader(@Mandatory Metadata metadata, @Mandatory Object object) {
        String name = getName(metadata, object);
        String identity = getIdentity(metadata, object);
        return name + " " + identity;
    }

    public @Mandatory List<Row> getRows(@Mandatory Metadata metadata, @Mandatory Object object) {
        List<Row> rows = new List<>();
        if (metadata.getMetadataFactory().isCompatible(object.getClass())) {
            ClassMetadata classMetadata = metadata.get(object);
            for (FieldMetadata fieldMetadata : classMetadata.getFields()) {
                String name = fieldMetadata.getName();
                String value = valueToString(metadata, fieldMetadata.getValue(object));
                rows.addLast(new Row(name, value));
            }
        }
        return rows;
    }

    private @Mandatory String getName(@Mandatory Metadata metadata, @Mandatory Object object) {
        if (metadata.getMetadataFactory().isCompatible(object.getClass())) {
            return metadata.get(object).getName();
        } else {
            return object.getClass().getSimpleName();
        }
    }

    private @Mandatory String getIdentity(@Mandatory Metadata metadata, @Mandatory Object object) {
        if (metadata.getMetadataFactory().isCompatible(object.getClass())) {
            Long identity = metadata.getMetadataFactory().getIdentity(object);
            if (identity != null) {
                return identity.toString();
            } else {
                return "null";
            }
        } else {
            return Long.toHexString(System.identityHashCode(object));
        }
    }

    private @Mandatory String valueToString(@Mandatory Metadata metadata, @Optional Object object) {
        if (object != null) {
            if (isPrimitive(object.getClass())) {
                return object.toString();
            } else {
                return getIdentity(metadata, object);
            }
        } else {
            return "null";
        }
    }

    private boolean isPrimitive(@Mandatory Class<?> clazz) {
        return clazz.equals(String.class) ||
            clazz.equals(Float.class) ||
            clazz.equals(float.class) ||
            clazz.equals(Double.class) ||
            clazz.equals(double.class) ||
            clazz.equals(Integer.class) ||
            clazz.equals(int.class) ||
            clazz.equals(Short.class) ||
            clazz.equals(short.class) ||
            clazz.equals(Long.class) ||
            clazz.equals(long.class) ||
            clazz.equals(Character.class) ||
            clazz.equals(char.class) ||
            clazz.equals(Byte.class) ||
            clazz.equals(byte.class);
    }
}
