package io.github.fourlastor.harlequin.ui;

import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import io.github.fourlastor.harlequin.animation.Animation;

/** Actors which will play an {@link Animation} and update itself over time. */
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

    /** Updates the {@link Animation} to play and resets the progress to 0. */
    public void setAnimation(Animation<? extends Drawable> animation) {
        this.animation = animation;
        setProgress(0f);
    }

    /** Returns true if the animation has finished. Looping animation never finish */
    public boolean animationFinished() {
        return animation.isAnimationFinished(playTime);
    }

    /** Updates the animation {@link Animation.PlayMode}. */
    public void setPlayMode(Animation.PlayMode playMode) {
        animation.setPlayMode(playMode);
    }

    /** Updates the animation progress. */
    public void setProgress(float progress) {
        this.playTime = progress;
        setDrawable(frameAt(progress));
    }

    /** Returns true if the animation is currently playing. */
    public boolean isPlaying() {
        return playing;
    }

    /** Updates the playing status. */
    public void setPlaying(boolean playing) {
        this.playing = playing;
    }
}
