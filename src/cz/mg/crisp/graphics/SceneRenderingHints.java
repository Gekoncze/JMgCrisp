package cz.mg.crisp.graphics;

import cz.mg.annotations.classes.Utility;

import java.awt.*;
import java.util.HashMap;

public @Utility class SceneRenderingHints extends RenderingHints {
    public SceneRenderingHints() {
        super(new HashMap<>());
        put(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        put(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
    }
}
