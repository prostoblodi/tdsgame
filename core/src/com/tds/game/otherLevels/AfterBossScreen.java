package com.tds.game.otherLevels;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
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
import com.tds.game.other.MenuScreen;
import com.tds.game.universal.GameClasses;
import com.tds.game.universal.updateAndDrawBulletsAndBadBoys;

public class AfterBossScreen implements Screen {

    private SpriteBatch batch = new SpriteBatch();
    private BitmapFont font;
    private Skin skin = new Skin();
    private Stage stage = new Stage();

    private AssetManager assetManager;
    private Game game;
    private Boolean isSwitched;

    Texture endImg, roadImg, startImg, menuUp, menuDown, menuO;
    private final TextButton.TextButtonStyle menuStyle = new TextButton.TextButtonStyle();

    private final Array<GameClasses.Gun> guns = new Array<>();

    private updateAndDrawBulletsAndBadBoys update;

    private short badBoysCounter;


    public AfterBossScreen(Game game, AssetManager assetManager, Boolean isSwitched, short badBoysCounter) {
        this.game = game;
        this.assetManager = assetManager;
        this.isSwitched = isSwitched;
        this.badBoysCounter = badBoysCounter;
    }

    @Override
    public void show() {
        if(!isSwitched) {
            game.setScreen(new TextAnimator(game, assetManager, badBoysCounter));
        }
        else {
            assetManager.load("road.png", Texture.class);
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
            System.out.println("}+ Textures has been loaded");
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

            System.out.println("}+ Textures has been get");
            setupInitialState();
        }
    }

    private void setupInitialState() {
        update = new updateAndDrawBulletsAndBadBoys(batch, guns, game, assetManager, badBoysCounter);

        createMenuButton();
        addGuns();

        System.out.println("}+ SetupInitialStated");
    }

    public void createMenuButton(){

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

        System.out.println("}+ MenuButton has been created");
    }

    private void drawRoad(){

        short RoadXCord = 1792;

        for(byte i = 0; i < 4; i++){
            batch.draw(roadImg, RoadXCord, 0);
            batch.draw(roadImg, RoadXCord, 952);
            RoadXCord -= 128;
        }

        short RoadYCord = 0;

        for(byte i = 0; i < 4; i++){
            batch.draw(roadImg, 1280, RoadYCord);
            RoadYCord += 128;
        }

        RoadYCord = 952;

        for(byte i = 0; i < 5; i++){
            batch.draw(roadImg, 1280, RoadYCord);
            RoadYCord -= 128;
        }

        RoadXCord = 0;

        for(byte i =  0; i < 10; i++){
            batch.draw(roadImg, RoadXCord, 476);
            RoadXCord += 128;
        }
    }

    public void addGuns(){
        short GunXCord = 1792;

        for(byte i = 0; i < 4; i++) { // create 3 guns
            guns.add(new GameClasses().new Gun(assetManager.get("gun.png", Texture.class), assetManager.get("AAGun.png", Texture.class), assetManager.get("2xGun.png", Texture.class), assetManager.get("5xGun.png", Texture.class), skin, GunXCord, (short) 128, stage));
            guns.add(new GameClasses().new Gun(assetManager.get("gun.png", Texture.class), assetManager.get("AAGun.png", Texture.class), assetManager.get("2xGun.png", Texture.class), assetManager.get("5xGun.png", Texture.class), skin, GunXCord, (short) 824, stage));
            GunXCord -= 128;
        }

        short GunYCord = 256;

        for(byte i = 0; i < 3; i++) { // create 3 guns
            guns.add(new GameClasses().new Gun(assetManager.get("gun.png", Texture.class), assetManager.get("AAGun.png", Texture.class), assetManager.get("2xGun.png", Texture.class), assetManager.get("5xGun.png", Texture.class), skin, GunXCord, GunYCord, stage));
            GunYCord += 128;
        }

        GunXCord = 0;

        for(byte i =  0; i < 10; i++){
            guns.add(new GameClasses().new Gun(assetManager.get("gun.png", Texture.class), assetManager.get("AAGun.png", Texture.class), assetManager.get("2xGun.png", Texture.class), assetManager.get("5xGun.png", Texture.class), skin, GunXCord, (short) 348, stage));
            GunXCord += 128;
        }

        GunXCord = 0;

        for(byte i =  0; i < 10; i++){
            guns.add(new GameClasses().new Gun(assetManager.get("gun.png", Texture.class), assetManager.get("AAGun.png", Texture.class), assetManager.get("2xGun.png", Texture.class), assetManager.get("5xGun.png", Texture.class), skin, GunXCord, (short) 604, stage));
            GunXCord += 128;
        }

        GunYCord = 220;

        for(byte i = 0; i < 2; i++){
            guns.add(new GameClasses().new Gun(assetManager.get("gun.png", Texture.class), assetManager.get("AAGun.png", Texture.class), assetManager.get("2xGun.png", Texture.class), assetManager.get("5xGun.png", Texture.class), skin, (short) 1152, GunYCord, stage));
            GunYCord -= 128;
        }

        GunYCord = 732;

        for(byte i = 0; i < 2; i++){
            guns.add(new GameClasses().new Gun(assetManager.get("gun.png", Texture.class), assetManager.get("AAGun.png", Texture.class), assetManager.get("2xGun.png", Texture.class), assetManager.get("5xGun.png", Texture.class), skin, (short) 1152, GunYCord, stage));
            GunYCord += 128;
        }
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 1);
        for (GameClasses.Gun gun : guns) {
            gun.draw(batch,stage,delta,badBoysCounter);
        }
        batch.begin();
        drawRoad();
        batch.end();

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
