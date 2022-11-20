package cz.mg.crisp.entity.metadata;

import cz.mg.annotations.classes.Utility;
import cz.mg.annotations.requirement.Mandatory;
import cz.mg.annotations.requirement.Optional;

public @Utility interface FieldReader {
    @Optional Object read(@Mandatory Object object);
}
