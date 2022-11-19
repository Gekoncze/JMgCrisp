package cz.mg.crisp.services;

import cz.mg.annotations.classes.Service;
import cz.mg.annotations.requirement.Mandatory;
import cz.mg.annotations.requirement.Optional;
import cz.mg.collections.list.List;
import cz.mg.crisp.entity.Fragment;
import cz.mg.crisp.entity.metadata.FieldMetadata;
import cz.mg.crisp.entity.metadata.ClassMetadata;
import cz.mg.crisp.entity.metadata.Metadata;

public @Service class FragmentDataReader {
    private static @Optional FragmentDataReader instance;

    public static @Mandatory FragmentDataReader getInstance() {
        if (instance == null) {
            instance = new FragmentDataReader();
        }
        return instance;
    }

    private FragmentDataReader() {
    }

    public void update(@Mandatory Fragment fragment, @Mandatory Metadata metadata) {
        Object object = fragment.getObject();

        ClassMetadata classMetadata = metadata.get(object);
        String name = classMetadata.getName();
        String identity = getObjectIdentity(metadata, object);
        String header = name + " " + identity;
        fragment.setHeader(header);

        fragment.setRows(new List<>());
        for (FieldMetadata fieldMetadata : classMetadata.getFields()) {
            String stringValue = valueToString(metadata, fieldMetadata.getValue(object));
            String row = fieldMetadata.getName() + ": " + stringValue;
            fragment.getRows().addLast(row);
        }
    }

    private @Mandatory String getObjectIdentity(@Mandatory Metadata metadata, @Mandatory Object object) {
        if (metadata.getMetadataFactory().isCompatible(object.getClass())) {
            Long identity = metadata.getMetadataFactory().getIdentity(object);
            if (identity != null) {
                return identity.toString();
            } else {
                return Long.toHexString(System.identityHashCode(object));
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
                return getObjectIdentity(metadata, object);
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
