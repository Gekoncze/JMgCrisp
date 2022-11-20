package cz.mg.crisp;

import cz.mg.annotations.requirement.Mandatory;
import cz.mg.annotations.requirement.Optional;
import cz.mg.crisp.entity.metadata.ClassMetadata;
import cz.mg.crisp.services.MetadataFactory;

@SuppressWarnings("rawtypes")
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
        throw new UnsupportedOperationException(); // TODO
    }

    @Override
    public @Mandatory ClassMetadata create(@Mandatory Class clazz) {
        throw new UnsupportedOperationException(); // TODO
    }

    @Override
    public @Optional Long getIdentity(@Mandatory Object object) {
        throw new UnsupportedOperationException(); // TODO
    }
}
