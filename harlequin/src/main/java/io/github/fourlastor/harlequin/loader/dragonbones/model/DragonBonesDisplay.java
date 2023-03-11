package io.github.fourlastor.harlequin.loader.dragonbones.model;

import com.badlogic.gdx.utils.FloatArray;
import com.badlogic.gdx.utils.JsonValue;
import io.github.fourlastor.harlequin.json.JsonParser;
import javax.inject.Inject;

public abstract class DragonBonesDisplay {

    public final String name;

    public DragonBonesDisplay(String name) {
        this.name = name;
    }

    public static class Image extends DragonBonesDisplay {
        public Image(String name) {
            super(name);
        }
    }

    public static class BoundingBox extends DragonBonesDisplay {

        public final FloatArray vertices;

        public BoundingBox(String name, FloatArray vertices) {
            super(name);
            this.vertices = vertices;
        }
    }

    public static class Parser extends JsonParser<DragonBonesDisplay> {
        @Inject
        public Parser() {}

        @Override
        public DragonBonesDisplay parse(JsonValue value) {
            String name = value.getString("name");
            String type = value.getString("type", null);
            if (type == null) {
                return new Image(name);
            } else {
                return new BoundingBox(name, getFloatArray(value.get("vertices")));
            }
        }
    }
}
