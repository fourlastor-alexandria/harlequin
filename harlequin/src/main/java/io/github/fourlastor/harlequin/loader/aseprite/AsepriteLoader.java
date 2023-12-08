package io.github.fourlastor.harlequin.loader.aseprite;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.AsynchronousAssetLoader;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import io.github.fourlastor.harlequin.animation.Animation;
import io.github.fourlastor.harlequin.animation.KeyFrame;
import io.github.fourlastor.harlequin.animation.KeyFrameAnimation;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AsepriteLoader extends AsynchronousAssetLoader<Animation<TextureRegion>, AsepriteLoader.Parameters> {

    private final JsonReader json;
    private JsonValue data;

    public AsepriteLoader() {
        this(new InternalFileHandleResolver(), new JsonReader());
    }

    public AsepriteLoader(FileHandleResolver resolver, JsonReader json) {
        super(resolver);
        this.json = json;
    }

    @Override
    public void loadAsync(AssetManager manager, String fileName, FileHandle file, Parameters parameter) {
        data = json.parse(file);
    }

    @Override
    public Animation<TextureRegion> loadSync(
            AssetManager manager, String fileName, FileHandle file, Parameters parameters) {
        TextureRegion allFrames;
        Animation.PlayMode playMode;
        if (parameters != null) {
            TextureAtlas atlas = manager.get(parameters.atlas, TextureAtlas.class);
            allFrames = atlas.findRegion(parameters.basePath + file.nameWithoutExtension());
            playMode = parameters.playMode;
        } else {
            allFrames = new TextureRegion(manager.get(singleFileName(file), Texture.class));
            playMode = Animation.PlayMode.NORMAL;
        }
        try {
            JsonValue frames = Objects.requireNonNull(data.get("frames"), "Frames missing in aseprite animation");
            List<KeyFrame<TextureRegion>> keyFrames = new ArrayList<>(frames.size);
            int startTime = 0;
            for (JsonValue key : frames) {
                JsonValue frame = Objects.requireNonNull(key.get("frame"), "Frame data is missing");
                int x = frame.getInt("x");
                int y = frame.getInt("y");
                int w = frame.getInt("w");
                int h = frame.getInt("h");
                int duration = key.getInt("duration");

                keyFrames.add(KeyFrame.create(startTime, new TextureRegion(allFrames, x, y, w, h)));
                startTime += duration;
            }
            return new KeyFrameAnimation<>(keyFrames, startTime / 1000f, playMode);
        } finally {
            data = null;
        }
    }

    @SuppressWarnings({"rawtypes", "unchecked"}) // overridden method
    @Override
    public Array<AssetDescriptor> getDependencies(String fileName, FileHandle file, Parameters parameters) {
        Array<AssetDescriptor> descriptors = new Array<>();
        if (parameters != null) {
            descriptors.add(new AssetDescriptor(parameters.atlas, TextureAtlas.class));
        } else {
            descriptors.add(new AssetDescriptor(singleFileName(file), Texture.class));
        }
        return descriptors;
    }

    private static String singleFileName(FileHandle file) {
        return file.nameWithoutExtension() + ".png";
    }

    public static class Parameters extends AssetLoaderParameters<Animation<TextureRegion>> {
        public final String atlas;
        public final String basePath;
        public final Animation.PlayMode playMode;

        public Parameters(String atlas) {
            this(atlas, "", Animation.PlayMode.NORMAL);
        }

        public Parameters(String atlas, String basePath, Animation.PlayMode playMode) {
            this.atlas = atlas;
            this.basePath = basePath;
            this.playMode = playMode;
        }
    }
}
