package com.tds.game.universal;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
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


    public static class Bullet {
        /** Bullet, that are created by guns, and only hit ground enemies.
         *
         */
        private final Texture texture;

        private float x, y; // Current position of the bullet
        private final float speedX, speedY;

        private final Rectangle hitBox; // Rectangle hitbox for collision detection
        private final BitmapFont font = new BitmapFont(); // Font for debug information

        private boolean active = true; // Whether the bullet is active
        private boolean debugMode = false; // Whether debug mode is enabled

        private final Array<BadBoy> badBoysArray;

        private final byte damage;


        /**
         * Constructor of bullet, that are created by guns, and only hit ground enemies.
         * @param startX The x-coordinate, where the bullet should appear.
         * @param startY The y-coordinate, where the bullet should appear.
         * @param speedX The velocity, at which the bullet will fly along the coordinate x.
         * @param speedY The velocity, at which the bullet will fly along the coordinate y.
         * @param damage The damage, a bullet will do when it comes in contact with an enemy.
         * @param texture Bullet texture.
         * @param badBoysArray List of enemies, that the bullet can shoot at.
         */
        public Bullet(Texture texture, float startX, float startY, float speedX, float speedY, Array<BadBoy> badBoysArray, Byte damage) {
            this.texture = texture;

            this.x = startX;
            this.y = startY;

            this.speedX = speedX;
            this.speedY = speedY;

            this.hitBox = new Rectangle(x, y, texture.getWidth(), texture.getHeight());

            font.setColor(Color.BLACK);

            this.badBoysArray = badBoysArray;

            this.damage = damage;
        }

        // Updates the bullet's position and checks for collisions
        public void update() {
            if (!active) return;

            x += speedX * Gdx.graphics.getDeltaTime();
            y += speedY * Gdx.graphics.getDeltaTime();
            hitBox.setPosition(x, y);

            for (BadBoy badBoy : badBoysArray) {
                if (Intersector.overlaps(hitBox, badBoy.getRectangleHitBox())) {
                    badBoy.takeDamage(damage); // Deal damage to the enemy
                    active = false; // Deactivate the bullet
                    break;
                }
            }

            if (isOutOfScreen()) {
                active = false; // Deactivate the bullet if it's out of bounds
            }
        }

        // Draws the bullet on the screen
        public void draw(Batch batch) {
            if (active) {
                batch.draw(texture, x, y);
                if (debugMode) {
                    font.draw(batch, ("X: " + x + '\n' + "Y: " + y + '\n' + "SpX: " + speedX + "SpY: " + speedY), x + 5, y + 70);
                }
            }
        }

        public boolean isActive() {
            return active;
        }

        public void enableDebugMode() {
            this.debugMode = true;
        }

        // Checks if the bullet is outside the screen boundaries
        private boolean isOutOfScreen() {
            return x > Gdx.graphics.getWidth() || x < 0 || y > Gdx.graphics.getHeight() || y < 0;
        }
    }

    public static class AABullet {
        /** Bullet, that are created by guns, and only hit ground enemies.
         *
         */
        private final Texture texture;

        private float x, y; // Current position of the bullet
        private final float speedX, speedY;

        private final Rectangle hitBox; // Rectangle hitbox for collision detection
        private final BitmapFont font; // Font for debug information

        private boolean active = true; // Whether the bullet is active
        private boolean debugMode = false; // Whether debug mode is enabled

        private final Array<AirBadBoy> airBadBoysArray;

        /**
         * Constructor of bullet, that are created by AA-guns, and only hit air enemies.
         * @param startX The x-coordinate, where the bullet should appear.
         * @param startY The y-coordinate, where the bullet should appear.
         * @param speedX The velocity, at which the bullet will fly along the coordinate x.
         * @param speedY The velocity, at which the bullet will fly along the coordinate y.
         * @param texture Bullet texture.
         * @param airBadBoysArray List of enemies, that the bullet can shoot at.
         */

        public AABullet(Texture texture, float startX, float startY, float speedX, float speedY, Array<AirBadBoy> airBadBoysArray) {
            this.texture = texture;

            this.x = startX;
            this.y = startY;

            this.speedX = speedX;
            this.speedY = speedY;

            this.hitBox = new Rectangle(x, y, texture.getWidth(), texture.getHeight());
            this.font = new BitmapFont();

            this.airBadBoysArray = airBadBoysArray;
        }

        // Updates the bullet's position and checks for collisions
        public void update() {
            if (!active) return;

            x += speedX * Gdx.graphics.getDeltaTime();
            y += speedY * Gdx.graphics.getDeltaTime();

            hitBox.setPosition(x, y);

            for (AirBadBoy airBadBoy : airBadBoysArray) {
                if (Intersector.overlaps(hitBox, airBadBoy.getRectangleHitBox())) {
                    airBadBoy.takeDamage(14); // Deal damage to the air enemy
                    active = false; // Deactivate the bullet
                    break;
                }
            }

            if (isOutOfScreen()) {
                active = false; // Deactivate the bullet if it's out of bounds
            }
        }

        // Draws the bullet on the screen
        public void draw(Batch batch) {
            if (active) {
                batch.draw(texture, x, y);
                if (debugMode) {
                    font.draw(batch, ("X: " + x + '\n' + "Y: " + y + '\n' + "SpX: " + speedX + "SpY: " + speedY), 0, 0);
                }
            }
        }

        public boolean isActive() {
            return active;
        }

        public void enableDebugMode() {
            this.debugMode = true;
        }

        // Checks if the bullet is outside the screen boundaries
        private boolean isOutOfScreen() {
            return x > Gdx.graphics.getWidth() || x < 0 || y > Gdx.graphics.getHeight() || y < 0;
        }
    }

    public static class BadBoy {
        /** Ground enemies
         *
         */
        private final Texture texture, redHp; // Textures for the enemy and its health bar background
        private final TextureRegion greenHp; // Texture region for the health bar foreground

        private short x; // Current x-coordinate of the enemy
        private final short y; // y-coordinate of the enemy (constant)

        private final Rectangle rectangleHitBox; // Rectangle hitbox for collision detection
        private final Circle circleHitBox; // Circle hitbox for additional collision detection
        private final BitmapFont font = new BitmapFont(); // Font for debug information

        private boolean active = true; // Whether the enemy is active
        private boolean debugMode = false; // Whether debug mode is enabled

        private byte badBoyHP = 100; // Health of the enemy
        private float badBoyHpPercent = 1; // Percentage of health remaining

        /**
         * Ground enemies
         * @param texture Enemy texture.
         * @param circleHitBox Circle hitbox(needed for cannons to realize that the enemy is in their range).
         * @param greenHp Texture for the green hp bar(will be removed in the future)
         * @param x The x-coordinate, where the enemy should appear.
         * @param y The y-coordinate, where the enemy should appear.
         * @param redHp Texture for the red hp bar(will be removed in the future)
         * @param rectangleHitBox Rectangle hitbox(needed for bullets to realize they hit an enemy)
         */

        public BadBoy(Texture texture, Texture redHp, TextureRegion greenHp, short x, short y, Rectangle rectangleHitBox, Circle circleHitBox) {
            this.texture = texture;

            this.redHp = redHp;
            this.greenHp = greenHp;

            this.x = x;
            this.y = y;

            font.setColor(Color.BLACK);

            this.rectangleHitBox = rectangleHitBox;
            this.circleHitBox = circleHitBox;
        }

        public void update() {
            x--;
            rectangleHitBox.setX(x);
            circleHitBox.setX(x);

            if (badBoyHP <= 0) {
                active = false; // Deactivate the enemy if its health reaches 0
            }
        }

        public void draw(Batch batch) {
            batch.draw(texture, x, y);
            batch.draw(redHp, x - 44, y + 96); // Draw the red (background) health bar

            greenHp.setRegionWidth((int) (185 * badBoyHpPercent)); // Adjust the green (foreground) health bar
            batch.draw(greenHp, x - 44, y + 96, greenHp.getRegionWidth(), greenHp.getRegionHeight());

            if (debugMode) {
                font.draw(batch, ("X: " + x + '\n' + "Y: " + y + '\n' + "HP: " + badBoyHP + '\n' + "HPP: " + badBoyHpPercent), x + 1, y + 70);
            }
        }

        public boolean isActive() {
            return !active;
        }

        public void enableDebugMode() {
            this.debugMode = true;
        }

        public Rectangle getRectangleHitBox() {
            return rectangleHitBox;
        }

        public Circle getCircleHitBox() {
            return circleHitBox;
        }

        public void takeDamage(byte damage) {
            badBoyHP -= damage; // Reduce health by the damage amount
            badBoyHpPercent = (float) badBoyHP / 100; // Update health percentage
        }

        public short getX() {
            return x;
        }

    }


    public static class AirBadBoy {
        private final Texture texture, redHp; // Textures for the enemy and its health bar background
        private final TextureRegion greenHp; // Texture region for the health bar foreground

        private short x; // Current x-coordinate of the enemy
        private final short y; // y-coordinate of the enemy (constant)

        private final Rectangle rectangleHitBox; // Rectangle hitbox for collision detection
        private final Circle circleHitBox; // Circle hitbox for additional collision detection
        private final BitmapFont font = new BitmapFont(); // Font for debug information

        private boolean active = true; // Whether the enemy is active
        private boolean debugMode = false; // Whether debug mode is enabled

        private short AirBadBoyHP = 100; // Health of the enemy
        private float AirBadBoyHpPercent = 1; // Percentage of health remaining

        public AirBadBoy(Texture texture, Texture redHp, TextureRegion greenHp, short x, short y, Rectangle rectangleHitBox, Circle circleHitBox) {
            this.texture = texture;

            this.redHp = redHp;
            this.greenHp = greenHp;

            this.x = x;
            this.y = y;

            this.rectangleHitBox = rectangleHitBox;
            this.circleHitBox = circleHitBox;
        }

        // Updates the enemy's position and checks if it's still active
        public void update() {
            x--;
            rectangleHitBox.setX(x);
            circleHitBox.setX(x);

            if (AirBadBoyHP <= 0) {
                active = false; // Deactivate the enemy if its health reaches 0
            }
        }

        // Draws the enemy and its health bar on the screen
        public void draw(Batch batch) {
            batch.draw(texture, x, y);
            batch.draw(redHp, x - 44, y + 96);

            greenHp.setRegionWidth((int) (185 * AirBadBoyHpPercent));
            batch.draw(greenHp, x - 44, y + 96, greenHp.getRegionWidth(), greenHp.getRegionHeight());

            if (debugMode) {
                font.draw(batch, ("X: " + x + '\n' + "Y: " + y + '\n' + "HP: " + AirBadBoyHP + '\n' + "HPP: " + AirBadBoyHpPercent), x + 5, y + 70);
            }
        }

        public boolean isActive() {
            return !active;
        }

        public void enableDebugMode() {
            this.debugMode = true;
        }

        public Rectangle getRectangleHitBox() {
            return rectangleHitBox;
        }

        public Circle getCircleHitBox() {
            return circleHitBox;
        }

        public void takeDamage(int damage) {
            AirBadBoyHP -= (byte) damage;
            AirBadBoyHpPercent = (float) AirBadBoyHP / 100;
        }

        public short getX() {
            return x;
        }

    }

    public static class Gun {

        private final Texture gunTexture, gun2Texture, gun3Texture, gun4Texture; // Textures for the gun's various levels

        public final short x; // X-coordinate of the gun's position
        public final short y; // Y-coordinate of the gun's position

        private byte gunLevel = 0; // Current level of the gun
        private boolean isGunCreated = false; // Flag to indicate if the gun has been created

        private final TextButton button2; // Button for upgrading the gun

        // Constructor initializes the gun with textures, position, and adds buttons to the stage
        public Gun(Texture gunTexture, Texture gun2Texture, Texture gun3Texture, Texture gun4Texture, Skin buttonSkin, short x, short y, Stage stage) {

            // Assign textures for different gun levels
            this.gunTexture = gunTexture;
            this.gun2Texture = gun2Texture;
            this.gun3Texture = gun3Texture;
            this.gun4Texture = gun4Texture;

            this.x = x;
            this.y = y;

            // Create and configure the button for creating the gun
            TextButton button = new TextButton("", buttonSkin, "gunButtonStyle");
            button.setPosition(x, y);
            button.setSize(128, 128);

            // Create and configure the button for upgrading the gun
            TextButton button2 = new TextButton("", buttonSkin, "updatedGunStyle");
            button2.setPosition(x + 16, y + 128);
            button2.setSize(96, 96);

            this.button2 = button2;

            // Add the gun creation button to the stage
            stage.addActor(button);

            // Listener for the gun creation button
            button.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    isGunCreated = true; // Mark the gun as created
                    gunLevel = 1; // Set gun level to 1
                    button.remove(); // Remove the creation button from the stage
                    System.out.println("} Gun spawn button has been clicked");
                }
            });

            // Listener for the gun upgrade button
            button2.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    gunLevel++; // Increment the gun level
                    button2.remove(); // Remove the upgrade button from the stage
                }
            });

            System.out.println("} Gun has been created");
        }

        // Draws the gun and manages its upgrades based on game state
        public void draw(Batch batch, Stage stage, float delta, short badBoysCounter) {
            if (gunLevel == 1) {
                batch.begin();
                batch.draw(gunTexture, x, y); // Draw gun texture for level 1
                batch.end();

                // Add upgrade button if the condition is met
                if (badBoysCounter >= 5) {
                    stage.addActor(button2);
                }

                // Update and render stage elements
                stage.act(delta);
                stage.draw();
            }
            else if (gunLevel == 2) {
                batch.begin();
                batch.draw(gun2Texture, x, y); // Draw gun texture for level 2
                batch.end();

                // Add upgrade button if the condition is met
                if (badBoysCounter >= 10) {
                    stage.addActor(button2);
                }
            }
            else if (gunLevel == 3) {
                batch.begin();
                batch.draw(gun3Texture, x, y); // Draw gun texture for level 3
                batch.end();

                // Add upgrade button if the condition is met
                if (badBoysCounter >= 22) {
                    stage.addActor(button2);
                }
            }
            else if (gunLevel == 4) {
                batch.begin();
                batch.draw(gun4Texture, x, y); // Draw gun texture for level 4
                batch.end();
            }
            else {
                stage.act(delta);
                stage.draw(); // Update and render stage elements
            }
        }

        // Finds the nearest "BadBoy" enemy unit to the gun
        public BadBoy findNearestBadBoy(Array<BadBoy> badBoysArray) {
            BadBoy nearestBadBoy = null;
            float minDistance = Float.MAX_VALUE;

            for (BadBoy badBoy : badBoysArray) {
                if (badBoy.isActive()) continue; // Skip active enemies

                // Calculate the distance to the enemy
                float distance = (float) Math.sqrt(Math.pow(badBoy.getX() - x, 2) + Math.pow(badBoy.getCircleHitBox().y - y, 2));
                if (distance < minDistance) {
                    minDistance = distance; // Update the minimum distance
                    nearestBadBoy = badBoy; // Update the nearest enemy
                }
            }

            assert nearestBadBoy != null; // Ensure a valid enemy is found
            return nearestBadBoy;
        }

        // Finds the nearest air enemy unit to the gun
        public AirBadBoy findNearestAirBadBoyX(Array<AirBadBoy> AirBadBoysArray) {
            AirBadBoy nearestBadBoy = null;
            float minDistance = Float.MAX_VALUE;

            for (AirBadBoy airBadBoy : AirBadBoysArray) {
                if (airBadBoy.isActive()) continue; // Skip active air enemies

                // Calculate the distance to the air enemy
                float distance = (float) Math.sqrt(Math.pow(airBadBoy.getX() - x, 2) + Math.pow(airBadBoy.getCircleHitBox().y - y, 2));
                if (distance < minDistance) {
                    minDistance = distance; // Update the minimum distance
                    nearestBadBoy = airBadBoy; // Update the nearest air enemy
                }
            }

            assert nearestBadBoy != null; // Ensure a valid air enemy is found
            return nearestBadBoy;
        }

        // Returns whether the gun has been created
        public boolean isGunCreated() {
            return isGunCreated;
        }

        // Returns the current level of the gun
        public byte getGunLevel() {
            return gunLevel;
        }
    }
}
