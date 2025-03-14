package io.github.testgame.lwjgl3;

import com.badlogic.gdx.Gdx;
import io.github.testgame.lwjgl3.scene.*;
import io.github.testgame.lwjgl3.abstractEngine.*;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class GameMaster extends ApplicationAdapter {
    private final int width, height;
    private SceneManager sceneManager;
    private UIManager uiManager;
    private Scene mainMenu, failScene, victoryScene;
    private iGameScene gameScene;
    private SpriteBatch batch;

    public GameMaster(int width, int height) {
        this.width = width;
        this.height = height;
    }

    @Override
    public void create() {
        batch = new SpriteBatch();
        sceneManager = new SceneManager();
        uiManager = new UIManager();

        mainMenu = new MainMenu(sceneManager, uiManager);
        gameScene = new GameScene(sceneManager);
        failScene = new FailScene(sceneManager, (GameScene) gameScene);
        victoryScene = new VictoryScene(sceneManager, (GameScene) gameScene);

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
        if (currentScene != null) {
            currentScene.render();
        }
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
    }
}
