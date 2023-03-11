package io.github.fourlastor.harlequin.ui;

import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import io.github.fourlastor.harlequin.animation.Animation;

public class AnimatedImage extends Image {

    private Animation<? extends Drawable> animation;

    public boolean playing = true;
    public float playTime = 0f;

    public AnimatedImage(Animation<? extends Drawable> animation) {
        super(animation.getKeyFrame(0));
        setAnimation(animation);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        if (!playing) {
            return;
        }
        playTime += delta;
        Drawable frame = frameAt(playTime);
        setDrawable(frame);
    }

    private Drawable frameAt(float position) {
        return animation.getKeyFrame(position);
    }

    public void setAnimation(Animation<? extends Drawable> animation) {
        this.animation = animation;
        setProgress(0f);
    }

    public boolean animationFinished() {
        return animation.isAnimationFinished(playTime);
    }

    public void setPlayMode(Animation.PlayMode playMode) {
        animation.setPlayMode(playMode);
    }

    public void setProgress(float progress) {
        this.playTime = progress;
        setDrawable(frameAt(progress));
    }

    public boolean isPlaying() {
        return playing;
    }

    public void setPlaying(boolean playing) {
        this.playing = playing;
    }
}
