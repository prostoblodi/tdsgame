package com.tds.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;

import java.util.Iterator;

public class updateAndDrawBulletsAndBadBoys {
    private float timeSinceLastBullet, timeSinceLastBadBoy, timeSinceLastAirBadBoy;

    float airRespawnTime = 5, respawnTime = 3;

    short badBoysCounter;

    private TextureRegion textureRegion;
    private Texture airBadBoyImg, redHp, AAbltImg, bltImg, badBoysImg;

    long startTime = TimeUtils.nanoTime();

    boolean airBadBoyCreated, isBossCreated = false;

    Batch batch;

    private final Array<GameClasses.Bullet> bullets = new Array<>();
    private final Array<GameClasses.AABullet> AAbullets = new Array<>();
    private final Array<GameClasses.BadBoy> badBoysArray = new Array<>();
    private final Array<GameClasses.AirBadBoy> airBadBoys = new Array<>();
    private final Array<GameClasses.Gun> guns;

    GameClasses.Boss boss = null;

    Game game;
    AssetManager assetManager;

    public updateAndDrawBulletsAndBadBoys(Batch batch, Array<GameClasses.Gun> guns, Game game, AssetManager assetManager, short badBoysCounter) {
        this.batch = batch;
        this.guns = guns;
        this.assetManager = assetManager;
        this.game = game;
        this.badBoysCounter = badBoysCounter;
        getTextures(assetManager);
    }

   private void getTextures(AssetManager assetManager){
       assetManager.load("airBadBoy.png", Texture.class);
       assetManager.load("badBoys.png", Texture.class);
       assetManager.load("blt.png", Texture.class);
       assetManager.load("AAblt.png", Texture.class);
       assetManager.load("Destroyer.png", Texture.class);

       airBadBoyImg = assetManager.get("airBadBoy.png", Texture.class);
       badBoysImg = assetManager.get("badBoys.png", Texture.class);
       bltImg = assetManager.get("blt.png", Texture.class);
       AAbltImg = assetManager.get("AAblt.png", Texture.class);
       redHp = assetManager.get("redHp.png", Texture.class);
       Texture greenHp = assetManager.get("greenHp.png", Texture.class);

       textureRegion = new TextureRegion(greenHp);
       System.out.println("}-- textures has been get");
    }


    public void updateAndDrawBullets(float delta) {
        GameClasses.BadBoy nearest;
        GameClasses.AirBadBoy nearestAir;
        timeSinceLastBullet += delta;

        if (timeSinceLastBullet >= 1.0f) {
            for (GameClasses.Gun gun : guns) {

                nearest = gun.findNearestBadBoy(badBoysArray);
                nearestAir = gun.findNearestAirBadBoyX(airBadBoys);

                if(gun.isGunCreated() && gun.getGunLevel() == 1) {
                    if (nearest != null) {
                        if (overlaps(gun.getX(), gun.getY(), nearest.getX(), 500)) {
                            spawnBullet(gun.getX(), gun.getY(), nearest, (byte) 7);
                        }
                    }
                    else if(boss != null){
                        if (overlaps(gun.getX(), gun.getY(), boss.x, 500)){
                            spawnBossBullet(gun.getX(), gun.getY(), boss);
                        }
                    }
                }
                else if(gun.getGunLevel() == 2){
                    if(nearestAir != null){
                        if(overlaps(gun.getX(), gun.getY(), nearestAir.getX(), 700)) {
                            spawnAirBullet(gun.getX(), gun.getY(), nearestAir);
                        }
                    }
                }
                else if(gun.getGunLevel() == 3){
                    if(nearest != null){
                        if(overlaps(gun.getX(), gun.getY(), nearest.getX(), 800)){
                            spawnBullet(gun.getX(), gun.getY(), nearest, (byte) 14);
                        }
                    }
                    else if(boss != null){
                        if (overlaps(gun.getX(), gun.getY(), boss.x, 500)){
                            spawnBossBullet(gun.getX(), gun.getY(), boss);
                        }
                    }
                }
                else if(gun.getGunLevel() == 4){
                    if(nearest != null){
                        if(overlaps(gun.getX(), gun.getY(), nearest.getX(), 800)){
                            spawnBullet(gun.getX(), gun.getY(), nearest, (byte) 35);
                        }
                    }
                    else if(boss != null){
                        if (overlaps(gun.getX(), gun.getY(), boss.x, 800)){
                            spawnBossBullet(gun.getX(), gun.getY(), boss);
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

    public void updateAndDrawBadBoys(float delta) {
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

        if(boss != null){
            if(boss.x <= 16){
                game.setScreen(new AfterBossScreen());
            }
        }

        if (timeSinceLastBadBoy >= respawnTime && !isBossCreated) {
            GameClasses.BadBoy badBoy = new GameClasses().new BadBoy(badBoysImg, redHp, textureRegion, (short) 912, new Rectangle(912, 16, badBoysImg.getWidth(), badBoysImg.getHeight()), new Circle(912, 16, (float) badBoysImg.getWidth() / 2));
            badBoysArray.add(badBoy);
            badBoysCounter++;

            System.out.println("}-- " + badBoysCounter + " bad boy has been created");

            if(badBoysCounter >= 10 && !airBadBoyCreated){
                GameClasses.AirBadBoy airBadBoy = new GameClasses().new AirBadBoy(airBadBoyImg, redHp, textureRegion, (short) 912, new Rectangle(912,16, airBadBoyImg.getWidth(), airBadBoyImg.getHeight()), new Circle(912, 16, (airBadBoyImg.getWidth() / 2)));
                airBadBoys.add(airBadBoy);
                System.out.println("}-- first air bad boy has been created");
                airBadBoyCreated = true;
            }
            timeSinceLastBadBoy = 0;
        }
        else if(airBadBoyCreated && !isBossCreated){
            if(timeSinceLastAirBadBoy >= airRespawnTime){
                GameClasses.AirBadBoy airBadBoy = new GameClasses().new AirBadBoy(airBadBoyImg, redHp, textureRegion, (short) 912, new Rectangle(912,16, airBadBoyImg.getWidth(), airBadBoyImg.getHeight()), new Circle(912, 16, (airBadBoyImg.getWidth() / 2)));
                airBadBoys.add(airBadBoy);
                System.out.println("}-- not first air bad boy has been created");
                timeSinceLastAirBadBoy = 0;
            }
        }

        if(badBoysCounter >= 40 && !isBossCreated){
            boss = new GameClasses().new Boss(assetManager.get("Destroyer.png", Texture.class), (short) 912);
            isBossCreated = true;
            System.out.println("}-- Boss has been created");
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

        if(boss != null) {
            boss.update();
            boss.draw(batch);
            System.out.println("}-- Boss has been updated");
        }
    }

    private void spawnBullet(float startX, float startY, GameClasses.BadBoy nearest, byte damage) {
        float targetX = nearest.getX();
        float targetY = 16; // Target Y position of bad boys
        float deltaX = targetX - startX;
        float deltaY = targetY - startY;
        float distance = (float) Math.sqrt(deltaX * deltaX + deltaY * deltaY);
        float speed = 200; // Bullet speed
        float speedX = deltaX / distance * speed;
        float speedY = deltaY / distance * speed;

        bullets.add(new GameClasses().new Bullet(bltImg, startX, startY, speedX, speedY, badBoysArray, damage, boss));

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

        AAbullets.add(new GameClasses().new AABullet(AAbltImg, startX, startY, speedX, speedY, airBadBoys));
    }

    private void spawnBossBullet(float startX, float startY, GameClasses.Boss boss){
        float targetX = boss.x;
        float targetY = 16; // Target Y position of bad boys
        float deltaX = targetX - startX;
        float deltaY = targetY - startY;
        float distance = (float) Math.sqrt(deltaX * deltaX + deltaY * deltaY);
        float speed = 200; // Bullet speed
        float speedX = deltaX / distance * speed;
        float speedY = deltaY / distance * speed;

        bullets.add(new GameClasses().new Bullet(bltImg, startX, startY, speedX, speedY, badBoysArray, (byte) 7, boss));
    }

    private boolean overlaps(float x, float y, float cx, float radius) {
        float dx = x - cx;
        float dy = y - (float) 16;
        float distance = dx * dx + dy * dy;
        float radiusSum = radius + (float) 64;
        return distance < radiusSum * radiusSum;
    }

}
