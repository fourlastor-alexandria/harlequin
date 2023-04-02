package io.github.fourlastor.harlequin.loader.dragonbones;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.AsynchronousAssetLoader;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonReader;
import io.github.fourlastor.harlequin.animation.Animation;
import io.github.fourlastor.harlequin.animation.AnimationNode;
import io.github.fourlastor.harlequin.json.JsonParser;
import io.github.fourlastor.harlequin.loader.dragonbones.model.DragonBonesEntity;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;

public class DragonBonesLoader extends AsynchronousAssetLoader<AnimationNode.Group, DragonBonesLoader.Parameters> {
    private final JsonReader json;
    private final JsonParser<DragonBonesEntity> jsonParser;

    private DragonBonesEntity data;

    public DragonBonesLoader() {
        this(new InternalFileHandleResolver(), new JsonReader());
    }

    public DragonBonesLoader(InternalFileHandleResolver resolver, JsonReader json) {
        this(resolver, json, new DragonBonesEntity.Parser());
    }

    private DragonBonesLoader(FileHandleResolver resolver, JsonReader json, DragonBonesEntity.Parser jsonParser) {
        super(resolver);
        this.json = json;
        this.jsonParser = jsonParser;
    }

    @Override
    public void loadAsync(AssetManager manager, String fileName, FileHandle file, Parameters parameters) {
        data = jsonParser.parse(json.parse(file));
    }

    @Override
    public AnimationNode.Group loadSync(AssetManager manager, String fileName, FileHandle file, Parameters parameters) {
        Objects.requireNonNull(
                parameters, "You must specify at least a texture atlas in the parameters of a DragonBones animation.");
        TextureAtlas atlas = manager.get(parameters.atlas, TextureAtlas.class);
        try {
            return new DragonBonesAnimationsParser(atlas, parameters.basePath, parameters.playModes).parse(data);
        } finally {
            data = null;
        }
    }

    @SuppressWarnings({"rawtypes", "unchecked"}) // overridden method
    @Override
    public Array<AssetDescriptor> getDependencies(String fileName, FileHandle file, Parameters parameters) {
        Array<AssetDescriptor> descriptors = new Array<>();
        descriptors.add(new AssetDescriptor(parameters.atlas, TextureAtlas.class));
        return descriptors;
    }

    public static class Parameters extends AssetLoaderParameters<AnimationNode.Group> {
        public final String atlas;
        public final String basePath;
        public final Map<String, Animation.PlayMode> playModes;

        public Parameters(String atlas) {
            this(atlas, "", Collections.emptyMap());
        }

        public Parameters(String atlas, String basePath) {
            this(atlas, basePath, Collections.emptyMap());
        }

        public Parameters(String atlas, String basePath, Map<String, Animation.PlayMode> playModes) {
            this.atlas = atlas;
            this.basePath = basePath;
            this.playModes = playModes;
        }
    }
}
