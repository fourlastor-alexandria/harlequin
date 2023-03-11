package io.github.fourlastor.harlequin.loader.dragonbones.model;

import com.badlogic.gdx.utils.JsonValue;
import io.github.fourlastor.harlequin.json.JsonParser;
import javax.inject.Inject;

public class DragonBonesDisplayFrame {

    public final int duration;
    /** Index of {@link DragonBonesDisplay} in {@link DragonBonesSkinSlot#displays} to apply during this keyframe. */
    public final int value;

    public DragonBonesDisplayFrame(int duration, int value) {
        this.duration = duration;
        this.value = value;
    }

    public static class Parser extends JsonParser<DragonBonesDisplayFrame> {
        @Inject
        public Parser() {}

        @Override
        public DragonBonesDisplayFrame parse(JsonValue value) {
            return new DragonBonesDisplayFrame(value.getInt("duration"), value.getInt("value", 0));
        }
    }
}
