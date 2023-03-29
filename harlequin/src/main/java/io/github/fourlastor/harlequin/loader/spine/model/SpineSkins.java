package io.github.fourlastor.harlequin.loader.spine.model;

import com.badlogic.gdx.utils.JsonValue;
import io.github.fourlastor.harlequin.json.JsonParser;
import java.util.Map;

public class SpineSkins {
    public final String name;
    public final Map<String, SpineSkin> skins;

    public SpineSkins(String name, Map<String, SpineSkin> skins) {
        this.name = name;
        this.skins = skins;
    }

    @Override
    public String toString() {
        return "Skins{" + "name='" + name + '\'' + ", skins=" + skins + '}';
    }

    public static class Parser extends JsonParser<SpineSkins> {

        private final JsonParser<SpineSkin> skinParser;

        public Parser() {
            this.skinParser = new SpineSkin.Parser();
        }

        @Override
        public SpineSkins parse(JsonValue value) {
            return new SpineSkins(value.name, getMap(value, skinParser::parse));
        }
    }
}
