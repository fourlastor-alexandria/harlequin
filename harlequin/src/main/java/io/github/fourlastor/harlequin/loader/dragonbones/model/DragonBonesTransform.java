package io.github.fourlastor.harlequin.loader.dragonbones.model;

import com.badlogic.gdx.utils.JsonValue;
import io.github.fourlastor.harlequin.json.JsonParser;

public class DragonBonesTransform {

    public static final DragonBonesTransform NONE = new DragonBonesTransform(0, 0);

    public final int x;
    public final int y;

    public DragonBonesTransform(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public static class Parser extends JsonParser<DragonBonesTransform> {

        public Parser() {}

        @Override
        public DragonBonesTransform parse(JsonValue value) {
            if (value == null) {
                return NONE;
            }
            return new DragonBonesTransform(value.getInt("x"), value.getInt("y"));
        }
    }
}
