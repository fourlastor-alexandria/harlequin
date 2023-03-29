package io.github.fourlastor.harlequin.loader.dragonbones.model;

import com.badlogic.gdx.utils.JsonValue;
import io.github.fourlastor.harlequin.json.JsonParser;
import java.util.List;

public class DragonBonesSkin {

    public final List<DragonBonesSkinSlot> slots;

    public DragonBonesSkin(List<DragonBonesSkinSlot> slots) {
        this.slots = slots;
    }

    public DragonBonesSkinSlot slot(String name) {
        for (int i = 0; i < slots.size(); i++) {
            DragonBonesSkinSlot slot = slots.get(i);
            if (slot.name.equals(name)) {
                return slot;
            }
        }
        throw new RuntimeException("Slot not found " + name);
    }

    public static class Parser extends JsonParser<DragonBonesSkin> {

        private final JsonParser<DragonBonesSkinSlot> skinSlotParser;

        public Parser() {
            this.skinSlotParser = new DragonBonesSkinSlot.Parser();
        }

        @Override
        public DragonBonesSkin parse(JsonValue value) {
            return new DragonBonesSkin(getList(value.get("slot"), skinSlotParser::parse));
        }
    }
}
