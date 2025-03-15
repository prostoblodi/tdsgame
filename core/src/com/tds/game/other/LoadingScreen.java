package com.tds.game.other;

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
        assetManager.load("menu_buttons/play/playGameButton.png", Texture.class);
        assetManager.load("menu_buttons/play/playGameButtonD.png", Texture.class);
        assetManager.load("menu_buttons/play/playGameButtonO.png", Texture.class);
        assetManager.load("menu_buttons/settings/settingsButton.png", Texture.class);
        assetManager.load("menu_buttons/settings/settingsD.png", Texture.class);
        assetManager.load("menu_buttons/settings/settingsO.png", Texture.class);
        assetManager.load("menu_buttons/quit/quit.png", Texture.class);
        assetManager.load("menu_buttons/quit/quitD.png", Texture.class);
        assetManager.load("menu_buttons/quit/quitO.png", Texture.class);

        assetManager.load("settings_buttons/back/backButton.png", Texture.class);
        assetManager.load("settings_buttons/back/backButtonD.png", Texture.class);
        assetManager.load("settings_buttons/back/backButtonO.png", Texture.class);
        assetManager.load("settings_buttons/fullscreen/fullscreen.png", Texture.class);
        assetManager.load("settings_buttons/fullscreen/fullscreenD.png", Texture.class);
        assetManager.load("settings_buttons/fullscreen/fullscreenO.png", Texture.class);
        assetManager.load("settings_buttons/window/window.png", Texture.class);
        assetManager.load("settings_buttons/window/windowD.png", Texture.class);
        assetManager.load("settings_buttons/window/windowO.png", Texture.class);

        assetManager.load("some_decor/end.png", Texture.class);
        assetManager.load("some_decor/road.png", Texture.class);
        assetManager.load("some_decor/start.png", Texture.class);

        assetManager.load("guns/gun.png", Texture.class);
        assetManager.load("guns/AAGun.png", Texture.class);
        assetManager.load("guns/2xGun.png", Texture.class);
        assetManager.load("guns/5xGun.png", Texture.class);

        assetManager.load("bad_boys/badBoys.png", Texture.class);
        assetManager.load("bad_boys/airBadBoy.png", Texture.class);

        assetManager.load("bullets/blt.png", Texture.class);
        assetManager.load("bullets/AAblt.png", Texture.class);

        assetManager.load("hp/redHp.png", Texture.class);
        assetManager.load("hp/greenHp.png", Texture.class);

        assetManager.load("game_buttons/nothing/nothing.png", Texture.class);
        assetManager.load("game_buttons/nothing/nothingD.png", Texture.class);
        assetManager.load("game_buttons/nothing/nothingO.png", Texture.class);
        assetManager.load("game_buttons/menu/menuButton.png", Texture.class);
        assetManager.load("game_buttons/menu/menuButtonD.png", Texture.class);
        assetManager.load("game_buttons/menu/menuButtonO.png", Texture.class);
        assetManager.load("game_buttons/upgrade/upgradeGun.png", Texture.class);
        assetManager.load("game_buttons/upgrade/upgradeGunD.png", Texture.class);
        assetManager.load("game_buttons/upgrade/upgradeGunO.png", Texture.class);
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
