package io.github.fourlastor.harlequin.loader.dragonbones.model;

import com.badlogic.gdx.utils.JsonValue;
import io.github.fourlastor.harlequin.json.JsonParser;

public class DragonBonesArmatureSlot {

    public final String name;
    public final String parent;
    public final int displayIndex;

    public DragonBonesArmatureSlot(String name, String parent, int displayIndex) {
        this.name = name;
        this.parent = parent;
        this.displayIndex = displayIndex;
    }

    public static class Parser extends JsonParser<DragonBonesArmatureSlot> {
        public Parser() {}

        @Override
        public DragonBonesArmatureSlot parse(JsonValue value) {
            return new DragonBonesArmatureSlot(
                    value.getString("name"), value.getString("parent"), value.getInt("displayIndex", 0));
        }
    }
}
