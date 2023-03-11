package io.github.fourlastor.harlequin.loader.dragonbones.model;

import com.badlogic.gdx.utils.JsonValue;
import io.github.fourlastor.harlequin.json.JsonParser;
import java.util.List;
import javax.inject.Inject;

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

        @Inject
        public Parser(JsonParser<DragonBonesDisplayFrame> displayFrameParser) {
            this.displayFrameParser = displayFrameParser;
        }

        @Override
        public DragonBonesAnimationSlot parse(JsonValue value) {
            return new DragonBonesAnimationSlot(
                    value.getString("name"), getList(value.get("displayFrame"), displayFrameParser::parse));
        }
    }
}
