package com.tds.game.level2;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

public class TextAnimator implements Screen {

    private Game game;
    private AssetManager assetManager;

    private OrthographicCamera camera;
    private SpriteBatch batch;
    private BitmapFont font;
    private String fullText = "End? No. Begin"; // Полный текст
    private StringBuilder displayedText = new StringBuilder(); // Текст для отображения

    private float timeElapsed;
    private float charInterval = 0.1f;// Интервал появления символов в секундах

    private int currentCharIndex = 0;

    private boolean isSwitched;


    public TextAnimator(Game game, AssetManager assetManager) {
        this.game = game;
        this.assetManager = assetManager;
        this.isSwitched = true;

        /*Initialises the generator using the file location given.*/
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.local("font.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter params = new FreeTypeFontGenerator.FreeTypeFontParameter();

        /*Sets the parameters of the object constant for the font, regardless of size.*/
        params.borderColor = Color.BLACK;
        params.characters = FreeTypeFontGenerator.DEFAULT_CHARS;
        params.genMipMaps = true;
        params.size = 70;

        /*Generates the font using the generator object.*/
        font = generator.generateFont(params);

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 600);

        batch = new SpriteBatch();
    }

    @Override
    public void show() {
        // Любая логика, которую нужно выполнить при показе экрана
        System.out.println("TextAnimator show called");
    }

    @Override
    public void render(float delta) {
        System.out.println("TextAnimator render called");
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        batch.setProjectionMatrix(camera.combined);

        timeElapsed += Gdx.graphics.getDeltaTime();

        if (timeElapsed > charInterval && currentCharIndex < fullText.length()) {
            displayedText.append(fullText.charAt(currentCharIndex));
            currentCharIndex++;
            timeElapsed = 0;
        }
        else if(currentCharIndex == fullText.length()){
            game.setScreen(new AfterBossScreen(game, assetManager, isSwitched));
        }

        batch.begin();
        drawText(batch, displayedText.toString());
        batch.end();
    }

    private void drawText(SpriteBatch batch, String text) {
        String[] lines = text.split(" ");
        float y = 400; // Начальная Y координата
        for (String line : lines) {
            font.draw(batch, line, 100, y); // Рисует строку по координатам (100, y)
            y -= 80; // Сдвиг Y координаты для следующей строки
        }
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
