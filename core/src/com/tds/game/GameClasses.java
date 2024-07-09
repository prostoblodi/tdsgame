package com.tds.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;

public class GameClasses {

    public class Bullet {
        private final Texture texture;
        private float x, y;
        private final float speedX, speedY;
        private final Rectangle hitBox;
        private boolean active;
        private final Array<BadBoy> badBoysArray;
        private final byte damage;
        private Boss boss;

        public Bullet(Texture texture, float startX, float startY, float speedX, float speedY, Array<BadBoy> badBoysArray, Byte damage, Boss boss) {
            this.texture = texture;
            this.x = startX;
            this.y = startY;
            this.speedX = speedX;
            this.speedY = speedY;
            this.hitBox = new Rectangle(x, y, texture.getWidth(), texture.getHeight());
            this.active = true;
            this.badBoysArray = badBoysArray;
            this.damage = damage;
            this.boss = boss;
        }

        public void update() {
            if (!active) return;
            x += speedX * Gdx.graphics.getDeltaTime();
            y += speedY * Gdx.graphics.getDeltaTime();
            hitBox.setPosition(x, y);

            for (BadBoy badBoys : badBoysArray) {
                if (Intersector.overlaps(hitBox, badBoys.getRectangleHitBox())) {
                    badBoys.takeDamage(damage);
                    active = false;
                    break;
                }
            }
            if(boss != null){
                if (Intersector.overlaps(hitBox, boss.recHitBox)) {
                    active = false;
                }
            }

            if (isOutOfScreen()) {
                active = false;
            }
        }

        public void draw(Batch batch) {
            if (active) {
                batch.draw(texture, x, y);
            }
        }

        public boolean isActive() {
            return active;
        }

        private boolean isOutOfScreen() {
            return x > Gdx.graphics.getWidth() || x < 0 || y > Gdx.graphics.getHeight() || y < 0;
        }
    }

    public class AABullet {
        private final Texture texture;
        private float x, y;
        private final float speedX, speedY;
        private final Rectangle hitBox;
        private boolean active;
        private final Array<AirBadBoy> airBadBoysArray;

        public AABullet(Texture texture, float startX, float startY, float speedX, float speedY, Array<AirBadBoy> airBadBoysArray) {
            this.texture = texture;
            this.x = startX;
            this.y = startY;
            this.speedX = speedX;
            this.speedY = speedY;
            this.hitBox = new Rectangle(x, y, texture.getWidth(), texture.getHeight());
            this.active = true;
            this.airBadBoysArray = airBadBoysArray;

        }

        public void update() {
            if (!active) return;
            x += speedX * Gdx.graphics.getDeltaTime();
            y += speedY * Gdx.graphics.getDeltaTime();
            hitBox.setPosition(x, y);

            for (AirBadBoy airBadBoy : airBadBoysArray) {
                if (Intersector.overlaps(hitBox, airBadBoy.getRectangleHitBox())) {
                    airBadBoy.takeDamage(14);
                    active = false;
                    break;
                }
            }

            if (isOutOfScreen()) {
                active = false;
            }
        }

        public void draw(Batch batch) {
            if (active) {
                batch.draw(texture, x, y);
            }
        }

        public boolean isActive() {
            return active;
        }

        private boolean isOutOfScreen() {
            return x > Gdx.graphics.getWidth() || x < 0 || y > Gdx.graphics.getHeight() || y < 0;
        }
    }




    public class BadBoy {
        private final Texture texture, redHp;
        private final TextureRegion greenHp;
        private short x;
        private final Rectangle rectangleHitBox;
        private final Circle circleHitBox;
        private boolean active = true;
        private byte badBoysHP = 100;
        private float badBoysHpPercent = 1;

        public BadBoy(Texture texture, Texture redHp, TextureRegion greenHp, short x, Rectangle rectangleHitBox, Circle circleHitBox) {
            this.texture = texture;
            this.redHp = redHp;
            this.greenHp = greenHp;
            this.x = x;
            this.rectangleHitBox = rectangleHitBox;
            this.circleHitBox = circleHitBox;
        }

        public void update() {
            x--;
            rectangleHitBox.setX(x);
            circleHitBox.setX(x);

            if (badBoysHP <= 0) {
                reset();
                active = false;
            }
        }

        public void reset() {
            x = 912;
            rectangleHitBox.setX(x);
            circleHitBox.setX(x);
            badBoysHP = 100;
        }

        public void draw(Batch batch) {
            batch.draw(texture, x, 16);
            batch.draw(redHp, x - 44, 112);
            greenHp.setRegionWidth((int) (185 * badBoysHpPercent));
            batch.draw(greenHp, x - 44, 112, greenHp.getRegionWidth(), greenHp.getRegionHeight());
        }

        public boolean isActive() {
            return active;
        }

        public Rectangle getRectangleHitBox() {
            return rectangleHitBox;
        }

        public Circle getCircleHitBox() {
            return circleHitBox;
        }

        public void takeDamage(byte damage) {
            badBoysHP -= damage;
            badBoysHpPercent = (float) badBoysHP / 100;
        }

        public short getX() {
            return x;
        }
    }


    public class AirBadBoy {
        private final Texture texture, redHp;
        private final TextureRegion greenHp;
        private short x;
        private final Rectangle rectangleHitBox;
        private final Circle circleHitBox;
        private boolean active = true;
        private short AirBadBoyHP = 100;
        private float AirbadBoyHpPercent = 1;


        public AirBadBoy(Texture texture, Texture redHp, TextureRegion greenHp, short x, Rectangle rectangleHitBox, Circle circleHitBox) {
            this.texture = texture;
            this.redHp = redHp;
            this.greenHp = greenHp;
            this.x = x;
            this.rectangleHitBox = rectangleHitBox;
            this.circleHitBox = circleHitBox;
        }

        public void update() {
            x--;
            rectangleHitBox.setX(x);
            circleHitBox.setX(x);

            if (AirBadBoyHP <= 0) {
                reset();
                active = false;
            }
        }

        public void reset() {
            x = 912;
            rectangleHitBox.setX(x);
            circleHitBox.setX(x);
            AirBadBoyHP = 250;
        }

        public void draw(Batch batch) {
            batch.draw(texture, x, 16);
            batch.draw(redHp, x - 44, 112);
            greenHp.setRegionWidth((int) (185 * AirbadBoyHpPercent));
            batch.draw(greenHp, x - 44, 112, greenHp.getRegionWidth(), greenHp.getRegionHeight());
        }

        public boolean isActive() {
            return active;
        }

        public Rectangle getRectangleHitBox() {
            return rectangleHitBox;
        }

        public Circle getCircleHitBox() {
            return circleHitBox;
        }

        public void takeDamage(int damage) {
            AirBadBoyHP -= (byte) damage;
            AirbadBoyHpPercent = (float) AirBadBoyHP / 100;
        }

        public short getX() {
            return x;
        }


    }

    public class Boss{

        Texture texture;
        Short x;
        Circle cirHitBox;
        Rectangle recHitBox;

        public Boss(Texture texture, short x){
            this.texture = texture;
            this.x = x;
            this.cirHitBox = new Circle(912, 16, 64);
            this.recHitBox = new Rectangle(912, 16, 128, 128);

        }

        public void draw(Batch batch){
            batch.draw(texture, x, 16);
        }

        public void update(){
            x--;
            cirHitBox.x--;
            recHitBox.x--;
        }

        private void reset(){
            x = 912;
            cirHitBox.x = 912;
            recHitBox.x = 912;
        }
    }

    public class Gun {

        private final Texture gunTexture, gun2Texture, gun3Texture, gun4Texture;
        private final short x;
        private final short y;
        private byte gunLevel = 0;
        private boolean isGunCreated = false;
        private final TextButton button2;

        public Gun(Texture gunTexture, Texture gun2Texture, Texture gun3Texture, Texture gun4Texture, Skin buttonSkin, short x, short y, Stage stage) {

            this.gunTexture = gunTexture;
            this.gun2Texture = gun2Texture;
            this.gun3Texture = gun3Texture;
            this.gun4Texture = gun4Texture;

            this.x = x;
            this.y = y;

            TextButton button = new TextButton("", buttonSkin, "gunButtonStyle");
            button.setPosition(x,y);
            button.setSize(128, 128);

            TextButton button2 = new TextButton("", buttonSkin, "updatedGunStyle");
            button2.setPosition(x+32,y+128);
            button2.setSize(96, 96);

            this.button2 = button2;

            stage.addActor(button);

            button.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    isGunCreated = true;
                    gunLevel = 1;
                    button.remove();
                }
            });
            button2.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    gunLevel++;
                    button2.remove();
                }
            });
        }

        public void draw(Batch batch, Stage stage, float delta, short badBoysCounter) {
            if (gunLevel == 1) {
                batch.begin();
                batch.draw(gunTexture, x, y);
                batch.end();

                if(badBoysCounter >= 5){
                    stage.addActor(button2);
                }

                stage.act(delta);
                stage.draw();
            }
            else if(gunLevel == 2){
                batch.begin();
                batch.draw(gun2Texture, x, y);
                batch.end();

                if(badBoysCounter >= 10){
                    stage.addActor(button2);
                }
            }
            else if(gunLevel == 3){
                batch.begin();
                batch.draw(gun3Texture, x, y);
                batch.end();

                if(badBoysCounter >= 22){
                    stage.addActor(button2);
                }
            }
            else if(gunLevel == 4){
                batch.begin();
                batch.draw(gun4Texture, x, y);
                batch.end();
            }
            else{
                stage.act(delta);
                stage.draw();
            }
        }

        public BadBoy findNearestBadBoy(Array<BadBoy> badBoysArray) {
            BadBoy nearestBadBoy = null;
            float minDistance = Float.MAX_VALUE;

            for (BadBoy badBoy : badBoysArray) {
                if (!badBoy.isActive()) continue;

                float distance = (float) Math.sqrt(Math.pow(badBoy.getX() - x, 2) + Math.pow(badBoy.getCircleHitBox().y - y, 2));
                if (distance < minDistance) {
                    minDistance = distance;
                    nearestBadBoy = badBoy;
                }
            }

            assert nearestBadBoy != null;
            return nearestBadBoy;
        }

        public AirBadBoy findNearestAirBadBoyX(Array<AirBadBoy> AirBadBoysArray) {
            AirBadBoy nearestBadBoy = null;
            float minDistance = Float.MAX_VALUE;

            for (AirBadBoy airBadBoy : AirBadBoysArray) {
                if (!airBadBoy.isActive()) continue;

                float distance = (float) Math.sqrt(Math.pow(airBadBoy.getX() - x, 2) + Math.pow(airBadBoy.getCircleHitBox().y - y, 2));
                if (distance < minDistance) {
                    minDistance = distance;
                    nearestBadBoy = airBadBoy;
                }
            }

            assert nearestBadBoy != null;
            return nearestBadBoy;
        }

        public boolean isGunCreated() {
            return isGunCreated;
        }

        public short getX(){
            return x;
        }

        public short getY(){
            return y;
        }

        public byte getGunLevel() {
            return gunLevel;
        }
    }
}
