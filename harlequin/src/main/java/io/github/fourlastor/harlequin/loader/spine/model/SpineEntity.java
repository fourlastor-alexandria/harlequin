package io.github.fourlastor.harlequin.loader.spine.model;

import com.badlogic.gdx.utils.JsonValue;
import io.github.fourlastor.harlequin.json.JsonParser;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;

public class SpineEntity {

    public final SpineSkeleton skeleton;

    public final List<SpineBone> bones;
    public final List<SpineSlot> slots;

    public final Map<String, SpineSkins> skins;

    public final Map<String, SpineAnimation> animations;

    public SpineEntity(
            SpineSkeleton skeleton,
            List<SpineBone> bones,
            List<SpineSlot> slots,
            Map<String, SpineSkins> skins,
            Map<String, SpineAnimation> animations) {
        this.skeleton = skeleton;
        this.bones = bones;
        this.slots = slots;
        this.skins = skins;
        this.animations = animations;
    }

    @Override
    public String toString() {
        return "Animation{" + "bones=" + bones + ", slots=" + slots + ", skins=" + skins + '}';
    }

    public static class Parser extends JsonParser<SpineEntity> {

        private final JsonParser<SpineSkeleton> skeletonParser;
        private final JsonParser<SpineBone> boneParser;

        private final JsonParser<SpineSlot> slotParser;
        private final JsonParser<SpineSkins> skinsParser;
        private final JsonParser<SpineAnimation> animationParser;

        @Inject
        public Parser(
                JsonParser<SpineSkeleton> skeletonParser,
                JsonParser<SpineBone> boneParser,
                JsonParser<SpineSlot> slotParser,
                JsonParser<SpineSkins> skinsParser,
                JsonParser<SpineAnimation> animationParser) {
            this.skeletonParser = skeletonParser;
            this.boneParser = boneParser;
            this.slotParser = slotParser;
            this.skinsParser = skinsParser;
            this.animationParser = animationParser;
        }

        @Override
        public SpineEntity parse(JsonValue value) {
            return new SpineEntity(
                    skeletonParser.parse(value.get("skeleton")),
                    getList(value.get("bones"), boneParser::parse),
                    getList(value.get("slots"), slotParser::parse),
                    getMap(value.get("skins").get("default"), skinsParser::parse),
                    getMap(value.get("animations"), animationParser::parse));
        }
    }
}
