package com.tds.game.other;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.assets.AssetManager;

public class MyGdxGame extends Game {
	private AssetManager assetManager;

	@Override
	public void create() {
		// Получаем информацию о дисплее
		Graphics.DisplayMode displayMode = Gdx.graphics.getDisplayMode();

		// Устанавливаем полноэкранный режим с использованием текущей разрешения дисплея
		Gdx.graphics.setFullscreenMode(displayMode);
		assetManager = new AssetManager();
		setScreen(new LoadingScreen(this, assetManager));
	}

	@Override
	public void dispose() {
		assetManager.dispose();
		super.dispose();
	}
}
