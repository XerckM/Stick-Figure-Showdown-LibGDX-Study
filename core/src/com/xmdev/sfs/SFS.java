package com.xmdev.sfs;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.xmdev.sfs.resources.Assets;

public class SFS extends ApplicationAdapter {
	public SpriteBatch batch;
	public Assets assets;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		assets = new Assets();

		// Load all assets
		assets.load();
		assets.manager.finishLoading();
	}

	@Override
	public void render () {
		ScreenUtils.clear(1, 0, 0, 1);
		batch.begin();
		batch.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		assets.dispose();
	}
}
