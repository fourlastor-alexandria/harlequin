package io.github.fourlastor.harlequin.loader.dragonbones.model;

import com.badlogic.gdx.utils.JsonValue;
import io.github.fourlastor.harlequin.json.JsonParser;
import java.util.List;

public class DragonBonesAnimationSlot {

    /** Name of the armature slot. */
    public final String name;

    /** List of keyframes for this slot. */
    public final List<DragonBonesDisplayFrame> displayFrames;

    public DragonBonesAnimationSlot(String name, List<DragonBonesDisplayFrame> displayFrames) {
        this.name = name;
        this.displayFrames = displayFrames;
    }

    public static class Parser extends JsonParser<DragonBonesAnimationSlot> {

        private final JsonParser<DragonBonesDisplayFrame> displayFrameParser;

        public Parser() {
            this.displayFrameParser = new DragonBonesDisplayFrame.Parser();
        }

        @Override
        public DragonBonesAnimationSlot parse(JsonValue value) {
            return new DragonBonesAnimationSlot(
                    value.getString("name"), getList(value.get("displayFrame"), displayFrameParser::parse));
        }
    }
}
