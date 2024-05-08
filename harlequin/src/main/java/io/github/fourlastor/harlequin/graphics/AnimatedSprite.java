package io.github.fourlastor.harlequin.graphics;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import io.github.fourlastor.harlequin.animation.Animation;

/** {@link Sprite} which will play an {@link Animation} and update itself over time. */
public class AnimatedSprite extends Sprite {

    private Animation<? extends TextureRegion> animation;

    private boolean playing = true;
    private float playTime = 0f;
    private float speed = 1f;

    public AnimatedSprite(Animation<? extends TextureRegion> animation) {
        super(animation.getKeyFrame(0));
        setAnimation(animation);
    }

    /** Advances this animation */
    public void advance(float delta) {
        if (!playing) {
            return;
        }
        setProgress(playTime + (delta * speed));
    }

    private TextureRegion frameAt(float position) {
        return animation.getKeyFrame(position);
    }

    /** Updates the {@link Animation} to play and resets the progress to 0. */
    public void setAnimation(Animation<? extends TextureRegion> animation) {
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
        setRegion(frameAt(progress));
    }

    /** Returns true if the animation is currently playing. */
    public boolean isPlaying() {
        return playing;
    }

    /** Updates the playing status. */
    public void setPlaying(boolean playing) {
        this.playing = playing;
    }

    /** Returns the current play speed. */
    public float getSpeed() {
        return speed;
    }

    /** Sets the current play speed. 1 represents normal playing speed. */
    public void setSpeed(float speed) {
        this.speed = speed;
    }
}
