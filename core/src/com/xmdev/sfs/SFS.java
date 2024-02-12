package com.xmdev.sfs;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.xmdev.sfs.resources.Assets;
import com.xmdev.sfs.screens.GameScreen;

public class SFS extends Game {
	public SpriteBatch batch;
	public Assets assets;

	// screens
	public GameScreen gameScreen;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		assets = new Assets();

		// Load all assets
		assets.load();
		assets.manager.finishLoading();

		// initialize game screen and switch to it
		gameScreen = new GameScreen(this);
		setScreen(gameScreen);
	}

	@Override
	public void render () {
		super.render();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		assets.dispose();
	}
}
