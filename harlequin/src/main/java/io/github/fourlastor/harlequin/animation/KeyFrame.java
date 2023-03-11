package io.github.fourlastor.harlequin.animation;

public abstract class KeyFrame<T> {
    abstract int start();

    abstract T value();

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
