package com.tds.game.other;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.tds.game.managers.SettingsManager;

public class LoadingScreen implements Screen {
    /** The game instance to switch between screens */
    private final Game game;
    /** Asset manager(load, get assets) */
    private final AssetManager assetManager;
    /** Manages scenes and actors */
    private final Stage stage;
    /** Style */
    private final Skin skin;
    private final Label loadingLabel;
    private FreeTypeFontGenerator generator;

    public LoadingScreen(Game game, AssetManager assetManager) {
        SettingsManager settingsManager = new SettingsManager();
        settingsManager.enableOrNotFullscreen(false);

        this.game = game;
        this.assetManager = assetManager;
        this.stage = new Stage(new ScreenViewport());  // Create a new stage with a screen viewport
        stage.getViewport().update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);  // Update the viewport when the window is resized

        this.skin = createSkin();  // Create the skin to apply styles
        this.loadingLabel = createLoadingLabel();  // Create the loading label

        Table table = new Table();  // Create a table to center the loading label
        table.setFillParent(true);  // Fill the entire stage with the table
        table.add(loadingLabel).center();  // Add the label to the table and center it
        stage.addActor(table);  // Add the table to the stage


        loadAssets();  // Begin loading assets asynchronously
    }

    private Skin createSkin() {
        // Create and return a new skin with a default font
        Skin skin = new Skin();
        skin.add("default-font", new BitmapFont());
        return skin;
    }

    private Label createLoadingLabel() {
        // Create a label to show the "Loading..." text
        LabelStyle labelStyle = new LabelStyle();
        generator = new FreeTypeFontGenerator(Gdx.files.internal("font.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 60;
        parameter.characters = FreeTypeFontGenerator.DEFAULT_CHARS;
        labelStyle.font = generator.generateFont(parameter);
        return new Label("Loading...", labelStyle);
    }

    private void loadAssets() {
        // Load all the assets needed for the game
        assetManager.load("menu_buttons/play/playGameButton.png", Texture.class);
        assetManager.load("menu_buttons/play/playGameButtonD.png", Texture.class);
        assetManager.load("menu_buttons/play/playGameButtonO.png", Texture.class);
        assetManager.load("menu_buttons/settings/settingsButton.png", Texture.class);
        assetManager.load("menu_buttons/settings/settingsD.png", Texture.class);
        assetManager.load("menu_buttons/settings/settingsO.png", Texture.class);
        assetManager.load("menu_buttons/quit/quit.png", Texture.class);
        assetManager.load("menu_buttons/quit/quitD.png", Texture.class);
        assetManager.load("menu_buttons/quit/quitO.png", Texture.class);

        // Additional assets (settings buttons, decorations, guns, etc.)
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
        Gdx.input.setInputProcessor(stage);  // Set the input processor when the screen is shown
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);  // Clear the screen

        stage.act(delta);  // Update the stage (process actors and events)
        stage.draw();  // Draw the stage and all its actors

        if (assetManager.update()) {
            // If the assets are loaded, switch to the MenuScreen
            game.setScreen(new MenuScreen(game, assetManager));
        } else {
            // If the assets are still loading, update the loading label with progress
            loadingLabel.setText("Loading... " + (int) (assetManager.getProgress() * 100) + "%");
        }
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);  // Update the viewport when the window is resized
    }

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}

    @Override
    public void dispose() {
        stage.dispose();  // Dispose of the stage to free resources
        skin.dispose();  // Dispose of the skin to free resources
        generator.dispose();  // Dispose of the font generator to free resources
    }
}
