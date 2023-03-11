package io.github.fourlastor.harlequin.animation;

public interface Animation<T> {

    T getKeyFrame(float stateTime);

    int getKeyFrameIndex(float stateTime);

    PlayMode getPlayMode();

    void setPlayMode(PlayMode playMode);

    boolean isAnimationFinished(float stateTime);

    float getAnimationDuration();

    /**
     * Defines possible playback modes for an {@link Animation}.
     */
    enum PlayMode {
        NORMAL,
        REVERSED,
        LOOP,
        LOOP_REVERSED,
        LOOP_PING_PONG,
    }
}
