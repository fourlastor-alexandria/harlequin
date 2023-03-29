package io.github.fourlastor.harlequin.loader.dragonbones;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.AsynchronousAssetLoader;
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

public class DragonBonesLoader extends AsynchronousAssetLoader<AnimationNode.Group, DragonBonesLoader.Parameters> {
    private final JsonReader json;
    private final JsonParser<DragonBonesEntity> jsonParser;

    private final String atlasPath;
    private final String basePath;
    private DragonBonesEntity data;

    public DragonBonesLoader(Options options, JsonReader json) {
        super(new InternalFileHandleResolver());
        this.json = json;
        this.jsonParser = new DragonBonesEntity.Parser();
        this.atlasPath = options.atlasPath;
        this.basePath = options.basePath;
    }

    @Override
    public void loadAsync(AssetManager manager, String fileName, FileHandle file, Parameters parameters) {
        data = jsonParser.parse(json.parse(file));
    }

    @Override
    public AnimationNode.Group loadSync(AssetManager manager, String fileName, FileHandle file, Parameters parameters) {
        TextureAtlas atlas = manager.get(atlasPath, TextureAtlas.class);
        String texture = file.parent().child("texture").path();
        String base = texture.substring(basePath.length() + 1);
        Map<String, Animation.PlayMode> playModes;
        if (parameters == null) {
            playModes = Collections.emptyMap();
        } else {
            playModes = parameters.playModes;
        }
        try {
            return new DragonBonesAnimationsParser(atlas, base, playModes).parse(data);
        } finally {
            data = null;
        }
    }

    @SuppressWarnings({"rawtypes", "unchecked"}) // overridden method
    @Override
    public Array<AssetDescriptor> getDependencies(String fileName, FileHandle file, Parameters parameters) {
        Array<AssetDescriptor> descriptors = new Array<>();
        descriptors.add(new AssetDescriptor(atlasPath, TextureAtlas.class));
        return descriptors;
    }

    public static class Options {
        public final String atlasPath;
        public final String basePath;

        public Options(String atlasPath, String basePath) {
            this.atlasPath = atlasPath;
            this.basePath = basePath;
        }
    }

    public static class Parameters extends AssetLoaderParameters<AnimationNode.Group> {
        public final Map<String, Animation.PlayMode> playModes;

        public Parameters(Map<String, Animation.PlayMode> playModes) {
            this.playModes = playModes;
        }
    }
}
