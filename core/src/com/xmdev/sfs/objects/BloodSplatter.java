package com.xmdev.sfs.objects;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.xmdev.sfs.SFS;
import com.xmdev.sfs.resources.Assets;
import com.xmdev.sfs.resources.GlobalVariables;

public class BloodSplatter {
    // state
    private float stateTime;
    private boolean active;
    private final Vector2 position = new Vector2();

    // animation
    private Animation<TextureRegion> splatterAnimation;

    public BloodSplatter(SFS game) {
        // initialize state
        stateTime = 0f;
        active = false;

        // initialize the splatter animation
        initializeSplatterAnimation(game.assets.manager);
    }

    private void initializeSplatterAnimation(AssetManager assetManager) {
        // get the blood texture atlas from the asset manager
        TextureAtlas bloodAtlas = assetManager.get(Assets.BLOOD_ATLAS);

        // create animation
        splatterAnimation = new Animation<TextureRegion>(0.03f, bloodAtlas.findRegions("BloodSplatter"));
    }

    public void activate(float positionX, float positionY) {
        // activate the blood splatter
        active = true;
        stateTime = 0f;
        position.set(positionX, positionY);
    }

    public void deactivate() {
        // deactivate the blood splatter
        active = false;
    }

    public void update(float deltaTime) {
        // if not active, don't update
        if (!active) return;

        // increment the state time by delta time
        stateTime += deltaTime;

        // if the splatter animation has finished, deactivate the blood splatter
        if (splatterAnimation.isAnimationFinished(stateTime)) {
            deactivate();
        }
    }

    public void render(SpriteBatch batch) {
        // if not active, don't render
        if (!active) return;

        // get the current frame of the splatter animation
        TextureRegion currentFrame = splatterAnimation.getKeyFrame(stateTime);

        // draw the current frame of the splatter animation
        batch.draw(
                currentFrame, position.x, position.y,
                currentFrame.getRegionWidth() * GlobalVariables.WORLD_SCALE,
                currentFrame.getRegionHeight() * GlobalVariables.WORLD_SCALE
        );
    }
}
