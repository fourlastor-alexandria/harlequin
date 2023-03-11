package io.github.fourlastor.harlequin.animation;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import java.util.List;
import java.util.Map;

public abstract class AnimationNode {
    public final String name;

    AnimationNode(String name) {
        this.name = name;
    }

    public static class Image extends AnimationNode {
        public final Animation<Drawable> drawableAnimation;
        public final Map<String, Animation<Drawable>> animations;

        public Image(String name, Animation<Drawable> drawableAnimation, Map<String, Animation<Drawable>> animations) {
            super(name);
            this.drawableAnimation = drawableAnimation;
            this.animations = animations;
        }
    }

    public static class BoundingBox extends AnimationNode {
        public final Animation<Rectangle> defaults;

        public BoundingBox(String name, Animation<Rectangle> defaults) {
            super(name);
            this.defaults = defaults;
        }
    }

    public static class Group extends AnimationNode {

        public final int x;
        public final int y;
        public final List<AnimationNode> children;

        public Group(String name, int x, int y, List<AnimationNode> children) {
            super(name);
            this.x = x;
            this.y = y;
            this.children = children;
        }
    }
}
