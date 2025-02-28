package io.github.testgame.lwjgl3;

import io.github.testgame.lwjgl3.scene.*;
import io.github.testgame.lwjgl3.abstractEngine.*;
import com.badlogic.gdx.ApplicationAdapter;

public class GameMaster extends ApplicationAdapter {
    private final int width, height;
    private SceneManager sceneManager;
    private Scene mainMenu, failScene, victoryScene, gameScene;

    public GameMaster(int width, int height) {
        this.width = width;
        this.height = height;
    }

    @Override
    public void create() {
        mainMenu = new MainMenu();
        failScene = new FailScene();
        victoryScene = new VictoryScene();
        gameScene = new GameScene();

        mainMenu.create();
        failScene.create();
        victoryScene.create();
        gameScene.create();

        sceneManager = SceneManager.getInstance();
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
