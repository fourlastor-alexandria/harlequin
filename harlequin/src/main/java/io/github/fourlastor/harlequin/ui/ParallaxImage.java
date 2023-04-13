package io.github.fourlastor.harlequin.ui;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import java.util.Objects;

/**
 * An {@link Actor} which draws the image according to the parallax factor.
 * A factor of 1 means there is no parallax.
 * Smaller factors make the image move slower.
 */
public class ParallaxImage extends Actor {

    private final TextureRegion texture;
    private final float factor;

    private final Vector2 currentDelta = new Vector2();

    public ParallaxImage(TextureRegion texture, float factor) {
        super();
        setBounds(0f, 0f, texture.getRegionWidth(), texture.getRegionHeight());
        setPosition(0f, 0f);
        this.factor = factor;
        this.texture = texture;
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
        for (float x = startX + dX - targetWidth; x <= maxX; x += targetWidth) {
            for (float y = startY + dY - targetHeight; y <= maxY; y += targetHeight) {
                batch.draw(texture, x, y, targetWidth, targetHeight);
            }
        }
    }
}
