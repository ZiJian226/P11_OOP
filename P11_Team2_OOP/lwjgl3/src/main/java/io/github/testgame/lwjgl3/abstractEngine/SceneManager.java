package io.github.testgame.lwjgl3.abstractEngine;

import io.github.testgame.lwjgl3.scene.SceneType;

public class SceneManager {
    private SceneType currentScene;

    public SceneManager() {
        this.currentScene = SceneType.MAIN_MENU;
    }

    public void changeScene(SceneType newScene) {
        this.currentScene = newScene;
    }

    public SceneType getCurrentScene() {
        return currentScene;
    }
}
