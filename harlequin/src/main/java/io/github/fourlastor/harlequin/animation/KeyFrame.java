package io.github.fourlastor.harlequin.animation;

/** A keyframe for a {@link KeyFrameAnimation}. */
public abstract class KeyFrame<T> {

    /** Position of the keyframe in the track. */
    abstract int start();

    /** Value assigned to this keyframe. */
    abstract T value();

    /** Returns an implementation of {@link KeyFrame}. */
    public static <T> KeyFrame<T> create(int start, T value) {
        return new KeyFrameImpl<>(start, value);
    }

    private static class KeyFrameImpl<T> extends KeyFrame<T> {
        private final int start;
        private final T value;

        KeyFrameImpl(int start, T value) {
            this.start = start;
            this.value = value;
        }

        @Override
        public int start() {
            return start;
        }

        @Override
        public T value() {
            return value;
        }
    }
}
