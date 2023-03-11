package io.github.fourlastor.harlequin.loader.dragonbones.model;

import com.badlogic.gdx.utils.JsonValue;
import io.github.fourlastor.harlequin.json.JsonParser;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;

public class DragonBonesArmature {

    public final int frameRate;
    public final String name;
    /** These should be used to determine initial position (by using groups?) */
    public final List<DragonBonesBone> bones;
    /** Contains the various slots, and their default value. */
    public final List<DragonBonesArmatureSlot> slots;
    /**
     * Contains possible values for [slots]
     * In practice, there is only one skin.
     */
    public final List<DragonBonesSkin> skins;
    /** Contains keyframes for the animations. */
    public final List<DragonBonesAnimation> animations;

    public DragonBonesArmature(
            int frameRate,
            String name,
            List<DragonBonesBone> bones,
            List<DragonBonesArmatureSlot> slots,
            List<DragonBonesSkin> skins,
            List<DragonBonesAnimation> animations) {
        this.frameRate = frameRate;
        this.name = name;
        this.bones = bones;
        this.slots = slots;
        this.skins = skins;
        this.animations = animations;
    }

    public DragonBonesSkin skin() {
        return skins.get(0);
    }

    public DragonBonesBone root() {
        for (int i = 0; i < bones.size(); i++) {
            DragonBonesBone bone = bones.get(i);
            if (bone.parent == null) {
                return bone;
            }
        }
        throw new RuntimeException("Root not found");
    }

    public List<DragonBonesBone> childrenBones(String name) {
        ArrayList<DragonBonesBone> bones = new ArrayList<>();
        for (int i = 0; i < this.bones.size(); i++) {
            DragonBonesBone bone = this.bones.get(i);
            if (name.equals(bone.parent)) {
                bones.add(bone);
            }
        }
        return bones;
    }

    public List<DragonBonesArmatureSlot> childrenSlots(String name) {
        ArrayList<DragonBonesArmatureSlot> slots = new ArrayList<>();
        for (int i = 0; i < this.slots.size(); i++) {
            DragonBonesArmatureSlot slot = this.slots.get(i);
            if (name.equals(slot.parent)) {
                slots.add(slot);
            }
        }
        return slots;
    }

    public DragonBonesBone bone(String name) {
        for (int i = 0; i < bones.size(); i++) {
            DragonBonesBone bone = bones.get(i);
            if (bone.name.equals(name)) {
                return bone;
            }
        }
        throw new RuntimeException("Bone not found " + name);
    }

    public static class Parser extends JsonParser<DragonBonesArmature> {

        private final JsonParser<DragonBonesBone> boneParser;
        private final JsonParser<DragonBonesArmatureSlot> armatureSlotParser;
        private final JsonParser<DragonBonesSkin> skinParser;
        private final JsonParser<DragonBonesAnimation> animationParser;

        @Inject
        public Parser(
                JsonParser<DragonBonesBone> boneParser,
                JsonParser<DragonBonesArmatureSlot> armatureSlotParser,
                JsonParser<DragonBonesSkin> skinParser,
                JsonParser<DragonBonesAnimation> animationParser) {
            this.boneParser = boneParser;
            this.armatureSlotParser = armatureSlotParser;
            this.skinParser = skinParser;
            this.animationParser = animationParser;
        }

        @Override
        public DragonBonesArmature parse(JsonValue value) {
            return new DragonBonesArmature(
                    value.getInt("frameRate"),
                    value.getString("name"),
                    getList(value.get("bone"), boneParser::parse),
                    getList(value.get("slot"), armatureSlotParser::parse),
                    getList(value.get("skin"), skinParser::parse),
                    getList(value.get("animation"), animationParser::parse));
        }
    }
}
