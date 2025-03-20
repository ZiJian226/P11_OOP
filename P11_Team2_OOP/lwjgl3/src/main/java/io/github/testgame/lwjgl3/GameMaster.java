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
    private Scene mainMenu, failScene, victoryScene;
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
        sceneTransition = new Transition(sceneManager);

        mainMenu = new MainMenu(sceneManager, uiManager, audioManager, sceneTransition);
        gameScene = new GameScene(sceneManager, audioManager);
        failScene = new FailScene(sceneManager, (GameScene) gameScene, audioManager);
        victoryScene = new VictoryScene(sceneManager, (GameScene) gameScene, audioManager);

        mainMenu.create();
        failScene.create();
        victoryScene.create();
        gameScene.create();

        // Add scenes to SceneManager
        sceneManager.addScene(SceneType.MAIN_MENU, mainMenu);
        sceneManager.addScene(SceneType.GAME, (Scene)gameScene);
        sceneManager.addScene(SceneType.FAIL, failScene);
        sceneManager.addScene(SceneType.VICTORY, victoryScene);

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
