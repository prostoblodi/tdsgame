package com.tds.game.other;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.tds.game.level1.MainGameScreen;

public class MenuScreen implements Screen {
    /** The game instance to switch between screens */
    private final Game game;
    /** Asset manager(load, get assets) */
    private final AssetManager assetManager;
    /** Manages scenes and actors */
    private final Stage stage;
    private TextButton playGameButton, settingsButton, quitButton;
    /** Style the buttons */
    private Skin skin;
    private final Label label;

    public MenuScreen(Game game, AssetManager assetManager) {
        this.game = game;
        this.assetManager = assetManager;
        this.stage = new Stage(new ScreenViewport());  // Create a new stage with a screen viewport

        // Loading text
        Label.LabelStyle labelStyle = new Label.LabelStyle();
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("font.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 160;
        parameter.characters = FreeTypeFontGenerator.DEFAULT_CHARS;
        labelStyle.font = generator.generateFont(parameter);
        label = new Label("TDS game", labelStyle);

        createSkinAndButtons();
        setupStage();
    }

    private void createSkinAndButtons() {
        // Create a skin to apply styles to buttons
        skin = new Skin();
        skin.add("default-font", new BitmapFont());  // Add default font
        skin.add("playGameButton", assetManager.get("menu_buttons/play/playGameButton.png", Texture.class));
        skin.add("playGameButtonD", assetManager.get("menu_buttons/play/playGameButtonD.png", Texture.class));
        skin.add("playGameButtonO", assetManager.get("menu_buttons/play/playGameButtonO.png", Texture.class));
        skin.add("settingsButton", assetManager.get("menu_buttons/settings/settingsButton.png", Texture.class));
        skin.add("settingsD", assetManager.get("menu_buttons/settings/settingsD.png", Texture.class));
        skin.add("settingsO", assetManager.get("menu_buttons/settings/settingsO.png", Texture.class));
        skin.add("quit", assetManager.get("menu_buttons/quit/quit.png", Texture.class));
        skin.add("quitD", assetManager.get("menu_buttons/quit/quitD.png", Texture.class));
        skin.add("quitO", assetManager.get("menu_buttons/quit/quitO.png", Texture.class));

        // Create buttons using the above skins and style them
        playGameButton = createButton("playGameButton", "playGameButtonD", "playGameButtonO");
        settingsButton = createButton("settingsButton", "settingsD", "settingsO");
        quitButton = createButton("quit", "quitD", "quitO");

        // Add listeners to the buttons for user interaction
        playGameButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new MainGameScreen(game, assetManager));  // Switch to the main game screen when clicked
            }
        });

        settingsButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new SettingsScreen(game, assetManager));  // Switch to the settings screen when clicked
            }
        });

        quitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();  // Exit the application when clicked
            }
        });
    }

    private TextButton createButton(String up, String down, String over) {
        TextButton.TextButtonStyle style = new TextButton.TextButtonStyle();
        style.font = skin.getFont("default-font");
        style.up = skin.getDrawable(up);
        style.down = skin.getDrawable(down);
        style.over = skin.getDrawable(over);

        return new TextButton("", style);
    }

    private void setupStage() {
        // Set button positions and sizes
        playGameButton.setPosition(624, 561);
        playGameButton.setSize(673, 134);
        stage.addActor(playGameButton);

        settingsButton.setPosition(624, 403);
        settingsButton.setSize(673, 134);
        stage.addActor(settingsButton);

        quitButton.setPosition(624, 245);
        quitButton.setSize(673, 134);
        stage.addActor(quitButton);

        label.setPosition(510, 720);
        stage.addActor(label);

        // Set the input processor to the stage to handle touch inputs
        Gdx.input.setInputProcessor(stage);
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
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);  // Update the viewport when the window is resized
    }

    @Override
    public void pause() {}  // Method called when the game is paused (not used here)

    @Override
    public void resume() {}  // Method called when the game is resumed (not used here)

    @Override
    public void hide() {}  // Method called when the screen is hidden (not used here)

    @Override
    public void dispose() {
        stage.dispose();  // Dispose of the stage to free resources
        skin.dispose();  // Dispose of the skin to free resources
    }
}
