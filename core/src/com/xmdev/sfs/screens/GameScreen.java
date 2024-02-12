package com.xmdev.sfs.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.ScreenUtils;
import com.xmdev.sfs.SFS;
import com.xmdev.sfs.resources.Assets;
import com.xmdev.sfs.resources.GlobalVariables;

public class GameScreen implements Screen {

    private final SFS game;
    private final OrthographicCamera camera;

    // background/ring
    private Texture backgroundTexture;
    private Texture frontRopesTexture;

    public GameScreen(SFS game) {
        this.game = game;

        // set up the camera
        camera = new OrthographicCamera(GlobalVariables.WORLD_WIDTH, GlobalVariables.WORLD_HEIGHT);
        camera.translate(camera.viewportWidth / 2f, camera.viewportHeight / 2f);
        camera.update();

        // create the game area
        createGameArea();
    }

    private void createGameArea() {
        // get ring textures from asset manager
        backgroundTexture = game.assets.manager.get(Assets.BACKGROUND_TEXTURE);
        frontRopesTexture = game.assets.manager.get(Assets.FRONT_ROPES_TEXTURE);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 1);

        // set the sprite batch to use the camera
        game.batch.setProjectionMatrix(camera.combined);

        // begin drawing
        game.batch.begin();

        // draw the background
        game.batch.draw(
                backgroundTexture, 0, 0,
                backgroundTexture.getWidth() * GlobalVariables.WORLD_SCALE,
                backgroundTexture.getHeight() * GlobalVariables.WORLD_SCALE);

        // end drawing
        game.batch.end();
    }

    @Override
    public void resize(int width, int height) {

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
