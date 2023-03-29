package io.github.fourlastor.harlequin.loader.spine.model;

import com.badlogic.gdx.utils.JsonValue;
import io.github.fourlastor.harlequin.json.JsonParser;
import java.util.Map;

public class SpineAnimation {
    public final String name;
    public final Map<String, SpineAnimationSlot> slots;

    public SpineAnimation(String name, Map<String, SpineAnimationSlot> slots) {
        this.name = name;
        this.slots = slots;
    }

    public static class Parser extends JsonParser<SpineAnimation> {

        private final JsonParser<SpineAnimationSlot> animatedSlotParser;

        public Parser() {
            this.animatedSlotParser = new SpineAnimationSlot.Parser();
        }

        @Override
        public SpineAnimation parse(JsonValue value) {
            return new SpineAnimation(value.name, getMap(value.get("slots"), animatedSlotParser::parse));
        }
    }
}
