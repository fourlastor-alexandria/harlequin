package io.github.fourlastor.harlequin.loader.dragonbones.model;

import com.badlogic.gdx.utils.JsonValue;
import io.github.fourlastor.harlequin.json.JsonParser;
import java.util.List;
import javax.inject.Inject;

public class DragonBonesSkinSlot {

    public final String name;
    public final List<DragonBonesDisplay> displays;

    public DragonBonesSkinSlot(String name, List<DragonBonesDisplay> displays) {
        this.name = name;
        this.displays = displays;
    }

    public static class Parser extends JsonParser<DragonBonesSkinSlot> {

        private final JsonParser<DragonBonesDisplay> displayParser;

        @Inject
        public Parser(JsonParser<DragonBonesDisplay> displayParser) {
            this.displayParser = displayParser;
        }

        @Override
        public DragonBonesSkinSlot parse(JsonValue value) {
            return new DragonBonesSkinSlot(
                    value.getString("name"), getList(value.get("display"), displayParser::parse));
        }
    }
}
