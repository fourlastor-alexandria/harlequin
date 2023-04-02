package io.github.fourlastor.harlequin.loader.spine;

import com.badlogic.gdx.utils.JsonReader;
import io.github.fourlastor.harlequin.json.JsonLoader;
import io.github.fourlastor.harlequin.loader.spine.model.SpineEntity;

public class SpineLoader extends JsonLoader<SpineEntity> {
    public SpineLoader() {
        this(new JsonReader());
    }

    public SpineLoader(JsonReader json) {
        super(json, new SpineEntity.Parser());
    }
}
