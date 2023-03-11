package io.github.fourlastor.harlequin.animation;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.function.ToIntFunction;

public class KeyFrameAnimation<T> implements Animation<T> {

    private static final float SECONDS_TO_MILLIS = 1000f;
    private static final Comparator<KeyFrame<?>> COMPARATOR = Comparator.comparingInt(new KeyExtractor());

    private final List<KeyFrame<T>> values;
    private final float duration;
    private final int durationMs;

    private PlayMode playMode;
    private int lastFrameIndex = -1;

    public KeyFrameAnimation(List<KeyFrame<T>> values, float duration, PlayMode playMode) {
        this.values = values;
        this.duration = duration;
        this.durationMs = toMillis(duration);
        this.playMode = playMode;
    }

    private final SearchFrame<T> searchFrame = new SearchFrame<>();

    private int findIndex(int position) {
        if (values.size() <= 1) {
            return 0;
        }
        if (lastFrameIndex >= 0) {
            KeyFrame<T> lastFrame = get(lastFrameIndex);
            if (lastFrame.start() <= position) {
                if (lastFrameIndex == values.size() - 1) {
                    return lastFrameIndex;
                }
                KeyFrame<T> nextFrame = get(lastFrameIndex + 1);
                if (nextFrame.start() >= position) {
                    return lastFrameIndex;
                }
            }
        }
        searchFrame.start = position;
        int found = Collections.binarySearch(values, searchFrame, COMPARATOR);
        int unwrapped = unwrapInsertionPoint(found);
        int frame = Math.max(unwrapped, 0);
        try {
            return frame;
        } finally {
            lastFrameIndex = frame;
        }
    }

    private int position(float stateTime) {
        int currentMillis = toMillis(stateTime);
        switch (playMode) {
            case REVERSED:
                return reversed(currentMillis, durationMs);
            case LOOP:
                return loop(currentMillis, durationMs);
            case LOOP_REVERSED:
                return loopReversed(currentMillis, durationMs);
            case LOOP_PING_PONG:
                return loopPingPong(currentMillis, durationMs);
            case NORMAL:
            default:
                return normal(currentMillis, durationMs);
        }
    }

    private int normal(int current, int duration) {
        return Math.min(current, duration);
    }

    private int reversed(int current, int duration) {
        return duration - normal(current, duration);
    }

    private int loop(int current, int duration) {
        return current % duration;
    }

    private int loopReversed(int current, int duration) {
        return duration - loop(current, duration);
    }

    private int loopPingPong(int current, int duration) {
        int cycle = current / duration;
        if (cycle % 2 == 0) {
            return loop(current, duration);
        } else {
            return loopReversed(current, duration);
        }
    }

    private int toMillis(float timeInSeconds) {
        return (int) (timeInSeconds * SECONDS_TO_MILLIS);
    }

    private KeyFrame<T> get(int index) {
        return values.get(index % values.size());
    }

    /**
     * Transforms the result of [{@link Collections#binarySearch} in case the element isn't found.
     *
     * @return [result] if an element was found (result >= 0), the first index before the "insertion point" otherwise.
     */
    private int unwrapInsertionPoint(int result) {
        return result < 0 ? -result - 2 : result;
    }

    @Override
    public T getKeyFrame(float stateTime) {
        int index = findIndex(position(stateTime));
        return get(index).value();
    }

    @Override
    public int getKeyFrameIndex(float stateTime) {
        return findIndex(position(stateTime));
    }

    @Override
    public PlayMode getPlayMode() {
        return playMode;
    }

    @Override
    public void setPlayMode(PlayMode playMode) {
        this.playMode = playMode;
    }

    @Override
    public boolean isAnimationFinished(float stateTime) {
        return stateTime >= duration;
    }

    @Override
    public float getAnimationDuration() {
        return duration;
    }

    private static class KeyExtractor implements ToIntFunction<KeyFrame<?>> {

        @Override
        public int applyAsInt(KeyFrame value) {
            return value.start();
        }
    }

    private static class SearchFrame<T> extends KeyFrame<T> {

        public int start;

        @Override
        public int start() {
            return start;
        }

        @Override
        public T value() {
            throw new UnsupportedOperationException("This keyframe should be used only for start()");
        }
    }
}
