package io.github.testgame.lwjgl3;

import com.badlogic.gdx.Gdx;
import io.github.testgame.lwjgl3.scene.*;
import io.github.testgame.lwjgl3.abstractEngine.*;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.Map;
import java.util.function.Supplier;

public class GameMaster extends ApplicationAdapter {
    private final int width, height;
    private SceneManager sceneManager;
    private AudioManager audioManager;
    private UIManager uiManager;
    private IOManager ioManager;
    private SpriteBatch batch;
    private Transition sceneTransition;
    private SceneFactory sceneFactory;

    // Scene factories for lazy initialization
    private Map<SceneType, Supplier<Scene>> sceneFactories;

    public GameMaster(int width, int height) {
        this.width = width;
        this.height = height;
    }

    @Override
    public void create() {
        batch = new SpriteBatch();
        sceneManager = new SceneManager();
        uiManager = new UIManager();
        audioManager = new AudioManager();
        ioManager = new IOManager();

        // Create scene factory and register factories
        sceneFactory = new SceneFactory(sceneManager);

        // Register scene factories for lazy initialization
        sceneFactory.registerFactory(SceneType.MAIN_MENU, this::createMainMenuScene);
        sceneFactory.registerFactory(SceneType.GAME, this::createGameScene);
        sceneFactory.registerFactory(SceneType.FAIL, this::createFailScene);
        sceneFactory.registerFactory(SceneType.VICTORY, this::createVictoryScene);
        sceneFactory.registerFactory(SceneType.INSTRUCTIONS, this::createInstructionsScene);

        // Create transition with scene factory
        sceneTransition = new Transition(sceneManager, sceneFactory);

        // Only initialize MainMenu at start
        Scene mainMenu = sceneFactory.getScene(SceneType.MAIN_MENU);
        mainMenu.setLifeCycle(true);
        sceneManager.addScene(SceneType.MAIN_MENU, mainMenu);
        sceneManager.changeScene(SceneType.MAIN_MENU);
    }

    private Scene createMainMenuScene() {
        GameScene gameScene = (GameScene) createGameScene();
        Scene mainMenu = new MainMenu(sceneManager, uiManager, audioManager, sceneTransition, gameScene, sceneFactory);
        mainMenu.create();
        return mainMenu;
    }

    private Scene createGameScene() {
        Scene gameScene = new GameScene(sceneManager, audioManager, ioManager, sceneTransition);
        gameScene.create();
        return gameScene;
    }

    private Scene createFailScene() {
        Scene failScene = new FailScene(sceneManager, audioManager, ioManager, sceneTransition, sceneFactory);
        failScene.create();
        return failScene;
    }

    private Scene createVictoryScene() {
        Scene victoryScene = new VictoryScene(sceneManager, audioManager, ioManager, sceneTransition, sceneFactory);
        victoryScene.create();
        return victoryScene;
    }

    private Scene createInstructionsScene() {
        Scene instructionsScene = new GameInstructionsScene(sceneManager, audioManager, ioManager, sceneTransition, sceneFactory);
        instructionsScene.create();
        return instructionsScene;
    }

    @Override
    public void render() {
        float delta = Gdx.graphics.getDeltaTime();
        Scene currentScene = sceneManager.getCurrentSceneObject();

        // Update and render current scene
        if (currentScene != null) {
            currentScene.render();
        }

        // Update and render transition effect if active
        sceneTransition.update(delta);
        sceneTransition.render();
    }

    @Override
    public void resize(int width, int height) {
        sceneManager.resize(width, height);
    }

    @Override
    public void dispose() {
        batch.dispose();
        sceneTransition.dispose();

        // Dispose all created scenes
        for (SceneType type : SceneType.values()) {
            sceneFactory.disposeScene(type);
        }
    }
}
