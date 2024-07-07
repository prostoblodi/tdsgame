package com.tds.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.Screen;
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
    private final Game game;
    private final AssetManager assetManager;
    private final Stage stage;
    private TextButton backButton, windowButton, fullscreenButton;
    private boolean isFullscreen = false;
    private boolean opened = false;

    public SettingsScreen(Game game, AssetManager assetManager) {
        this.game = game;
        this.assetManager = assetManager;
        this.stage = new Stage(new ScreenViewport());

        createButtons();

        if (!opened) {
            setFullscreen();
            opened = true;
        }
    }

    private void createButtons() {
        Skin skin = createSkin();

        TextButton.TextButtonStyle backButtonStyle = new TextButton.TextButtonStyle();
        backButtonStyle.font = skin.getFont("default-font");
        backButtonStyle.up = skin.getDrawable("back_up");
        backButtonStyle.down = skin.getDrawable("back_down");
        backButtonStyle.over = skin.getDrawable("back_over");

        TextButton.TextButtonStyle fullscreenStyle = new TextButton.TextButtonStyle();
        fullscreenStyle.font = skin.getFont("default-font");
        fullscreenStyle.up = skin.getDrawable("fullscreen_up");
        fullscreenStyle.down = skin.getDrawable("fullscreen_down");
        fullscreenStyle.over = skin.getDrawable("fullscreen_over");

        TextButton.TextButtonStyle windowStyle = new TextButton.TextButtonStyle();
        windowStyle.font = skin.getFont("default-font");
        windowStyle.up = skin.getDrawable("window_up");
        windowStyle.down = skin.getDrawable("window_down");
        windowStyle.over = skin.getDrawable("window_over");

        backButton = new TextButton("", backButtonStyle);
        fullscreenButton = new TextButton("", fullscreenStyle);
        windowButton = new TextButton("", windowStyle);

        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new MenuScreen(game, assetManager));
            }
        });

        fullscreenButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                setFullscreen();
            }
        });

        windowButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                setWindowed();
            }
        });

        stage.addActor(backButton);
        stage.addActor(fullscreenButton);
        stage.addActor(windowButton);
    }

    private Skin createSkin() {
        Skin skin = new Skin();

        skin.add("default-font", new BitmapFont());
        skin.add("back_up", assetManager.get("backButton.png", Texture.class));
        skin.add("back_down", assetManager.get("backButtonD.png", Texture.class));
        skin.add("back_over", assetManager.get("backButtonO.png", Texture.class));
        skin.add("fullscreen_up", assetManager.get("fullscreen.png", Texture.class));
        skin.add("fullscreen_down", assetManager.get("fullscreenD.png", Texture.class));
        skin.add("fullscreen_over", assetManager.get("fullscreenO.png", Texture.class));
        skin.add("window_up", assetManager.get("window.png", Texture.class));
        skin.add("window_down", assetManager.get("windowD.png", Texture.class));
        skin.add("window_over", assetManager.get("windowO.png", Texture.class));

        return skin;
    }

    @Override
    public void show() {
        adjustButtonPositions();
        Gdx.input.setInputProcessor(stage);
    }

    private void adjustButtonPositions() {
        if (isFullscreen) {
            backButton.setPosition(0, 920);
            windowButton.setPosition(438, 540);
            fullscreenButton.setPosition(960, 540);
        } else {
            backButton.setPosition(0, 850);
            windowButton.setPosition(438, 540);
            fullscreenButton.setPosition(960, 540);
        }
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(delta);
        stage.draw();
    }

    private void setFullscreen() {
        if (!isFullscreen) {
            Graphics.DisplayMode currentMode = Gdx.graphics.getDisplayMode();
            Gdx.graphics.setFullscreenMode(currentMode);
            isFullscreen = true;
            adjustButtonPositions();
        }
    }

    private void setWindowed() {
        if (isFullscreen) {
            Gdx.graphics.setWindowedMode(1920, 1010);
            isFullscreen = false;
            adjustButtonPositions();
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
    }
}
