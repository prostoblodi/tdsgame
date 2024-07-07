package com.tds.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class LoadingScreen implements Screen {
    private final Game game;
    private final AssetManager assetManager;
    private final Stage stage;
    private final Skin skin;
    private final Label loadingLabel;

    public LoadingScreen(Game game, AssetManager assetManager) {
        this.game = game;
        this.assetManager = assetManager;
        this.stage = new Stage(new ScreenViewport());

        this.skin = createSkin();
        this.loadingLabel = createLoadingLabel();

        Table table = new Table();
        table.setFillParent(true);
        table.add(loadingLabel).center();
        stage.addActor(table);

        loadAssets();
    }

    private Skin createSkin() {
        Skin skin = new Skin();
        skin.add("default-font", new BitmapFont());
        return skin;
    }

    private Label createLoadingLabel() {
        LabelStyle labelStyle = new LabelStyle();
        labelStyle.font = skin.getFont("default-font");
        return new Label("Loading...", labelStyle);
    }

    private void loadAssets() {
        assetManager.load("end.png", Texture.class);
        assetManager.load("gun.png", Texture.class);
        assetManager.load("AAGun.png", Texture.class);
        assetManager.load("2xGun.png", Texture.class);
        assetManager.load("road.png", Texture.class);
        assetManager.load("start.png", Texture.class);
        assetManager.load("badBoys.png", Texture.class);
        assetManager.load("airBadBoy.png", Texture.class);
        assetManager.load("blt.png", Texture.class);
        assetManager.load("AAblt.png", Texture.class);
        assetManager.load("nothing.png", Texture.class);
        assetManager.load("nothingD.png", Texture.class);
        assetManager.load("nothingO.png", Texture.class);
        assetManager.load("playGameButton.png", Texture.class);
        assetManager.load("playGameButtonD.png", Texture.class);
        assetManager.load("playGameButtonO.png", Texture.class);
        assetManager.load("settingsButton.png", Texture.class);
        assetManager.load("settingsD.png", Texture.class);
        assetManager.load("settingsO.png", Texture.class);
        assetManager.load("quit.png", Texture.class);
        assetManager.load("quitD.png", Texture.class);
        assetManager.load("quitO.png", Texture.class);
        assetManager.load("backButton.png", Texture.class);
        assetManager.load("backButtonD.png", Texture.class);
        assetManager.load("backButtonO.png", Texture.class);
        assetManager.load("fullscreen.png", Texture.class);
        assetManager.load("fullscreenD.png", Texture.class);
        assetManager.load("fullscreenO.png", Texture.class);
        assetManager.load("window.png", Texture.class);
        assetManager.load("windowD.png", Texture.class);
        assetManager.load("windowO.png", Texture.class);
        assetManager.load("redHp.png", Texture.class);
        assetManager.load("greenHp.png", Texture.class);
        assetManager.load("menuButton.png", Texture.class);
        assetManager.load("menuButtonD.png", Texture.class);
        assetManager.load("menuButtonO.png", Texture.class);
        assetManager.load("upgradeGun.png", Texture.class);
        assetManager.load("upgradeGunD.png", Texture.class);
        assetManager.load("upgradeGunO.png", Texture.class);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(delta);
        stage.draw();

        if (assetManager.update()) {
            game.setScreen(new MenuScreen(game, assetManager));
        } else {
            loadingLabel.setText("Loading... " + (int) (assetManager.getProgress() * 100) + "%");
        }
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}

    @Override
    public void dispose() {
        stage.dispose();
        skin.dispose();
    }
}
