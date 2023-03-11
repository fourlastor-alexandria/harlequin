package io.github.fourlastor.harlequin.loader.spine.model;

import com.badlogic.gdx.utils.JsonValue;
import io.github.fourlastor.harlequin.json.JsonParser;
import javax.inject.Inject;

public class SpineBone {

    public final String parent;
    public final String name;
    public final float x;
    public final float y;
    public final int rotation;
    public final float scaleX;
    public final float scaleY;

    public SpineBone(String name, String parent, float x, float y, int rotation, float scaleX, float scaleY) {
        this.name = name;
        this.parent = parent;
        this.x = x;
        this.y = y;
        this.rotation = rotation;
        this.scaleX = scaleX;
        this.scaleY = scaleY;
    }

    @Override
    public String toString() {
        return "Bone{" + "parent='"
                + parent + '\'' + ", name='"
                + name + '\'' + ", x="
                + x + ", y="
                + y + ", rotation="
                + rotation + ", scaleX="
                + scaleX + ", scaleY="
                + scaleY + '}';
    }

    public static class Parser extends JsonParser<SpineBone> {

        @Inject
        public Parser() {}

        @Override
        public SpineBone parse(JsonValue value) {
            return new SpineBone(
                    value.getString("name"),
                    value.getString("parent", null),
                    value.getFloat("x", 0f),
                    value.getFloat("y", 0f),
                    value.getInt("rotation", 0),
                    value.getFloat("scaleX", 0f),
                    value.getFloat("scaleY", 0f));
        }
    }
}
