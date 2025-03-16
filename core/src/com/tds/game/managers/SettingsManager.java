package com.tds.game.managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;

public class SettingsManager {

    private static final FileHandle saveFile = Gdx.files.local("settings.stn"); // Файл для сохранения
    private static final Json json = new Json(); // Используем библиотеку Json для сериализации/десериализации

    public void saveSettings(boolean isFullscreen) {
        String jsonData = json.toJson(isFullscreen);
        saveFile.writeString(jsonData, false); // false — перезаписать файл
    }

    public boolean loadSettings() {
        if (saveFile.exists()) {
            String jsonData = saveFile.readString();
            return json.fromJson(boolean.class, jsonData);
        }
        return false; // Если нет сохранения, возвращаем новый объект
    }

    public void enableOrNotFullscreen(boolean isSettingsScreen){
        if (loadSettings()){
            setFullscreen();
        }
        else {
            setWindowed(isSettingsScreen);
        }
    }

    public void setFullscreen() {
        Graphics.DisplayMode currentMode = Gdx.graphics.getDisplayMode();  // Get current display mode
        Gdx.graphics.setFullscreenMode(currentMode);  // Set the screen to fullscreen
        System.out.println("Set resolution to: fullscreen" + "(" + Gdx.graphics.getWidth() + "x" + Gdx.graphics.getHeight() + ")");
        saveSettings(true);
    }

    public void setWindowed(boolean isSettingsScreen) {
        Graphics.DisplayMode currentMode = Gdx.graphics.getDisplayMode();  // Get current display mode
        if(!isSettingsScreen) {
            Gdx.graphics.setFullscreenMode(currentMode);
            Gdx.graphics.setWindowedMode(Gdx.graphics.getWidth(), (Gdx.graphics.getHeight() + 40));
            System.out.println("Set resolution to: " + Gdx.graphics.getWidth() + "x" + (Gdx.graphics.getHeight() + 40));
        }
        else {
            Gdx.graphics.setFullscreenMode(currentMode);
            Gdx.graphics.setWindowedMode(Gdx.graphics.getWidth(), Gdx.graphics.getHeight() - 30);
            System.out.println("Set resolution to: " + Gdx.graphics.getWidth() + "x" + (Gdx.graphics.getHeight() - 30) + "(-30)");
        }

        saveSettings(false);
    }
}
