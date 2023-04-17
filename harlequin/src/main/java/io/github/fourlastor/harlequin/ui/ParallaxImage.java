package io.github.fourlastor.harlequin.ui;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TiledDrawable;
import java.util.Objects;

/**
 * An {@link Actor} which draws the image according to the parallax factor.
 * A factor of 1 means there is no parallax.
 * Smaller factors make the image move slower.
 */
public class ParallaxImage extends Actor {

    private final float factor;

    private final Vector2 currentDelta = new Vector2();
    private final TiledDrawable drawable;

    public ParallaxImage(TextureRegion textureRegion, float factor) {
        this(new TiledDrawable(textureRegion), factor);
    }

    public ParallaxImage(TextureRegionDrawable textureRegionDrawable, float factor) {
        this(new TiledDrawable(textureRegionDrawable), factor);
    }

    public ParallaxImage(TiledDrawable drawable, float factor) {
        super();
        setBounds(0f, 0f, drawable.getMinWidth(), drawable.getMinHeight());
        setPosition(0f, 0f);
        this.drawable = drawable;
        this.factor = factor;
    }

    @Override
    public void setScale(float scaleXY) {
        super.setScale(scaleXY);
        drawable.setScale(scaleXY);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        Camera camera = Objects.requireNonNull(getStage()).getCamera();

        currentDelta.x = -(camera.position.x * factor);
        currentDelta.y = -(camera.position.y * factor);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        Camera camera = Objects.requireNonNull(getStage()).getCamera();

        float targetWidth = getWidth() * getScaleX();
        float targetHeight = getHeight() * getScaleY();
        float startX = camera.position.x - camera.viewportWidth / 2;
        float startY = camera.position.y - camera.viewportHeight / 2;
        float maxX = camera.viewportWidth + startX;
        float maxY = camera.viewportHeight + startY;
        float dX = currentDelta.x % targetWidth;
        float dY = currentDelta.y % targetHeight;
        float x = startX + dX - targetWidth;
        float y = startY + dY - targetHeight;
        drawable.draw(batch, x, y, maxX - x, maxY - y);
    }
}
