package io.github.testgame.lwjgl3.scene.sceneHelper;

import io.github.testgame.lwjgl3.abstractEngine.SceneManager;
import io.github.testgame.lwjgl3.scene.Scene;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class SceneFactory {
    private SceneManager sceneManager;
    private Map<SceneType, Supplier<Scene>> factories;
    private Map<SceneType, Scene> cachedScenes;

    public SceneFactory(SceneManager sceneManager) {
        this.sceneManager = sceneManager;
        this.factories = new HashMap<>();
        this.cachedScenes = new HashMap<>();
    }

    public void registerFactory(SceneType type, Supplier<Scene> factory) {
        factories.put(type, factory);
    }

    public Scene getScene(SceneType type) {
        // Return cached scene if it exists and is in lifecycle
        if (cachedScenes.containsKey(type) && cachedScenes.get(type).getLifeCycle()) {
            return cachedScenes.get(type);
        }

        // Create new scene if we have a factory for it
        if (factories.containsKey(type)) {
            Scene scene = factories.get(type).get();
            cachedScenes.put(type, scene);
            return scene;
        }

        return null;
    }

    public boolean disposeScene(SceneType type) {
        Scene scene = cachedScenes.remove(type);
        if (scene != null) {
            scene.dispose();
            return true;
        }
        return false;
    }
}
