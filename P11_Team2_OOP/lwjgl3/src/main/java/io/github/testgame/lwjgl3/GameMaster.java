package io.github.testgame.lwjgl3;

import com.badlogic.gdx.Gdx;
import io.github.testgame.lwjgl3.scene.*;
import io.github.testgame.lwjgl3.abstractEngine.*;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class GameMaster extends ApplicationAdapter {
    private final int width, height;
    private SceneManager sceneManager;
    private AudioManager audioManager;
    private UIManager uiManager;
    private IOManager ioManager;
    private Scene mainMenu, failScene, victoryScene, GameInstructionsScene;
    private iGameScene gameScene;
    private SpriteBatch batch;
    private Transition sceneTransition;

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
        sceneTransition = new Transition(sceneManager);

        gameScene = new GameScene(sceneManager, audioManager, ioManager);
        mainMenu = new MainMenu(sceneManager, uiManager, audioManager, sceneTransition, (GameScene) gameScene);
        failScene = new FailScene(sceneManager,  audioManager, ioManager);
        victoryScene = new VictoryScene(sceneManager, audioManager, ioManager);
        GameInstructionsScene = new GameInstructionsScene(sceneManager, audioManager, ioManager);

        mainMenu.create();
        failScene.create();
        victoryScene.create();
        gameScene.create();
        GameInstructionsScene.create();

        // Add scenes to SceneManager
        sceneManager.addScene(SceneType.MAIN_MENU, mainMenu);
        sceneManager.addScene(SceneType.GAME, (Scene)gameScene);
        sceneManager.addScene(SceneType.FAIL, failScene);
        sceneManager.addScene(SceneType.VICTORY, victoryScene);
        sceneManager.addScene(SceneType.INSTRUCTIONS, GameInstructionsScene);

        // Set initial scene
        sceneManager.changeScene(SceneType.MAIN_MENU);
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
        mainMenu.dispose();
        failScene.dispose();
        victoryScene.dispose();
        gameScene.dispose();
        sceneTransition.dispose();
    }
}
