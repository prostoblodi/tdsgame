package com.tds.game.level2;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class AfterBossScreen implements Screen {

    SpriteBatch batch = new SpriteBatch();
    BitmapFont font;

    AssetManager assetManager;
    Game game;
    Boolean isSwitched;

    public AfterBossScreen(Game game, AssetManager assetManager, Boolean isSwitched) {
        this.game = game;
        this.assetManager = assetManager;
        this.isSwitched = isSwitched;

        game.setScreen(this);
    }

    @Override
    public void show() {
        if(!isSwitched) {
            game.setScreen(new TextAnimator(game, assetManager));
        }
    }

    @Override
    public void render(float delta) {

    }

    @Override
    public void resize(int width, int height) {}

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}

    @Override
    public void dispose() {
        batch.dispose();
        font.dispose();
    }
}
