package io.github.fourlastor.harlequin.json;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.AsynchronousAssetLoader;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonReader;

public abstract class JsonLoader<T> extends AsynchronousAssetLoader<T, AssetLoaderParameters<T>> {
    private T data;
    private final JsonReader json;
    private final JsonParser<T> parser;

    public JsonLoader(JsonReader json, JsonParser<T> parser) {
        super(new InternalFileHandleResolver());
        this.json = json;
        this.parser = parser;
    }

    @Override
    public void loadAsync(AssetManager manager, String fileName, FileHandle file, AssetLoaderParameters<T> parameter) {
        data = parser.parse(json.parse(file));
    }

    @Override
    public T loadSync(AssetManager manager, String fileName, FileHandle file, AssetLoaderParameters<T> parameter) {
        try {
            return data;
        } finally {
            data = null;
        }
    }

    @SuppressWarnings("rawtypes")
    @Override
    public Array<AssetDescriptor> getDependencies(
            String fileName, FileHandle file, AssetLoaderParameters<T> parameter) {
        return null;
    }
}
