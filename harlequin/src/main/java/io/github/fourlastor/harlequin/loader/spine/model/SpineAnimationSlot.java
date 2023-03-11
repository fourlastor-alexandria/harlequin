package io.github.fourlastor.harlequin.loader.spine.model;

import com.badlogic.gdx.utils.JsonValue;
import io.github.fourlastor.harlequin.animation.KeyFrame;
import io.github.fourlastor.harlequin.json.JsonParser;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;

public class SpineAnimationSlot {

    public final List<KeyFrame<String>> keyFrames;

    public SpineAnimationSlot(List<KeyFrame<String>> keyFrames) {
        this.keyFrames = keyFrames;
    }

    public static class Parser extends JsonParser<SpineAnimationSlot> {

        @Inject
        public Parser() {}

        @Override
        public SpineAnimationSlot parse(JsonValue value) {
            ArrayList<KeyFrame<String>> keyFrames = new ArrayList<>(value.size);
            for (JsonValue it : value.get("attachment")) {
                keyFrames.add(KeyFrame.create((int) (it.getFloat("time") * 1000), it.getString("name")));
            }
            return new SpineAnimationSlot(keyFrames);
        }
    }
}
