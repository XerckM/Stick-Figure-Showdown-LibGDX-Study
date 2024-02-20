package com.xmdev.sfs.screens;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.xmdev.sfs.SFS;
import com.xmdev.sfs.resources.GlobalVariables;

public class LoadingScreen implements Screen {
    private final SFS game;
    private final Viewport viewport;

    // progress bar
    private static final float PROGRESS_BAR_MAX_WIDTH = 58f;
    private static final float PROGRESS_BAR_HEIGHT = 5f;
    private static final float PROGRESS_BAR_BACKGROUND_WIDTH = PROGRESS_BAR_MAX_WIDTH + 2f;
    private static final float PROGRESS_BAR_BACKGROUND_HEIGHT = PROGRESS_BAR_HEIGHT + 2f;

    // delay
    private float delayTimer;
    private boolean delayStarted;
    private static final float DELAY_TIME = 1f;

    public LoadingScreen(SFS game) {
        this.game = game;

        // set up the viewport
        viewport = new ExtendViewport(
                GlobalVariables.WORLD_WIDTH, GlobalVariables.MIN_WORLD_HEIGHT,
                GlobalVariables.WORLD_WIDTH, 0
        );

        // initialize the delay variables
        delayTimer = DELAY_TIME;
        delayStarted = false;

        // start loading assets from the asset manager
        game.assets.load();
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(GlobalVariables.BLUE_BACKGROUND);

        if (delayStarted) {
            // if the delay has started, check if the delay timer has finished
            if (delayTimer <= 0) {
                // if the delay timer has finished, tell the game that assets have finished loading
                game.assetsLoaded();
            } else {
                // if the delay timer hasn't finished, decrement the delay timer by delta time
                delayTimer -= delta;
            }
        } else if (game.assets.manager.update()) {
            // if assets have finished loading, start the delay
            delayStarted = true;
        }

        // set the shape renderer to use the viewport's camera
        game.shapeRenderer.setProjectionMatrix(viewport.getCamera().combined);

        // draw the progress bar using the current load progress
        game.shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        game.shapeRenderer.setColor(0, 0, 0, 1);
        game.shapeRenderer.rect(
                viewport.getWorldWidth() / 2f - PROGRESS_BAR_BACKGROUND_WIDTH / 2f,
                viewport.getWorldHeight() / 2f - PROGRESS_BAR_BACKGROUND_HEIGHT / 2f,
                PROGRESS_BAR_BACKGROUND_WIDTH, PROGRESS_BAR_BACKGROUND_HEIGHT
        );
        game.shapeRenderer.setColor(GlobalVariables.GOLD);
        game.shapeRenderer.rect(
                viewport.getWorldWidth() / 2f - PROGRESS_BAR_MAX_WIDTH / 2f,
                viewport.getWorldHeight() / 2f - PROGRESS_BAR_HEIGHT / 2f,
                PROGRESS_BAR_MAX_WIDTH * game.assets.manager.getProgress(),
                PROGRESS_BAR_HEIGHT
        );
        game.shapeRenderer.end();
    }

    @Override
    public void resize(int width, int height) {
        // update the viewport with the new screen size
        viewport.update(width, height, true);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}