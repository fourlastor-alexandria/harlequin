package io.github.fourlastor.harlequin.ui;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.utils.SnapshotArray;
import io.github.fourlastor.harlequin.animation.AnimationNode;

public class AnimationStateMachine extends Group implements StateActor {

    private boolean sizeComputed = false;
    private boolean animating = true;

    public AnimationStateMachine(AnimationNode.Group node) {
        setPosition(node.x, node.y);
        for (AnimationNode child : node.children) {
            if (child instanceof AnimationNode.Group) {
                addActor(new AnimationStateMachine(((AnimationNode.Group) child)));
            } else if (child instanceof AnimationNode.Image) {
                addActor(new AnimationImage(((AnimationNode.Image) child)));
            }
        }
    }

    public void setAnimating(Boolean animating) {
        this.animating = animating;
    }

    @Override
    public void act(float delta) {
        if (animating) {
            super.act(delta);
        }
    }

    @Override
    public float getWidth() {
        if (!sizeComputed) {
            computeSize();
        }
        return super.getWidth();
    }

    @Override
    public float getHeight() {
        if (!sizeComputed) {
            computeSize();
        }
        return super.getHeight();
    }

    private void computeSize() {
        float minX = Float.MAX_VALUE;
        float minY = Float.MAX_VALUE;
        float maxX = Float.MIN_VALUE;
        float maxY = Float.MIN_VALUE;
        SnapshotArray<Actor> children = getChildren();
        children.begin();
        for (Actor child : children) {
            minX = Math.min(minX, child.getX());
            minY = Math.min(minY, child.getY());
            maxX = Math.max(maxX, child.getX() + child.getWidth());
            maxY = Math.max(maxY, child.getY() + child.getHeight());
        }
        children.end();
        setSize(maxX - minX, maxY - minY);
        sizeComputed = true;
    }

    @Override
    public void enter(String stateName) {
        SnapshotArray<Actor> children = getChildren();
        children.begin();
        for (Actor child : children) {
            if (child instanceof StateActor) {
                ((StateActor) child).enter(stateName);
            }
        }
        children.end();
    }

    private static class AnimationImage extends Group implements StateActor {

        private final AnimatedImage image;
        private final AnimationNode.Image node;

        public AnimationImage(AnimationNode.Image node) {
            image = new AnimatedImage(node.drawableAnimation);
            addActor(image);
            this.node = node;
        }

        @Override
        public float getWidth() {
            return image.getWidth();
        }

        @Override
        public float getHeight() {
            return image.getHeight();
        }

        @Override
        public void enter(String stateName) {
            image.setAnimation(node.animations.get(stateName));
        }
    }
}
