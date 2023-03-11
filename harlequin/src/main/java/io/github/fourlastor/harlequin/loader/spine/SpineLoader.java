package io.github.fourlastor.harlequin.loader.spine;

import com.badlogic.gdx.utils.JsonReader;
import io.github.fourlastor.harlequin.json.JsonLoader;
import io.github.fourlastor.harlequin.json.JsonParser;
import io.github.fourlastor.harlequin.loader.spine.model.SpineEntity;
import javax.inject.Inject;

public class SpineLoader extends JsonLoader<SpineEntity> {
    @Inject
    public SpineLoader(JsonReader json, JsonParser<SpineEntity> parser) {
        super(json, parser);
    }
}
