package com.xmdev.sfs;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.xmdev.sfs.objects.Fighter;
import com.xmdev.sfs.resources.Assets;
import com.xmdev.sfs.screens.GameScreen;

public class SFS extends Game {
	public SpriteBatch batch;
	public Assets assets;

	// screens
	public GameScreen gameScreen;

	// fighters
	public Fighter player, opponent;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		assets = new Assets();

		// Load all assets
		assets.load();
		assets.manager.finishLoading();

		// initialize the fighters
		player = new Fighter(this, "Slim Stallone", new Color(1f, 0.2f, 0.2f, 1f));
		opponent = new Fighter(this, "Fat Stallone", new Color(0.25f, 0.7f, 1f, 1f));

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
