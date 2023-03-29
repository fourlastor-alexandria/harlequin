package io.github.fourlastor.harlequin.loader.dragonbones.model;

import com.badlogic.gdx.utils.JsonValue;
import io.github.fourlastor.harlequin.json.JsonParser;
import java.util.List;

public class DragonBonesEntity {

    public final int frameRate;
    public final String name;

    /** The actual entity. It seems to have always length 1. */
    public final List<DragonBonesArmature> armatures;

    public DragonBonesEntity(int frameRate, String name, List<DragonBonesArmature> armatures) {
        this.frameRate = frameRate;
        this.name = name;
        this.armatures = armatures;
    }

    public static class Parser extends JsonParser<DragonBonesEntity> {

        private final JsonParser<DragonBonesArmature> armatureParser;

        public Parser() {
            this.armatureParser = new DragonBonesArmature.Parser();
        }

        @Override
        public DragonBonesEntity parse(JsonValue value) {
            return new DragonBonesEntity(
                    value.getInt("frameRate"),
                    value.getString("name"),
                    getList(value.get("armature"), armatureParser::parse));
        }
    }
}
