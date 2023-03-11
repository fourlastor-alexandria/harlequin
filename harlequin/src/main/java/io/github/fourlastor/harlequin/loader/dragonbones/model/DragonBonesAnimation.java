package io.github.fourlastor.harlequin.loader.dragonbones.model;

import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.Null;
import io.github.fourlastor.harlequin.json.JsonParser;
import java.util.List;
import javax.inject.Inject;

public class DragonBonesAnimation {

    public final int duration;
    public final String name;
    /** List of slots with keyframes in this animation. */
    public final List<DragonBonesAnimationSlot> slots;

    public DragonBonesAnimation(int duration, String name, List<DragonBonesAnimationSlot> slots) {
        this.duration = duration;
        this.name = name;
        this.slots = slots;
    }

    @Null
    public DragonBonesAnimationSlot slot(String name) {
        for (int i = 0; i < slots.size(); i++) {
            DragonBonesAnimationSlot slot = slots.get(i);
            if (name.equals(slot.name)) {
                return slot;
            }
        }
        return null;
    }

    public static class Parser extends JsonParser<DragonBonesAnimation> {

        private final JsonParser<DragonBonesAnimationSlot> animationSlotParser;

        @Inject
        public Parser(JsonParser<DragonBonesAnimationSlot> animationSlotParser) {
            this.animationSlotParser = animationSlotParser;
        }

        @Override
        public DragonBonesAnimation parse(JsonValue value) {
            return new DragonBonesAnimation(
                    value.getInt("duration"),
                    value.getString("name"),
                    getList(value.get("slot"), animationSlotParser::parse));
        }
    }
}
