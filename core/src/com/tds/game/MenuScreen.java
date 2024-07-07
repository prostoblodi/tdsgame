package com.tds.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class MenuScreen implements Screen {
    private final Game game;
    private final AssetManager assetManager;
    private final Stage stage;
    private TextButton playGameButton, settingsButton, quitButton;
    private Skin skin;

    public MenuScreen(Game game, AssetManager assetManager) {
        this.game = game;
        this.assetManager = assetManager;
        this.stage = new Stage(new ScreenViewport());

        createSkinAndButtons();
        setupStage();
    }

    private void createSkinAndButtons() {
        skin = new Skin();
        skin.add("default-font", new BitmapFont());
        skin.add("playGameButton", assetManager.get("playGameButton.png", Texture.class));
        skin.add("playGameButtonD", assetManager.get("playGameButtonD.png", Texture.class));
        skin.add("playGameButtonO", assetManager.get("playGameButtonO.png", Texture.class));
        skin.add("settingsButton", assetManager.get("settingsButton.png", Texture.class));
        skin.add("settingsD", assetManager.get("settingsD.png", Texture.class));
        skin.add("settingsO", assetManager.get("settingsO.png", Texture.class));
        skin.add("quit", assetManager.get("quit.png", Texture.class));
        skin.add("quitD", assetManager.get("quitD.png", Texture.class));
        skin.add("quitO", assetManager.get("quitO.png", Texture.class));

        playGameButton = createButton("playGameButton", "playGameButtonD", "playGameButtonO");
        settingsButton = createButton("settingsButton", "settingsD", "settingsO");
        quitButton = createButton("quit", "quitD", "quitO");

        playGameButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new MainGameScreen(game, assetManager));
            }
        });

        settingsButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new SettingsScreen(game, assetManager));
            }
        });

        quitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
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
        playGameButton.setPosition(624, 631);
        playGameButton.setSize(673, 134);
        stage.addActor(playGameButton);

        settingsButton.setPosition(624, 473);
        settingsButton.setSize(673, 134);
        stage.addActor(settingsButton);

        quitButton.setPosition(624, 315);
        quitButton.setSize(673, 134);
        stage.addActor(quitButton);

        Gdx.input.setInputProcessor(stage);
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
