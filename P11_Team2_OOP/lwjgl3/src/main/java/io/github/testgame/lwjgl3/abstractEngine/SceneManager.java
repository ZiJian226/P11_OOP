package io.github.testgame.lwjgl3.abstractEngine;

import com.badlogic.gdx.Gdx;
import io.github.testgame.lwjgl3.scene.Scene;
import io.github.testgame.lwjgl3.scene.sceneHelper.SceneType;
import java.util.HashMap;
import java.util.Map;

public class SceneManager {
    private SceneType currentScene;
    private Map<SceneType, Scene> scenes;

    public SceneManager() {
        this.currentScene = SceneType.MAIN_MENU;
        this.scenes = new HashMap<>();
    }

    public void addScene(SceneType type, Scene scene) {
        scenes.put(type, scene);
    }

    public void changeScene(SceneType newScene) {
        if (scenes.containsKey(newScene)) {
            Scene nextScene = scenes.get(newScene);
            nextScene.reload();
            currentScene = newScene;
            Gdx.input.setInputProcessor(scenes.get(newScene).getStage());
        }
    }

    public SceneType getCurrentScene() {
        return currentScene;
    }

    public Scene getCurrentSceneObject() {
        return scenes.get(currentScene);
    }

    public boolean isCurrentScene(SceneType sceneType) {
        return currentScene == sceneType;
    }

    public void resize(int width, int height) {
        for (Scene scene : scenes.values()) {
            scene.resize(width, height);
        }
    }
}
