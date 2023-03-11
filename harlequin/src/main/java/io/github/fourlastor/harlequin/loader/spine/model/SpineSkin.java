package io.github.fourlastor.harlequin.loader.spine.model;

import com.badlogic.gdx.utils.FloatArray;
import com.badlogic.gdx.utils.JsonValue;
import io.github.fourlastor.harlequin.json.JsonParser;
import javax.inject.Inject;

public abstract class SpineSkin {
    public final String name;

    protected SpineSkin(String name) {
        this.name = name;
    }

    public static class BoundingBox extends SpineSkin {
        public final int vertexCount;
        public final FloatArray vertices;

        public BoundingBox(String name, int vertexCount, FloatArray vertices) {
            super(name);
            this.vertexCount = vertexCount;
            this.vertices = vertices;
        }
    }

    public static class Image extends SpineSkin {
        public Image(String name) {
            super(name);
        }
    }

    @Override
    public String toString() {
        return "Skin{" + "name='" + name + '\'' + '}';
    }

    public static class Parser extends JsonParser<SpineSkin> {

        @Inject
        public Parser() {}

        @Override
        public SpineSkin parse(JsonValue value) {
            if ("boundingbox".equals(value.getString("type", null))) {
                return new BoundingBox(
                        value.getString("name"), value.getInt("vertexCount", 0), getFloatArray(value.get("vertices")));
            } else {
                return new Image(value.getString("name"));
            }
        }
    }
}
