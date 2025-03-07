package io.github.testgame.lwjgl3;

import io.github.testgame.lwjgl3.scene.*;
import io.github.testgame.lwjgl3.abstractEngine.*;
import com.badlogic.gdx.ApplicationAdapter;

public class GameMaster extends ApplicationAdapter {
    private final int width, height;
    private SceneManager sceneManager;
    private UIManager uiManager;
    private Scene mainMenu, failScene, victoryScene;
    private iGameScene gameScene;

    public GameMaster(int width, int height) {
        this.width = width;
        this.height = height;
    }

    @Override
    public void create() {
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
    }

    @Override
    public void render() {
        switch (sceneManager.getCurrentScene()) {
            case MAIN_MENU:
                mainMenu.render();
                break;
            case GAME:
                gameScene.render();
                break;
            case FAIL:
                failScene.render();
                break;
            case VICTORY:
                victoryScene.render();
                break;
        }
    }

    @Override
    public void dispose() {
        mainMenu.dispose();
        failScene.dispose();
        victoryScene.dispose();
        gameScene.dispose();
    }
}
