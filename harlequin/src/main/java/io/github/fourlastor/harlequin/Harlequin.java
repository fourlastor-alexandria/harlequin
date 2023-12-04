package io.github.fourlastor.harlequin;

import java.util.ArrayList;
import java.util.List;

/** Settings for harlequin. */
public class Harlequin {

    /** Default {@link ListCreator}, which uses {@link ArrayList}. */
    public static final ListCreator DEFAULT = new ListCreator() {
        @Override
        public <T> List<T> newList() {
            return new ArrayList<>();
        }

        @Override
        public <T> List<T> newList(int size) {
            return new ArrayList<>(size);
        }
    };

    /** {@link ListCreator} used when instancing new {@link List}s. */
    public static ListCreator LIST_CREATOR = DEFAULT;

    public interface ListCreator {
        <T> List<T> newList();

        <T> List<T> newList(int size);
    }
}
