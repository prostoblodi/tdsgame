package com.tds.game.level1;

import com.badlogic.gdx.*;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.tds.game.universal.GameClasses;
import com.tds.game.other.MenuScreen;
import com.tds.game.universal.updateAndDrawBulletsAndBadBoys;

public class MainGameScreen implements Screen {

    private updateAndDrawBulletsAndBadBoys update; // Function that update bad boys and bullets

    private final Game game; // Main game variable, makes it possible to open different windows
    private final AssetManager assetManager; // Just an asset manager(textures, etc.)
    private SpriteBatch batch; // Variable that is needed for rendering textures
    private Stage stage; // Variable that is needed to render buttons
    public Skin skin; // A variable that assigns objects their textures

    private Texture endImg, roadImg, startImg, menuUp, menuDown, menuO; // Textures
    private BitmapFont font; // Text

    private final TextButton.TextButtonStyle menuStyle = new TextButton.TextButtonStyle();

    private final Array<GameClasses.Gun> guns = new Array<>();

    public short badBoysCounter = 0;

    private boolean isDebugEnabled = false;

    public MainGameScreen(Game game, AssetManager assetManager) {
        this.game = game;
        this.assetManager = assetManager;
    }

    @Override
    public void show() { // Load all textures
        this.batch = new SpriteBatch();
        this.stage = new Stage(new ScreenViewport());

        Gdx.input.setInputProcessor(stage);

        System.out.println("}- Textures has been loaded");

        // save some pictures
        endImg = assetManager.get("some_decor/end.png", Texture.class);
        roadImg = assetManager.get("some_decor/road.png", Texture.class);
        startImg = assetManager.get("some_decor/start.png", Texture.class);

        // save gun spawn button pictures
        Texture gunButtonUp = assetManager.get("game_buttons/nothing/nothing.png", Texture.class);
        Texture gunButtonDown = assetManager.get("game_buttons/nothing/nothingD.png", Texture.class);
        Texture gunButtonO = assetManager.get("game_buttons/nothing/nothingO.png", Texture.class);

        // save upgrade gun button pictures
        Texture upgradeGun = assetManager.get("game_buttons/upgrade/upgradeGun.png", Texture.class);
        Texture upgradeGunD = assetManager.get("game_buttons/upgrade/upgradeGunD.png", Texture.class);
        Texture upgradeGunO = assetManager.get("game_buttons/upgrade/upgradeGunO.png", Texture.class);

        // save menu button pictures
        menuUp = assetManager.get("game_buttons/menu/menuButton.png", Texture.class);
        menuDown = assetManager.get("game_buttons/menu/menuButtonD.png", Texture.class);
        menuO = assetManager.get("game_buttons/menu/menuButtonO.png", Texture.class);

        // create Skin
        skin = new Skin();
        skin.add("default-font", new BitmapFont());

        // create gun spawn button style
        TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.up = new TextureRegionDrawable(new TextureRegion(gunButtonUp));
        buttonStyle.down = new TextureRegionDrawable(new TextureRegion(gunButtonDown));
        buttonStyle.over = new TextureRegionDrawable(new TextureRegion(gunButtonO));
        buttonStyle.font = skin.getFont("default-font");

        // create upgrade gun button style
        TextButton.TextButtonStyle button2Style = new TextButton.TextButtonStyle();
        button2Style.up = new TextureRegionDrawable(new TextureRegion(upgradeGun));
        button2Style.down = new TextureRegionDrawable(new TextureRegion(upgradeGunD));
        button2Style.over = new TextureRegionDrawable(new TextureRegion(upgradeGunO));
        button2Style.font = skin.getFont("default-font");

        // add buttons styles at skin
        skin.add("gunButtonStyle", buttonStyle);
        skin.add("updatedGunStyle", button2Style);

        System.out.println("}- Textures has been get");

        setupInitialState();
    }

    private void setupInitialState() { // Base state of some variables
        font = new BitmapFont();
        font.setColor(Color.WHITE);

        update = new updateAndDrawBulletsAndBadBoys(batch, guns, game, assetManager, badBoysCounter);

        short GunXCord = 128;

        for(byte i = 0; i < 6; i++) { // create 6 guns
            guns.add(new GameClasses.Gun(assetManager.get("guns/gun.png", Texture.class), assetManager.get("guns/AAGun.png", Texture.class), assetManager.get("guns/2xGun.png", Texture.class), assetManager.get("guns/5xGun.png", Texture.class), skin, GunXCord, (short) 128, stage));
            GunXCord += 128;
        }

        System.out.println("}- SetupInitialStated");
        createButtons();
    }

    @Override
    public void render(float delta) {
        badBoysCounter = update.badBoysCounter;

        ScreenUtils.clear(0, 0, 0, 1);
        stage.act(delta);
        stage.draw();

        for (GameClasses.Gun gun : guns) {
            gun.draw(batch,stage,delta,badBoysCounter);
        }

        batch.begin();
        drawTextures();

        if((byte) ((badBoysCounter - 5) <= 0 ? (5-badBoysCounter) : 0) != 0) {
            font.draw(batch, "For upgrade you need kill " + (byte) ((badBoysCounter - 5) <= 0 ? (5-badBoysCounter) : 0) + " more bad boys", 128, 276); // это враньё, там просто считается сколько вышло чубриков
        }

        if (Gdx.input.isKeyPressed(Input.Keys.F1) || isDebugEnabled) {
            update.enableDebugMode();
            if(!isDebugEnabled){isDebugEnabled = true;}
        }

        update.updateAndDrawBullets(delta);
        update.updateAndDrawBadBoys(delta);

        batch.end();
    }

    private void drawTextures() {
        for (int i = 0; i < 6; i++) {
            batch.draw(roadImg, 128 + i * 128, 0);
        }
        batch.draw(startImg, 896, 0);
        batch.draw(endImg, 0, 0);
    }

    public void createButtons(){
        short y = (short) (!Gdx.graphics.isFullscreen() ? 860 : 920);

        menuStyle.up = new TextureRegionDrawable(new TextureRegion(menuUp));
        menuStyle.down = new TextureRegionDrawable(new TextureRegion(menuDown));
        menuStyle.over = new TextureRegionDrawable(new TextureRegion(menuO));
        menuStyle.font = skin.getFont("default-font");

        skin.add("menuButton", menuStyle);

        TextButton menuButton = new TextButton("", menuStyle);

        menuButton.setPosition(0,y);
        menuButton.setSize(160, 160);

        stage.addActor(menuButton);


        menuButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new MenuScreen(game, assetManager));
            }
        });

        System.out.println("}- Buttons has been created");
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, false);
    }

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
        disposeTextures();
        stage.dispose();
    }

    private void disposeTextures() {
        endImg.dispose();
        roadImg.dispose();
        startImg.dispose();
    }
}
