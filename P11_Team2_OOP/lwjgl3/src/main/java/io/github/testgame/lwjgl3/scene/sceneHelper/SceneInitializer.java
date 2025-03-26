package io.github.testgame.lwjgl3.scene.sceneHelper;

import io.github.testgame.lwjgl3.abstractEngine.*;
import io.github.testgame.lwjgl3.scene.*;

public class SceneInitializer {
    private SceneManager sceneManager;
    private AudioManager audioManager;
    private UIManager uiManager;
    private IOManager ioManager;
    private Transition sceneTransition;
    private SceneFactory sceneFactory;

    public SceneInitializer(SceneManager sceneManager, AudioManager audioManager,
                            UIManager uiManager, IOManager ioManager,
                            Transition sceneTransition, SceneFactory sceneFactory) {
        this.sceneManager = sceneManager;
        this.audioManager = audioManager;
        this.uiManager = uiManager;
        this.ioManager = ioManager;
        this.sceneTransition = sceneTransition;
        this.sceneFactory = sceneFactory;
    }

    public void registerAllSceneFactories() {
        // Register all scene factories
        sceneFactory.registerFactory(SceneType.MAIN_MENU, () -> createMainMenuScene());
        sceneFactory.registerFactory(SceneType.GAME, () -> createGameScene());
        sceneFactory.registerFactory(SceneType.FAIL, () -> createFailScene());
        sceneFactory.registerFactory(SceneType.VICTORY, () -> createVictoryScene());
        sceneFactory.registerFactory(SceneType.INSTRUCTIONS, () -> createInstructionsScene());
    }

    private Scene createMainMenuScene() {
        GameScene gameScene = new GameScene(sceneManager, audioManager, ioManager, sceneTransition);
        MainMenu mainMenu = new MainMenu(sceneManager, uiManager, audioManager,
            sceneTransition, gameScene, sceneFactory);
        mainMenu.create();
        return mainMenu;
    }

    private Scene createGameScene() {
        GameScene gameScene = new GameScene(sceneManager, audioManager, ioManager, sceneTransition);
        gameScene.create();
        return gameScene;
    }

    private Scene createFailScene() {
        FailScene failScene = new FailScene(sceneManager, audioManager, ioManager,
            sceneTransition, sceneFactory);
        failScene.create();
        return failScene;
    }

    private Scene createVictoryScene() {
        VictoryScene victoryScene = new VictoryScene(sceneManager, audioManager, ioManager,
            sceneTransition, sceneFactory);
        victoryScene.create();
        return victoryScene;
    }

    private Scene createInstructionsScene() {
        GameInstructionsScene instructionsScene = new GameInstructionsScene(sceneManager,
            audioManager, ioManager,
            sceneTransition, sceneFactory);
        instructionsScene.create();
        return instructionsScene;
    }
}
