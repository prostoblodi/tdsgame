package com.tds.game.level1;

import com.badlogic.gdx.*;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
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

    /** Handles updating and drawing bullets and bad boys (enemies) */
    private updateAndDrawBulletsAndBadBoys update;

    /** Main game variable to switch between screens */
    private final Game game;

    /** Asset manager for loading and accessing textures and other assets */
    private final AssetManager assetManager;

    /** Used for rendering textures */
    private SpriteBatch batch;

    /** Stage for rendering UI elements like buttons */
    private Stage stage;

    /** Skin for styling UI elements */
    public Skin skin;

    /** Textures for different UI elements and backgrounds */
    private Texture endImg, roadImg, startImg, menuUp, menuDown, menuO;

    /** BitmapFont for rendering text */
    private BitmapFont font;

    /** Style for the menu button */
    private final TextButton.TextButtonStyle menuStyle = new TextButton.TextButtonStyle();

    /** Array to store guns */
    private final Array<GameClasses.Gun> guns = new Array<>();

    /** Counter for bad boys (enemies) killed */
    public short badBoysCounter = 0;

    /** Is debug mode enabled */
    private boolean isDebugEnabled = false;

    /** Checks memory usage **/
    Runtime runtime = Runtime.getRuntime();

    public MainGameScreen(Game game, AssetManager assetManager) {
        this.game = game;
        this.assetManager = assetManager;
    }

    // Load textures and initialize buttons
    @Override
    public void show() {
        this.batch = new SpriteBatch();
        this.stage = new Stage(new ScreenViewport());

        // Set the input processor for handling UI input events
        Gdx.input.setInputProcessor(stage);

        System.out.println("}- Textures have been loaded");

        // Load background textures
        endImg = assetManager.get("some_decor/end.png", Texture.class);
        roadImg = assetManager.get("some_decor/road.png", Texture.class);
        startImg = assetManager.get("some_decor/start.png", Texture.class);

        // Load gun spawn button textures
        Texture gunButtonUp = assetManager.get("game_buttons/nothing/nothing.png", Texture.class);
        Texture gunButtonDown = assetManager.get("game_buttons/nothing/nothingD.png", Texture.class);
        Texture gunButtonO = assetManager.get("game_buttons/nothing/nothingO.png", Texture.class);

        // Load upgrade gun button textures
        Texture upgradeGun = assetManager.get("game_buttons/upgrade/upgradeGun.png", Texture.class);
        Texture upgradeGunD = assetManager.get("game_buttons/upgrade/upgradeGunD.png", Texture.class);
        Texture upgradeGunO = assetManager.get("game_buttons/upgrade/upgradeGunO.png", Texture.class);

        // Load menu button textures
        menuUp = assetManager.get("game_buttons/menu/menuButton.png", Texture.class);
        menuDown = assetManager.get("game_buttons/menu/menuButtonD.png", Texture.class);
        menuO = assetManager.get("game_buttons/menu/menuButtonO.png", Texture.class);

        // Create a new skin and set up fonts
        skin = new Skin();
        skin.add("default-font", new BitmapFont());

        // Create style for gun spawn buttons
        TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.up = new TextureRegionDrawable(new TextureRegion(gunButtonUp));
        buttonStyle.down = new TextureRegionDrawable(new TextureRegion(gunButtonDown));
        buttonStyle.over = new TextureRegionDrawable(new TextureRegion(gunButtonO));
        buttonStyle.font = skin.getFont("default-font");

        // Create style for upgrade gun buttons
        TextButton.TextButtonStyle button2Style = new TextButton.TextButtonStyle();
        button2Style.up = new TextureRegionDrawable(new TextureRegion(upgradeGun));
        button2Style.down = new TextureRegionDrawable(new TextureRegion(upgradeGunD));
        button2Style.over = new TextureRegionDrawable(new TextureRegion(upgradeGunO));
        button2Style.font = skin.getFont("default-font");

        // Add button styles to the skin
        skin.add("gunButtonStyle", buttonStyle);
        skin.add("updatedGunStyle", button2Style);

        System.out.println("}- Textures have been retrieved");

        setupInitialState();
    }

    // Set up the initial state of variables and objects
    private void setupInitialState() {
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("font.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();

        parameter.size = 20;
        parameter.characters = FreeTypeFontGenerator.DEFAULT_CHARS;

        font = generator.generateFont(parameter);

        // Initialize the update logic
        update = new updateAndDrawBulletsAndBadBoys(batch, guns, game, assetManager, badBoysCounter);

        // Define initial x-coordinate for guns
        short GunXCord = 128;

        // Create 6 guns and add them to the guns array
        for (byte i = 0; i < 6; i++) {
            guns.add(new GameClasses.Gun(
                    assetManager.get("guns/gun.png", Texture.class),
                    assetManager.get("guns/AAGun.png", Texture.class),
                    assetManager.get("guns/2xGun.png", Texture.class),
                    assetManager.get("guns/5xGun.png", Texture.class),
                    assetManager.get("guns/gunturret.jpg", Texture.class),
                    skin,
                    GunXCord,
                    (short) 128,
                    stage
            ));
            GunXCord += 128;
        }

        System.out.println("}- SetupInitialState completed");
        createButtons();
    }

    @Override
    public void render(float delta) {
        // Update the bad boys counter from the update logic
        badBoysCounter = update.badBoysCounter;

        // Clear the screen with black color
        ScreenUtils.clear(0, 0, 0, 1);

        // Update and draw the stage (UI elements)
        stage.act(delta);
        stage.draw();

        // Draw guns
        for (GameClasses.Gun gun : guns) {
            gun.draw(batch, stage, delta, badBoysCounter);
        }

        // Begin rendering batch for non-UI elements
        batch.begin();

        drawTextures();

        // Display the number of bad boys required for upgrade
        if ((byte) ((badBoysCounter - 5) <= 0 ? (5 - badBoysCounter) : 0) != 0) {
            font.draw(batch, "For upgrade you need to kill " + (byte) ((badBoysCounter - 5) <= 0 ? (5 - badBoysCounter) : 0) + " more bad boys", 128, 276);
        }

        // Toggle debug mode
        if (Gdx.input.isKeyPressed(Input.Keys.F1) || isDebugEnabled) {
            font.draw(batch, "Memory in use: " + (runtime.totalMemory() / 1024 / 1024) + " MB", 0, Gdx.graphics.getHeight() - 150);
            update.enableDebugMode();
            isDebugEnabled = !isDebugEnabled;
        }

        // Update and draw bullets and bad boys
        update.updateAndDrawBullets(delta);
        update.updateAndDrawBadBoys(delta);

        batch.end();
    }

    // Draw static textures like roads and the start/end points
    private void drawTextures() {
        for (int i = 0; i < 6; i++) {
            batch.draw(roadImg, 128 + i * 128, 0);
        }
        batch.draw(startImg, 896, 0);
        batch.draw(endImg, 0, 0);
    }

    // Create menu button and set its properties
    public void createButtons() {
        short y = (short) (!Gdx.graphics.isFullscreen() ? 860 : 920);

        menuStyle.up = new TextureRegionDrawable(new TextureRegion(menuUp));
        menuStyle.down = new TextureRegionDrawable(new TextureRegion(menuDown));
        menuStyle.over = new TextureRegionDrawable(new TextureRegion(menuO));
        menuStyle.font = skin.getFont("default-font");

        skin.add("menuButton", menuStyle);

        TextButton menuButton = new TextButton("", menuStyle);

        menuButton.setPosition(0, y);
        menuButton.setSize(160, 160);

        stage.addActor(menuButton);

        // Add a click listener to the menu button to switch screens
        menuButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new MenuScreen(game, assetManager));
            }
        });

        System.out.println("}- Buttons have been created");
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
        // Dispose of all resources
        batch.dispose();
        font.dispose();
        endImg.dispose();
        roadImg.dispose();
        startImg.dispose();
        stage.dispose();
    }
}
