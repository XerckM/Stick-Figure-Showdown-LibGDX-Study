package com.xmdev.sfs.objects;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.xmdev.sfs.SFS;
import com.xmdev.sfs.resources.Assets;
import com.xmdev.sfs.resources.GlobalVariables;

public class BloodPool {
    // state
    private float stateTime;
    private boolean active;
    private float alpha;
    private final Vector2 position = new Vector2();
    private static final float FADE_TIME = 60f;

    // texture
    private final TextureRegion texture;
    private static final int TEXTURE_AMOUNT = 3;

    public BloodPool(SFS game) {
        // initialize the state
        stateTime = 0f;
        active = false;
        alpha = 1f;

        // set the texture to a random blood pool texture
        texture = getRandomBloodPoolTexture(game.assets.manager);
    }

    private TextureRegion getRandomBloodPoolTexture(AssetManager assetManager) {
        // get the blood texture atlas from the asset manager
        TextureAtlas bloodAtlas = assetManager.get(Assets.BLOOD_ATLAS);

        // return a random blood pool texture from the blood atlas
        return bloodAtlas.findRegion("BloodPool" + MathUtils.random(TEXTURE_AMOUNT - 1));
    }

    public void activate(float positionX, float positionY) {
        // activate the blood pool
        active = true;
        stateTime = 0f;
        alpha = 1f;
        position.set(positionX, positionY);
    }

    public void update(float deltaTime) {
        // if not active, don't update
        if (!active) return;

        // increment the state time by delta time
        stateTime += deltaTime;

        // reduce the alpha to make the blood pool transparent over time
        alpha = 1f - stateTime / FADE_TIME;

        // if the alpha has reached zero, deactivate the blood pool
        if (alpha <= 0) {
            alpha = 0f;
            active = false;
        }
    }

    public void render(SpriteBatch batch) {
        // if not active, don't render
        if (!active) return;

        // set the sprite batch's color using the blood pool's alpha
        batch.setColor(1, 1f, 1f, alpha);

        // draw the blood pool
        batch.draw(
                texture, position.x, position.y,
                texture.getRegionWidth() * GlobalVariables.WORLD_SCALE,
                texture.getRegionHeight() * GlobalVariables.WORLD_SCALE
        );

        // reset the sprite batch's color to fully opaque
        batch.setColor(1, 1, 1, 1);
    }
}
