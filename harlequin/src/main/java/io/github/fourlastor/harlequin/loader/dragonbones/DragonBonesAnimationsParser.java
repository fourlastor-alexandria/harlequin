package io.github.fourlastor.harlequin.loader.dragonbones;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.FloatArray;
import io.github.fourlastor.harlequin.animation.Animation;
import io.github.fourlastor.harlequin.animation.AnimationNode;
import io.github.fourlastor.harlequin.animation.KeyFrame;
import io.github.fourlastor.harlequin.animation.KeyFrameAnimation;
import io.github.fourlastor.harlequin.loader.dragonbones.model.DragonBonesAnimation;
import io.github.fourlastor.harlequin.loader.dragonbones.model.DragonBonesAnimationSlot;
import io.github.fourlastor.harlequin.loader.dragonbones.model.DragonBonesArmature;
import io.github.fourlastor.harlequin.loader.dragonbones.model.DragonBonesArmatureSlot;
import io.github.fourlastor.harlequin.loader.dragonbones.model.DragonBonesBone;
import io.github.fourlastor.harlequin.loader.dragonbones.model.DragonBonesDisplay;
import io.github.fourlastor.harlequin.loader.dragonbones.model.DragonBonesDisplayFrame;
import io.github.fourlastor.harlequin.loader.dragonbones.model.DragonBonesEntity;
import io.github.fourlastor.harlequin.loader.dragonbones.model.DragonBonesSkin;
import io.github.fourlastor.harlequin.loader.dragonbones.model.DragonBonesSkinSlot;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DragonBonesAnimationsParser {

    private final TextureAtlas atlas;
    private final String basePath;
    private final Map<String, Animation.PlayMode> playModes;

    public DragonBonesAnimationsParser(TextureAtlas atlas, String basePath, Map<String, Animation.PlayMode> playModes) {
        this.atlas = atlas;
        this.basePath = basePath;
        this.playModes = playModes;
    }

    public AnimationNode.Group parse(DragonBonesEntity entity) {
        DragonBonesArmature armature = entity.armatures.get(0);
        DragonBonesBone root = armature.root();
        return tree(armature, root);
    }

    private AnimationNode.Group tree(DragonBonesArmature armature, DragonBonesBone root) {
        List<DragonBonesBone> bones = armature.childrenBones(root.name);
        List<AnimationNode> boneNodes = new ArrayList<>(bones.size());
        for (int i = 0; i < bones.size(); i++) {
            DragonBonesBone bone = bones.get(i);
            boneNodes.add(tree(armature, bone));
        }

        List<DragonBonesArmatureSlot> slots = armature.childrenSlots(root.name);
        List<AnimationNode> slotNodes = new ArrayList<>(bones.size());
        for (int i = 0; i < slots.size(); i++) {
            DragonBonesArmatureSlot slot = slots.get(i);
            slotNodes.add(node(armature, slot));
        }
        List<AnimationNode> nodes = new ArrayList<>(boneNodes.size() + slotNodes.size());
        nodes.addAll(boneNodes);
        nodes.addAll(slotNodes);

        return new AnimationNode.Group(root.name, root.transform.x, -root.transform.y, nodes);
    }

    private AnimationNode node(DragonBonesArmature armature, DragonBonesArmatureSlot slot) {
        DragonBonesSkin skin = armature.skin();
        DragonBonesSkinSlot skinSlot = skin.slot(slot.name);
        DragonBonesDisplay defaultDisplay = skinSlot.displays.get(slot.displayIndex);
        if (defaultDisplay instanceof DragonBonesDisplay.Image) {
            return image(armature, slot, (DragonBonesDisplay.Image) defaultDisplay, skinSlot);
        } else if (defaultDisplay instanceof DragonBonesDisplay.BoundingBox) {
            return boundingBox(slot, (DragonBonesDisplay.BoundingBox) defaultDisplay);
        }
        throw new RuntimeException("Display type not supported: " + defaultDisplay);
    }

    private AnimationNode.BoundingBox boundingBox(
            DragonBonesArmatureSlot slot, DragonBonesDisplay.BoundingBox defaultDisplay) {
        return new AnimationNode.BoundingBox(slot.name, defaultBoundingBox(defaultDisplay.vertices));
    }

    private AnimationNode.Image image(
            DragonBonesArmature armature,
            DragonBonesArmatureSlot slot,
            DragonBonesDisplay.Image defaultDisplay,
            DragonBonesSkinSlot skinSlot) {
        Map<String, Animation<Drawable>> animations = new HashMap<>(armature.animations.size());
        Animation<Drawable> defaultImage = defaultImage(defaultDisplay.name);
        for (DragonBonesAnimation animation : armature.animations) {
            DragonBonesAnimationSlot animationSlot = animation.slot(slot.name);
            if (animationSlot == null) {
                animations.put(animation.name, defaultImage);
            } else {
                ArrayList<KeyFrame<Drawable>> frames = new ArrayList<>(animationSlot.displayFrames.size());
                int durationMs = 0;
                for (DragonBonesDisplayFrame displayFrame : animationSlot.displayFrames) {
                    frames.add(KeyFrame.create(durationMs, drawable(skinSlot.displays.get(displayFrame.value).name)));
                    durationMs += displayFrame.duration * 1000 / 60;
                }

                if (durationMs == 0) {
                    durationMs = 1;
                }

                Animation.PlayMode playMode = playModes.getOrDefault(animation.name, Animation.PlayMode.LOOP);
                animations.put(animation.name, new KeyFrameAnimation<>(frames, durationMs / 1000f, playMode));
            }
        }
        return new AnimationNode.Image(slot.name, defaultImage, animations);
    }

    private Animation<Drawable> defaultImage(String name) {
        TextureRegionDrawable drawable = drawable(name);
        List<KeyFrame<Drawable>> frames = Arrays.asList(KeyFrame.create(0, drawable));
        return new KeyFrameAnimation<>(frames, 0.1f, Animation.PlayMode.NORMAL);
    }

    private TextureRegionDrawable drawable(String name) {
        TextureRegion region = atlas.findRegion(basePath + "/" + name);
        return new TextureRegionDrawable(region);
    }

    private Animation<Rectangle> defaultBoundingBox(FloatArray vertices) {
        List<KeyFrame<Rectangle>> frames = vertices(vertices);
        return new KeyFrameAnimation<>(frames, 0.1f, Animation.PlayMode.NORMAL);
    }

    private List<KeyFrame<Rectangle>> vertices(FloatArray vertices) {
        float minX = Float.MAX_VALUE;
        float minY = Float.MAX_VALUE;
        float maxX = Float.MIN_VALUE;
        float maxY = Float.MIN_VALUE;
        for (int i = 0; i < vertices.size; i += 2) {
            float x = vertices.get(i);
            float y = vertices.get(i + 1);
            minX = Math.min(minX, x);
            maxX = Math.max(maxX, x);
            minY = Math.min(minY, y);
            maxY = Math.max(maxY, y);
        }
        float halfWidth = (maxX - minX) / 2f;
        float halfHeight = (maxY - minY) / 2f;
        float centerX = minX + halfWidth;
        float centerY = minY + halfHeight;
        return Arrays.asList(KeyFrame.create(0, new Rectangle(centerX, centerY, halfWidth, halfHeight)));
    }
}
