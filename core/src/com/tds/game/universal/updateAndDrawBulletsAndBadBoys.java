package com.tds.game.universal;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;
import com.tds.game.other.MenuScreen;

import java.util.Iterator;

public class updateAndDrawBulletsAndBadBoys {
    // Time variables for managing update intervals
    private float timeSinceLastBullet, timeSinceLastBadBoy, timeSinceLastAirBadBoy;

    // Respawn times for enemies
    float airRespawnTime = 5, respawnTime = 3;

    public short badBoysCounter; // Counter for the number of "bad boys"

    // Textures for game objects
    private TextureRegion greenHp;
    private Texture airBadBoyImg, redHp, AAbltImg, bltImg, badBoysImg;

    // Start time to track game duration
    long startTime = TimeUtils.nanoTime();

    // Flags to control game states
    boolean airBadBoyCreated, isBossCreated = false;

    Batch batch; // Renderer for drawing game objects

    // Arrays to store active game objects
    private final Array<GameClasses.Bullet> bullets = new Array<>();
    private final Array<GameClasses.AABullet> AAbullets = new Array<>();
    private final Array<GameClasses.BadBoy> badBoysArray = new Array<>();
    private final Array<GameClasses.AirBadBoy> airBadBoys = new Array<>();
    private final Array<GameClasses.Gun> guns;

    private final Array<GameClasses.debugAble> debugAbles = new Array<>();

    Game game;
    AssetManager assetManager;

    public updateAndDrawBulletsAndBadBoys(Batch batch, Array<GameClasses.Gun> guns, Game game, AssetManager assetManager, short badBoysCounter) {
        this.batch = batch;
        this.guns = guns;
        this.assetManager = assetManager;
        this.game = game;
        this.badBoysCounter = badBoysCounter;
        getTextures(assetManager); // Load textures
    }

    // Load all required textures from the AssetManager
    private void getTextures(AssetManager assetManager) {
        airBadBoyImg = assetManager.get("bad_boys/airBadBoy.png", Texture.class);
        badBoysImg = assetManager.get("bad_boys/badBoys.png", Texture.class);

        bltImg = assetManager.get("bullets/blt.png", Texture.class);
        AAbltImg = assetManager.get("bullets/AAblt.png", Texture.class);

        redHp = assetManager.get("hp/redHp.png", Texture.class);

        greenHp = new TextureRegion(assetManager.get("hp/greenHp.png", Texture.class));

        System.out.println("}-- Textures loaded successfully");
    }

    // Update and draw bullets
    public void updateAndDrawBullets(float delta) {
        GameClasses.BadBoy nearest;
        GameClasses.AirBadBoy nearestAir;
        timeSinceLastBullet += delta;

        // Spawn bullets at regular intervals
        if (timeSinceLastBullet >= 1.0f) {
            for (GameClasses.Gun gun : guns) {
                nearest = gun.findNearestBadBoy(badBoysArray);
                nearestAir = gun.findNearestAirBadBoyX(airBadBoys);

                // Check gun level and spawn bullets accordingly
                if (gun.isGunCreated() && gun.getGunLevel() == 1) {
                    if (nearest != null && overlaps(gun.x, gun.y, nearest.getX(), 500)) {
                        spawnBullet(gun.x, gun.y, nearest, (byte) 7);
                    }
                }
                else if (gun.getGunLevel() == 2) {
                    if (nearestAir != null && overlaps(gun.x, gun.y, nearestAir.getX(), 700)) {
                        spawnAirBullet(gun.x, gun.y, nearestAir);
                    }
                }
                else if (gun.getGunLevel() == 3) {
                    if (nearest != null && overlaps(gun.x, gun.y, nearest.getX(), 800)) {
                        spawnBullet(gun.x, gun.y, nearest, (byte) 14);
                    }
                }
                else if (gun.getGunLevel() == 4) {
                    if (nearest != null && overlaps(gun.x, gun.y, nearest.getX(), 800)) {
                        spawnBullet(gun.x, gun.y, nearest, (byte) 35);
                    }
                }
            }
            timeSinceLastBullet = 0;
        }

        // Update and draw bullets
        Iterator<GameClasses.Bullet> iter = bullets.iterator();
        while (iter.hasNext()) {
            GameClasses.Bullet bullet = iter.next();
            bullet.update();
            bullet.draw(batch);
            if (!bullet.isActive()) {
                iter.remove();
            }
        }

        // Update and draw anti-air bullets
        Iterator<GameClasses.AABullet> iter2 = AAbullets.iterator();
        while (iter2.hasNext()) {
            GameClasses.AABullet AAbullet = iter2.next();
            AAbullet.update();
            AAbullet.draw(batch);
            if (!AAbullet.isActive()) {
                iter2.remove();
            }
        }

        for (GameClasses.Bullet bullet : bullets){
            debugAbles.add(bullet);
        }
        for (GameClasses.AABullet AAbullet : AAbullets){
            debugAbles.add(AAbullet);
        }
    }

    // Update and draw bad boys
    public void updateAndDrawBadBoys(float delta) {
        long time = TimeUtils.nanoTime() - startTime;
        long seconds = time / 1000000000L;

        // Adjust respawn time dynamically
        if (seconds % 30 == 0 && respawnTime > 0.7) {
            respawnTime -= 0.01F;
            if (airBadBoyCreated) {
                airRespawnTime -= 0.005F;
            }
        }

        timeSinceLastBadBoy += delta;
        timeSinceLastAirBadBoy += delta;

        // Check if bad boys or air bad boys reach the base
        for (GameClasses.BadBoy badBoy : badBoysArray) {
            if (badBoy.getX() <= 16) {
                game.setScreen(new MenuScreen(game, assetManager));
            }
        }
        for (GameClasses.AirBadBoy airBadBoy : airBadBoys) {
            if (airBadBoy.getX() <= 16) {
                game.setScreen(new MenuScreen(game, assetManager));
            }
        }

        // Spawn bad boys
        if (timeSinceLastBadBoy >= respawnTime && !isBossCreated) {
            GameClasses.BadBoy badBoy = new GameClasses.BadBoy(badBoysImg, redHp, greenHp, (short) 912, (short) 16, new Rectangle(912, 16, badBoysImg.getWidth(), badBoysImg.getHeight()), new Circle(912, 16, (float) badBoysImg.getWidth() / 2));
            badBoysArray.add(badBoy);
            badBoysCounter++;

            System.out.println("}-- " + badBoysCounter + " bad boys created");

            // Spawn air bad boys after a certain count of bad boys
            if (badBoysCounter >= 10 && !airBadBoyCreated) {
                GameClasses.AirBadBoy airBadBoy = new GameClasses.AirBadBoy(airBadBoyImg, redHp, greenHp, (short) 912, (short) 16, new Rectangle(912, 16, airBadBoyImg.getWidth(), airBadBoyImg.getHeight()), new Circle(912, 16, ((float) airBadBoyImg.getWidth() / 2)));
                airBadBoys.add(airBadBoy);
                airBadBoyCreated = true;
            }
            timeSinceLastBadBoy = 0;
        }
        else if (airBadBoyCreated && !isBossCreated) {
            if (timeSinceLastAirBadBoy >= airRespawnTime) {
                GameClasses.AirBadBoy airBadBoy = new GameClasses.AirBadBoy(airBadBoyImg, redHp, greenHp, (short) 912, (short) 16, new Rectangle(912, 16, airBadBoyImg.getWidth(), airBadBoyImg.getHeight()), new Circle(912, 16, ((float) airBadBoyImg.getWidth() / 2)));
                airBadBoys.add(airBadBoy);
                timeSinceLastAirBadBoy = 0;
            }
        }

        // Update and draw bad boys
        Iterator<GameClasses.BadBoy> iter = badBoysArray.iterator();
        while (iter.hasNext()) {
            GameClasses.BadBoy badBoy = iter.next();
            badBoy.update();
            badBoy.draw(batch);
            if (badBoy.isActive()) {
                iter.remove();
            }
        }

        // Update and draw air bad boys
        Iterator<GameClasses.AirBadBoy> iter2 = airBadBoys.iterator();
        while (iter2.hasNext()) {
            GameClasses.AirBadBoy airBadBoy = iter2.next();
            airBadBoy.update();
            airBadBoy.draw(batch);
            if (airBadBoy.isActive()) {
                iter2.remove();
            }
        }

        for (GameClasses.BadBoy badBoy : badBoysArray){
            debugAbles.add(badBoy);
        }
        for (GameClasses.AirBadBoy AirBadBoy : airBadBoys){
            debugAbles.add(AirBadBoy);
        }
    }

    // Spawn a bullet targeting the nearest bad boy
    private void spawnBullet(float startX, float startY, GameClasses.BadBoy nearest, byte damage) {
        float targetX = nearest.getX();
        float targetY = 16; // Target Y position of bad boys
        float deltaX = targetX - startX;
        float deltaY = targetY - startY;
        float distance = (float) Math.sqrt(deltaX * deltaX + deltaY * deltaY);
        float speed = 200; // Bullet speed
        float speedX = deltaX / distance * speed;
        float speedY = deltaY / distance * speed;

        bullets.add(new GameClasses.Bullet(bltImg, startX, startY, speedX, speedY, badBoysArray, damage));
    }

    // Spawn a bullet targeting the nearest air bad boy
    private void spawnAirBullet(float startX, float startY, GameClasses.AirBadBoy nearest) {
        float targetX = nearest.getX();
        float targetY = 16; // Target Y position of air bad boys
        float deltaX = targetX - startX;
        float deltaY = targetY - startY;
        float distance = (float) Math.sqrt(deltaX * deltaX + deltaY * deltaY);
        float speed = 200; // Bullet speed
        float speedX = deltaX / distance * speed;
        float speedY = deltaY / distance * speed;

        AAbullets.add(new GameClasses.AABullet(AAbltImg, startX, startY, speedX, speedY, airBadBoys));
    }

    // Check if a gun overlaps with a target's range
    private boolean overlaps(float x, float y, float cx, float radius) {
        float dx = x - cx;
        float dy = y - 16;
        float distance = dx * dx + dy * dy;
        float radiusSum = radius + 64;
        return distance < radiusSum * radiusSum;
    }

    // Enable debug mode for all objects
    public void enableDebugMode() {
        for (GameClasses.debugAble debugAble : debugAbles) {
            debugAble.enableDebugMode();
        }
    }

    // Enable debug mode for all objects
    public void disableDebugMode() {
        for (GameClasses.debugAble debugAble : debugAbles) {
            debugAble.disableDebugMode();
        }
    }
}
