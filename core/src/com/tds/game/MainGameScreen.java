package com.tds.game;

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

public class MainGameScreen implements Screen {

    private updateAndDrawBulletsAndBadBoys update;

    private final Game game;
    private final AssetManager assetManager;
    private SpriteBatch batch;
    private Stage stage;
    public Skin skin;

    private Texture endImg, roadImg, startImg, menuUp, menuDown, menuO;
    private BitmapFont font;

    private final TextButton.TextButtonStyle menuStyle = new TextButton.TextButtonStyle();

    private final Array<GameClasses.Gun> guns = new Array<>();

    short badBoysCounter = 0;

    public MainGameScreen(Game game, AssetManager assetManager) {
        this.game = game;
        this.assetManager = assetManager;
    }

    @Override
    public void show() {
        this.batch = new SpriteBatch();
        this.stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
        // load end picture
        assetManager.load("end.png", Texture.class);
        // load guns pictures
        assetManager.load("gun.png", Texture.class);
        assetManager.load("AAGun.png", Texture.class);
        assetManager.load("2xGun.png", Texture.class);
        // load road and start(bad boys spawn) pictures
        assetManager.load("road.png", Texture.class);
        assetManager.load("start.png", Texture.class);
        // load hp pictures
        assetManager.load("redHp.png", Texture.class);
        assetManager.load("greenHp.png", Texture.class);
        // load menu button picture
        assetManager.load("menuButton.png", Texture.class);
        // load gun spawn button picture
        assetManager.load("nothing.png", Texture.class);
        assetManager.load("nothingD.png", Texture.class);
        assetManager.load("nothingO.png", Texture.class);
        // load upgrade gun button picture
        assetManager.load("upgradeGun.png", Texture.class);
        assetManager.load("upgradeGunD.png", Texture.class);
        assetManager.load("upgradeGunO.png", Texture.class);
        // finish loading
        assetManager.finishLoading();
        // save some pictures
        endImg = assetManager.get("end.png", Texture.class);
        roadImg = assetManager.get("road.png", Texture.class);
        startImg = assetManager.get("start.png", Texture.class);

        // save gun spawn button pictures
        Texture gunButtonUp = assetManager.get("nothing.png", Texture.class);
        Texture gunButtonDown = assetManager.get("nothingD.png", Texture.class);
        Texture gunButtonO = assetManager.get("nothingO.png", Texture.class);
        // save upgrade gun button pictures
        Texture upgradeGun = assetManager.get("upgradeGun.png", Texture.class);
        Texture upgradeGunD = assetManager.get("upgradeGunD.png", Texture.class);
        Texture upgradeGunO = assetManager.get("upgradeGunO.png", Texture.class);
        // save menu button pictures
        menuUp = assetManager.get("menuButton.png", Texture.class);
        menuDown = assetManager.get("menuButtonD.png", Texture.class);
        menuO = assetManager.get("menuButtonO.png", Texture.class);

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

        System.out.println("}- Textures has been loaded");

        setupInitialState();
    }

    private void setupInitialState() {
        font = new BitmapFont();
        font.setColor(Color.BLACK);

        update = new updateAndDrawBulletsAndBadBoys(batch, guns, game, assetManager, badBoysCounter);

        short GunXCord = 128;

        for(byte i = 0; i < 6; i++) { // create 6 guns
            guns.add(new GameClasses().new Gun(assetManager.get("gun.png", Texture.class), assetManager.get("AAGun.png", Texture.class), assetManager.get("2xGun.png", Texture.class), assetManager.get("5xGun.png", Texture.class), skin, GunXCord, (short) 128, stage));
            GunXCord += 128;
        }

        System.out.println("}- SetupInitialStated");
        createButtons();
    }

    @Override
    public void render(float delta) {
        badBoysCounter = update.badBoysCounter;
        byte moreKills = (byte) ((badBoysCounter - 5) <= 0 ? (5-badBoysCounter) : 0);
        boolean show = moreKills != 0;

        ScreenUtils.clear(0, 0, 0, 1);
        stage.act(delta);
        stage.draw();

        for (GameClasses.Gun gun : guns) {
            gun.draw(batch,stage,delta,badBoysCounter);
        }

        batch.begin();
        drawTextures();

        if(show) {
            font.draw(batch, "For upgrade you need kill " + moreKills + " more bad boys", 128, 256); // это враньё, там просто считается сколько вышло чубриков
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
