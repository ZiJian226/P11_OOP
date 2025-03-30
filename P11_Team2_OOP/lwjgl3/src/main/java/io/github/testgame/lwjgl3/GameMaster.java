package io.github.testgame.lwjgl3;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import io.github.testgame.lwjgl3.abstractEngine.*;
import io.github.testgame.lwjgl3.scene.*;
import io.github.testgame.lwjgl3.scene.sceneHelper.SceneFactory;
import io.github.testgame.lwjgl3.scene.sceneHelper.SceneInitializer;
import io.github.testgame.lwjgl3.scene.sceneHelper.SceneType;
import io.github.testgame.lwjgl3.scene.sceneHelper.Transition;

public class GameMaster extends ApplicationAdapter {
    private final int width, height;
    private SceneManager sceneManager;
    private AudioManager audioManager;
    private UIManager uiManager;
    private IOManager ioManager;
    private Transition sceneTransition;
    private SceneFactory sceneFactory;
    private SceneInitializer sceneInitializer;

    public GameMaster(int width, int height) {
        this.width = width;
        this.height = height;
    }

    @Override
    public void create() {
        // Initialize core abstract engines
        sceneManager = new SceneManager();
        audioManager = new AudioManager();
        uiManager = new UIManager();
        ioManager = new IOManager();

        // Create scene factory and transition
        sceneFactory = new SceneFactory(sceneManager);
        sceneTransition = new Transition(sceneManager, sceneFactory);

        // Initialize scene system
        sceneInitializer = new SceneInitializer(sceneManager, audioManager,
            uiManager, ioManager,
            sceneTransition, sceneFactory);

        // Register all scene factories
        sceneInitializer.registerAllSceneFactories();

        // Start with main menu scene
        Scene mainMenu = sceneFactory.getScene(SceneType.MAIN_MENU);
        mainMenu.setLifeCycle(true);
        sceneManager.addScene(SceneType.MAIN_MENU, mainMenu);
        sceneManager.changeScene(SceneType.MAIN_MENU);
    }

    @Override
    public void render() {
        // Update transition effect
        sceneTransition.update(Gdx.graphics.getDeltaTime());

        // Render current scene
        sceneManager.getCurrentSceneObject().render();

        // Render transition effect last (on top)
        sceneTransition.render();
    }

    @Override
    public void resize(int width, int height) {
        sceneManager.resize(width, height);
    }

    @Override
    public void dispose() {
        for (SceneType type : SceneType.values()) {
            sceneFactory.disposeScene(type);
        }
        sceneTransition.dispose();
        audioManager.dispose();
    }
}
