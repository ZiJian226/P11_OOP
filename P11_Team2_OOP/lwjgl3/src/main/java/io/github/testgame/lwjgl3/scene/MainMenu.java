package io.github.testgame.lwjgl3.scene;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import io.github.testgame.lwjgl3.abstractEngine.*;

//extend
public class MainMenu extends Scene {
    private BitmapFont font;
    private UIManager uiManager;
    private SceneManager sceneManager;

    public MainMenu(SceneManager sceneManager, UIManager uiManager) {
        this.sceneManager = sceneManager;
        this.uiManager = uiManager;
    }

    @Override
    public void create() {
        font = new BitmapFont(); // Default font
//        uiManager = new UIManager();


        // Create the "Start" button with custom colors
        float buttonWidth = 200;
        float buttonHeight = 100;
        float x = (Gdx.graphics.getWidth() - buttonWidth) / 2;
        float y = (Gdx.graphics.getHeight() - buttonHeight) / 2 + 100;
        Button startButton = new Button(x, y, buttonWidth, buttonHeight, Color.WHITE, Color.BLACK, font, "Start");
        startButton.setOnClick(() -> sceneManager.changeScene(SceneType.GAME));

        // Create the "Fail" and "Victory" buttons for testing
        Button failButton = new Button(x, y - 120, buttonWidth, buttonHeight, Color.RED, Color.BLACK, font, "Fail");
        failButton.setOnClick(() -> sceneManager.changeScene(SceneType.FAIL));
        Button victoryButton = new Button(x, y - 240, buttonWidth, buttonHeight, Color.GREEN, Color.BLACK, font, "Victory");
        victoryButton.setOnClick(() -> sceneManager.changeScene(SceneType.VICTORY));

        startButton.setButtonColor(Color.WHITE);
        startButton.setTextColor(Color.BLACK);

        uiManager.addButton(startButton);
        uiManager.addButton(failButton);
        uiManager.addButton(victoryButton);
    }

    @Override
    public void render() {
        // Clear the scene with a black background
        Gdx.gl.glClearColor(0.2f, 0.2f, 0.2f, 1);
        Gdx.gl.glClear(Gdx.gl.GL_COLOR_BUFFER_BIT);

        uiManager.render(shapeRenderer, batch);
        uiManager.handleInput();

//        startButton.render(shapeRenderer, batch);
//        failButton.render(shapeRenderer, batch);
//        victoryButton.render(shapeRenderer, batch);
//
//        // Check for input
//        if (Gdx.input.justTouched()) {
//            int touchX = Gdx.input.getX();
//            int touchY = Gdx.graphics.getHeight() - Gdx.input.getY();
//
//            if (startButton.isClicked(touchX, touchY)) {
//                SceneManager.getInstance().changeScene(SceneType.GAME);
//            } else if (failButton.isClicked(touchX, touchY)) {
//                SceneManager.getInstance().changeScene(SceneType.FAIL);
//            } else if (victoryButton.isClicked(touchX, touchY)) {
//                SceneManager.getInstance().changeScene(SceneType.VICTORY);
//            }
//        }
    }

    @Override
    public void dispose() {
        shapeRenderer.dispose();
        batch.dispose();
        uiManager.dispose();
    }
}
