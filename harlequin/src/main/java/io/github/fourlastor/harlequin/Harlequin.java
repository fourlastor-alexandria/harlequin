package io.github.fourlastor.harlequin;

import java.util.ArrayList;
import java.util.List;

public class Harlequin {

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

    public static ListCreator LIST_CREATOR = DEFAULT;

    public interface ListCreator {
        <T> List<T> newList();

        <T> List<T> newList(int size);
    }
}
