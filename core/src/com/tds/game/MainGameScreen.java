package com.tds.game;

import com.badlogic.gdx.*;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import java.util.Iterator;

public class MainGameScreen implements Screen {

    private final Game game;
    private final AssetManager assetManager;
    private SpriteBatch batch;
    private Stage stage;
    public Skin skin;

    private final Array<GameClasses.Bullet> bullets;
    private final Array<GameClasses.AABullet> AAbullets;
    private final Array<GameClasses.BadBoy> badBoysArray;
    private final Array<GameClasses.AirBadBoy> airBadBoys;
    private final Array<GameClasses.Gun> guns;

    private Texture endImg, roadImg, startImg, badBoysImg, airBadBoyImg, bltImg, AAbltImg, menuUp, menuDown, menuO;
    private Texture redHp, greenHp;
    private BitmapFont font;
    private TextureRegion textureRegion;

    private final TextButton.TextButtonStyle menuStyle = new TextButton.TextButtonStyle();

    private float timeSinceLastBullet;
    private float timeSinceLastBadBoy;
    private float timeSinceLastAirBadBoy;
    float respawnTime = 3;
    float airRespawnTime = 5;

    short badBoysCounter = 0;

    long startTime = TimeUtils.nanoTime();

    boolean airBadBoyCreated;

    public MainGameScreen(Game game, AssetManager assetManager) {
        this.game = game;
        this.assetManager = assetManager;
        this.airBadBoys = new Array<>();
        this.guns = new Array<>();
        this.bullets = new Array<>();
        this.AAbullets = new Array<>();
        this.badBoysArray = new Array<>();
    }

    @Override
    public void show() {
        this.batch = new SpriteBatch();
        this.stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
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
        assetManager.load("redHp.png", Texture.class);
        assetManager.load("greenHp.png", Texture.class);
        assetManager.load("menuButton.png", Texture.class);

        // Load individual textures for buttons
        assetManager.load("nothing.png", Texture.class);
        assetManager.load("nothingD.png", Texture.class);
        assetManager.load("nothingO.png", Texture.class);

        assetManager.load("upgradeGun.png", Texture.class);
        assetManager.load("upgradeGunD.png", Texture.class);
        assetManager.load("upgradeGunO.png", Texture.class);

        assetManager.finishLoading();

        endImg = assetManager.get("end.png", Texture.class);
        roadImg = assetManager.get("road.png", Texture.class);
        startImg = assetManager.get("start.png", Texture.class);
        badBoysImg = assetManager.get("badBoys.png", Texture.class);
        airBadBoyImg = assetManager.get("airBadBoy.png", Texture.class);
        bltImg = assetManager.get("blt.png", Texture.class);
        AAbltImg = assetManager.get("AAblt.png", Texture.class);
        redHp = assetManager.get("redHp.png", Texture.class);
        greenHp = assetManager.get("greenHp.png", Texture.class);

        textureRegion = new TextureRegion(greenHp);

        // Load textures for buttons
        Texture gunButtonUp = assetManager.get("nothing.png", Texture.class);
        Texture gunButtonDown = assetManager.get("nothingD.png", Texture.class);
        Texture gunButtonO = assetManager.get("nothingO.png", Texture.class);

        Texture upgradeGun = assetManager.get("upgradeGun.png", Texture.class);
        Texture upgradeGunD = assetManager.get("upgradeGunD.png", Texture.class);
        Texture upgradeGunO = assetManager.get("upgradeGunO.png", Texture.class);

        menuUp = assetManager.get("menuButton.png", Texture.class);
        menuDown = assetManager.get("menuButtonD.png", Texture.class);
        menuO = assetManager.get("menuButtonO.png", Texture.class);

        // Create Skin
        skin = new Skin();
        skin.add("default-font", new BitmapFont());

        // Create TextButtonStyle
        TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.up = new TextureRegionDrawable(new TextureRegion(gunButtonUp));
        buttonStyle.down = new TextureRegionDrawable(new TextureRegion(gunButtonDown));
        buttonStyle.over = new TextureRegionDrawable(new TextureRegion(gunButtonO));
        buttonStyle.font = skin.getFont("default-font");

        TextButton.TextButtonStyle button2Style = new TextButton.TextButtonStyle();
        button2Style.up = new TextureRegionDrawable(new TextureRegion(upgradeGun));
        button2Style.down = new TextureRegionDrawable(new TextureRegion(upgradeGunD));
        button2Style.over = new TextureRegionDrawable(new TextureRegion(upgradeGunO));
        button2Style.font = skin.getFont("default-font");

        // Add button style to skin
        skin.add("gunButtonStyle", buttonStyle);
        skin.add("updatedGunStyle", button2Style);

        setupInitialState();
    }

    private void setupInitialState() {
        font = new BitmapFont();
        font.setColor(Color.BLACK);
        short GunXCord = 128;
        for(byte i = 0; i < 6; i++) {
            guns.add(new GameClasses().new Gun(assetManager.get("gun.png", Texture.class), assetManager.get("AAGun.png", Texture.class), assetManager.get("2xGun.png", Texture.class), skin, GunXCord, (short) 128, stage));
            GunXCord += 128;
        }
        createButtons();
    }

    @Override
    public void render(float delta) {
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
        updateAndDrawBullets(delta);
        updateAndDrawBadBoys(delta);
        batch.end();
        System.out.println("-----FPS:" + Gdx.graphics.getFramesPerSecond() + ", Counter:" + badBoysCounter + ", Respawn Time:" + respawnTime + "-----" );


    }

    private void drawTextures() {
        for (int i = 0; i < 6; i++) {
            batch.draw(roadImg, 128 + i * 128, 0);
        }
        batch.draw(startImg, 896, 0);
        batch.draw(endImg, 0, 0);
    }

    private void updateAndDrawBullets(float delta) {
        GameClasses.BadBoy nearest;
        GameClasses.AirBadBoy nearestAir;
        timeSinceLastBullet += delta;
        if (timeSinceLastBullet >= 1.0f) {
            for (GameClasses.Gun gun : guns) {
                nearest = gun.findNearestBadBoy(badBoysArray);
                nearestAir = gun.findNearestAirBadBoyX(airBadBoys);
                if(gun.isGunCreated() && gun.getGunLevel() == 1) {
                    if (nearest != null) {
                        if (overlaps(gun.getX(), gun.getY(), nearest.getX(), 16, 500, 64)) {
                            spawnBullet(gun.getX(), gun.getY(), nearest, (byte) 7);
                        }
                    }
                }
                else if(gun.getGunLevel() == 2){
                    if(nearestAir != null){
                        if(overlaps(gun.getX(), gun.getY(), nearestAir.getX(), 16, 700, 64)) {
                            spawnAirBullet(gun.getX(), gun.getY(), nearestAir);
                        }
                    }
                }
                else if(gun.getGunLevel() == 3){
                    if(nearest != null){
                        if(overlaps(gun.getX(), gun.getY(), nearest.getX(), 16, 800, 64)){
                            spawnBullet(gun.getX(), gun.getY(), nearest, (byte) 14);
                        }
                    }
                }
            }
            timeSinceLastBullet = 0;
        }
        Iterator<GameClasses.Bullet> iter = bullets.iterator();
        while (iter.hasNext()) {
            GameClasses.Bullet bullet = iter.next();
            bullet.update();
            bullet.draw(batch);
            if (!bullet.isActive()) {
                iter.remove();
            }
        }
        Iterator<GameClasses.AABullet> iter2 = AAbullets.iterator();
        while (iter2.hasNext()) {
            GameClasses.AABullet AAbullet = iter2.next();
            AAbullet.update();
            AAbullet.draw(batch);
            if (!AAbullet.isActive()) {
                iter2.remove();
            }
        }
    }



    private void spawnBullet(float startX, float startY, GameClasses.BadBoy nearest, byte damage) {
        // Find the nearest bad boy's position
        float targetX = nearest.getX();
        float targetY = 16; // Target Y position of bad boys
        float deltaX = targetX - startX;
        float deltaY = targetY - startY;
        float distance = (float) Math.sqrt(deltaX * deltaX + deltaY * deltaY);
        float speed = 200; // Bullet speed
        float speedX = deltaX / distance * speed;
        float speedY = deltaY / distance * speed;

        // Create a new bullet and add it to the bullets array
        bullets.add(new GameClasses().new Bullet(bltImg, startX, startY, speedX, speedY, badBoysArray, damage));
    }

    private void spawnAirBullet(float startX, float startY, GameClasses.AirBadBoy nearest) {
        // Find the nearest bad boy's position
        float targetX = nearest.getX();
        float targetY = 16; // Target Y position of bad boys
        float deltaX = targetX - startX;
        float deltaY = targetY - startY;
        float distance = (float) Math.sqrt(deltaX * deltaX + deltaY * deltaY);
        float speed = 200; // Bullet speed
        float speedX = deltaX / distance * speed;
        float speedY = deltaY / distance * speed;

        // Create a new bullet and add it to the bullets array
        AAbullets.add(new GameClasses().new AABullet(AAbltImg, startX, startY, speedX, speedY, airBadBoys));
    }


    private void updateAndDrawBadBoys(float delta) {
        long time = TimeUtils.nanoTime() - startTime;
        long seconds = time / 1000000000L;
        if(seconds % 30 == 0 && respawnTime > 0.7){
            respawnTime -= 0.01F;
            if(airBadBoyCreated){
                airRespawnTime -= 0.005F;
            }
        }
        timeSinceLastBadBoy += delta;
        timeSinceLastAirBadBoy += delta;
        for(GameClasses.BadBoy badBoy : badBoysArray){
            if(badBoy.getX() <= 16){
                game.setScreen(new MenuScreen(game, assetManager));
            }
        }
        for(GameClasses.AirBadBoy airbadBoy : airBadBoys){
            if(airbadBoy.getX() <= 16){
                game.setScreen(new MenuScreen(game, assetManager));
            }
        }
        if (timeSinceLastBadBoy >= respawnTime) {
            GameClasses.BadBoy badBoy = new GameClasses().new BadBoy(badBoysImg, redHp, textureRegion, (short) 912, new Rectangle(912, 16, badBoysImg.getWidth(), badBoysImg.getHeight()), new Circle(912, 16, (float) badBoysImg.getWidth() / 2));
            badBoysArray.add(badBoy);
            badBoysCounter++;

            if(badBoysCounter >= 10 && !airBadBoyCreated){
                GameClasses.AirBadBoy airBadBoy = new GameClasses().new AirBadBoy(airBadBoyImg, redHp, textureRegion, (short) 912, new Rectangle(912,16, airBadBoyImg.getWidth(), airBadBoyImg.getHeight()), new Circle(912, 16, (airBadBoyImg.getWidth() / 2)));
                airBadBoys.add(airBadBoy);
                System.out.println("AirBadBoyCreated");
                airBadBoyCreated = true;
            }
            else if(airBadBoyCreated){
                if(timeSinceLastAirBadBoy >= airRespawnTime){
                    GameClasses.AirBadBoy airBadBoy = new GameClasses().new AirBadBoy(airBadBoyImg, redHp, textureRegion, (short) 912, new Rectangle(912,16, airBadBoyImg.getWidth(), airBadBoyImg.getHeight()), new Circle(912, 16, (airBadBoyImg.getWidth() / 2)));
                    airBadBoys.add(airBadBoy);
                    timeSinceLastAirBadBoy = 0;
                }
            }

            timeSinceLastBadBoy = 0;
        }

        Iterator<GameClasses.BadBoy> iter = badBoysArray.iterator();
        while (iter.hasNext()) {
            GameClasses.BadBoy badBoys = iter.next();
            badBoys.update();
            badBoys.draw(batch);
            if (!badBoys.isActive()) {
                iter.remove();
            }
        }

        Iterator<GameClasses.AirBadBoy> iter2 = airBadBoys.iterator();
        while (iter2.hasNext()) {
            GameClasses.AirBadBoy airbadBoy = iter2.next();
            airbadBoy.update();
            airbadBoy.draw(batch); // Ensure batch is passed to draw method
            if (!airbadBoy.isActive()) {
                iter2.remove();
            }
        }
    }

    public boolean overlaps(float x, float y,float cx, float cy, float radius, float radius2) {
        float dx = x - cx;
        float dy = y - cy;
        float distance = dx * dx + dy * dy;
        float radiusSum = radius + radius2;
        return distance < radiusSum * radiusSum;
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
        badBoysImg.dispose();
        bltImg.dispose();
        redHp.dispose();
        greenHp.dispose();
    }
}
