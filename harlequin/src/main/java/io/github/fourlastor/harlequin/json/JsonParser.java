package io.github.fourlastor.harlequin.json;

import com.badlogic.gdx.utils.FloatArray;
import com.badlogic.gdx.utils.IntArray;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.Null;
import io.github.fourlastor.harlequin.Harlequin;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public abstract class JsonParser<T> {
    public abstract T parse(JsonValue value);

    @Null
    protected final <V> V getOptional(JsonValue value, String name, Function<JsonValue, V> fn) {
        if (value.has(name)) {
            JsonValue jsonValue = value.get(name);
            if (jsonValue == null || jsonValue.isNull()) {
                return null;
            }
            return fn.apply(jsonValue);
        }
        return null;
    }

    @Null
    protected final Integer getOptionalInt(JsonValue value, String name) {
        return getOptional(value, name, JsonValue::asInt);
    }

    protected final IntArray getIntArray(JsonValue value) {
        IntArray values = new IntArray(false, value.size);
        for (JsonValue entry : value) {
            values.add(entry.asInt());
        }
        return values;
    }

    protected final FloatArray getFloatArray(JsonValue value) {
        FloatArray values = new FloatArray(false, value.size);
        for (JsonValue entry : value) {
            values.add(entry.asFloat());
        }
        return values;
    }

    protected final List<String> getStringList(JsonValue value) {
        return getList(value, JsonValue::asString);
    }

    protected final <V> List<V> getList(JsonValue value, Function<JsonValue, V> fn) {
        List<V> values = Harlequin.LIST_CREATOR.newList(value.size);
        for (JsonValue entry : value) {
            values.add(fn.apply(entry));
        }
        return values;
    }

    protected final <V> Map<String, V> getMap(JsonValue value, Function<JsonValue, V> fn) {
        HashMap<String, V> values = new HashMap<>();
        for (JsonValue entry : value) {
            values.put(entry.name, fn.apply(entry));
        }
        return values;
    }
}
