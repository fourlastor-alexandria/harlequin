package io.github.fourlastor.harlequin.loader.dragonbones.model;

import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.Null;
import io.github.fourlastor.harlequin.json.JsonParser;

public class DragonBonesBone {

    public final String name;

    @Null
    public final String parent;

    public final DragonBonesTransform transform;

    public DragonBonesBone(String name, @Null String parent, DragonBonesTransform transform) {
        this.name = name;
        this.parent = parent;
        this.transform = transform;
    }

    public static class Parser extends JsonParser<DragonBonesBone> {

        private final JsonParser<DragonBonesTransform> transformParser;

        public Parser() {
            this.transformParser = new DragonBonesTransform.Parser();
        }

        @Override
        public DragonBonesBone parse(JsonValue value) {
            return new DragonBonesBone(
                    value.getString("name"),
                    value.getString("parent", null),
                    transformParser.parse(value.get("transform")));
        }
    }
}
