package com.tds.game.other;

import com.badlogic.gdx.*;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;

public class SettingsScreen implements Screen {
    private final Game game;  // The game instance to switch between screens
    private final AssetManager assetManager;  // Asset manager to load assets
    private final Stage stage;  // Stage to manage scene and actors
    private TextButton backButton, windowButton, fullscreenButton;  // Buttons for user interaction
    private boolean isFullscreen = false;  // Flag to check if fullscreen is enabled
    private final boolean isAndroid;  // Flag to check if the app is running on Android

    public SettingsScreen(Game game, AssetManager assetManager) {
        this.game = game;
        this.assetManager = assetManager;
        this.stage = new Stage(new ScreenViewport());  // Create a new stage with a screen viewport
        this.isAndroid = Gdx.app.getType() == Application.ApplicationType.Android;  // Check if it's Android

        createButtons();  // Create the UI buttons

        // Check and set the screen mode based on the current graphics settings
        if (Gdx.graphics.isFullscreen()) {
            setFullscreen();  // Set fullscreen mode if already enabled
        }
        else {
            setWindowed();  // Set windowed mode if fullscreen is not enabled
        }
    }

    private void createButtons() {
        // Create a skin to apply styles to buttons
        Skin skin = createSkin();

        // Create and style the back button
        TextButton.TextButtonStyle backButtonStyle = new TextButton.TextButtonStyle();
        backButtonStyle.font = skin.getFont("default-font");
        backButtonStyle.up = skin.getDrawable("back_up");
        backButtonStyle.down = skin.getDrawable("back_down");
        backButtonStyle.over = skin.getDrawable("back_over");

        // Create and style the fullscreen button
        TextButton.TextButtonStyle fullscreenStyle = new TextButton.TextButtonStyle();
        fullscreenStyle.font = skin.getFont("default-font");
        fullscreenStyle.up = skin.getDrawable("fullscreen_up");
        fullscreenStyle.down = skin.getDrawable("fullscreen_down");
        fullscreenStyle.over = skin.getDrawable("fullscreen_over");

        // Create and style the windowed button
        TextButton.TextButtonStyle windowStyle = new TextButton.TextButtonStyle();
        windowStyle.font = skin.getFont("default-font");
        windowStyle.up = skin.getDrawable("window_up");
        windowStyle.down = skin.getDrawable("window_down");
        windowStyle.over = skin.getDrawable("window_over");

        // Initialize buttons with styles
        backButton = new TextButton("", backButtonStyle);
        fullscreenButton = new TextButton("", fullscreenStyle);
        windowButton = new TextButton("", windowStyle);

        // Add listeners for the button click events
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new MenuScreen(game, assetManager));  // Switch to the main menu screen
            }
        });

        fullscreenButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                setFullscreen();  // Set the screen to fullscreen when clicked
            }
        });

        windowButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                setWindowed();  // Set the screen to windowed mode when clicked
            }
        });

        // Add buttons to the stage
        stage.addActor(backButton);
        stage.addActor(fullscreenButton);
        stage.addActor(windowButton);
    }

    private Skin createSkin() {
        // Create and configure a new skin with assets
        Skin skin = new Skin();

        skin.add("default-font", new BitmapFont());  // Add default font
        skin.add("back_up", assetManager.get("settings_buttons/back/backButton.png", Texture.class));
        skin.add("back_down", assetManager.get("settings_buttons/back/backButtonD.png", Texture.class));
        skin.add("back_over", assetManager.get("settings_buttons/back/backButtonO.png", Texture.class));
        skin.add("fullscreen_up", assetManager.get("settings_buttons/fullscreen/fullscreen.png", Texture.class));
        skin.add("fullscreen_down", assetManager.get("settings_buttons/fullscreen/fullscreenD.png", Texture.class));
        skin.add("fullscreen_over", assetManager.get("settings_buttons/fullscreen/fullscreenO.png", Texture.class));
        skin.add("window_up", assetManager.get("settings_buttons/window/window.png", Texture.class));
        skin.add("window_down", assetManager.get("settings_buttons/window/windowD.png", Texture.class));
        skin.add("window_over", assetManager.get("settings_buttons/window/windowO.png", Texture.class));

        return skin;  // Return the configured skin
    }

    @Override
    public void show() {
        adjustButtonPositions();  // Adjust button positions based on screen mode
        Gdx.input.setInputProcessor(stage);  // Set the input processor to the stage to receive input events
    }

    private void adjustButtonPositions() {
        // Adjust button positions depending on fullscreen or Android mode
        if (isFullscreen || isAndroid) {
            backButton.setPosition(0, 920);  // Set position for fullscreen or Android
        }
        else {
            backButton.setPosition(0, 850);  // Set position for windowed mode
        }
        windowButton.setPosition(200, 540);  // Position window button
        fullscreenButton.setPosition(1057, 540);  // Position fullscreen button
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);  // Clear the screen
        stage.act(delta);  // Update the stage
        stage.draw();  // Draw the stage and all its actors
    }

    private void setFullscreen() {
        // Set fullscreen mode if not already in fullscreen
        if (!isFullscreen) {
            Graphics.DisplayMode currentMode = Gdx.graphics.getDisplayMode();  // Get current display mode
            Gdx.graphics.setFullscreenMode(currentMode);  // Set the screen to fullscreen
            isFullscreen = true;  // Set the fullscreen flag
            adjustButtonPositions();  // Adjust button positions after changing mode
        }
    }

    private void setWindowed() {
        // Set windowed mode if in fullscreen mode
        if (isFullscreen) {
            Gdx.graphics.setWindowedMode(1920, 1010);  // Set the screen to windowed mode with specific resolution
            isFullscreen = false;  // Reset the fullscreen flag
            adjustButtonPositions();  // Adjust button positions after changing mode
        }
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
    }
}
