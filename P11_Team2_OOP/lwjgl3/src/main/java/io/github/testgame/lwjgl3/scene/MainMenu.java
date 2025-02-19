package io.github.testgame.lwjgl3.scene;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import io.github.testgame.lwjgl3.abstractEngine.*;

//extend
public class MainMenu extends Scene {
    private BitmapFont font;
    private Button startButton;
    private Button failButton;
    private Button victoryButton;

    @Override
    public void create() {
        font = new BitmapFont(); // Default font

        // Create the "Start" button with custom colors
        float buttonWidth = 200;
        float buttonHeight = 100;
        float x = (Gdx.graphics.getWidth() - buttonWidth) / 2;
        float y = (Gdx.graphics.getHeight() - buttonHeight) / 2 + 100;
        startButton = new Button(x, y, buttonWidth, buttonHeight, Color.WHITE, Color.BLACK, font, "Start");

        // Create the "Fail" and "Victory" buttons for testing
        failButton = new Button(x, y - 120, buttonWidth, buttonHeight, Color.RED, Color.BLACK, font, "Fail");
        victoryButton = new Button(x, y - 240, buttonWidth, buttonHeight, Color.GREEN, Color.BLACK, font, "Victory");

        startButton.setButtonColor(Color.WHITE);
        startButton.setTextColor(Color.BLACK);
    }

    @Override
    public void render() {
        // Clear the scene with a black background
        Gdx.gl.glClearColor(0.2f, 0.2f, 0.2f, 1);
        Gdx.gl.glClear(Gdx.gl.GL_COLOR_BUFFER_BIT);

        startButton.render(shapeRenderer, batch);
        failButton.render(shapeRenderer, batch);
        victoryButton.render(shapeRenderer, batch);

        // Check for input
        if (Gdx.input.justTouched()) {
            int touchX = Gdx.input.getX();
            int touchY = Gdx.graphics.getHeight() - Gdx.input.getY();

            if (startButton.isClicked(touchX, touchY)) {
                SceneManager.getInstance().changeScene(SceneType.GAME);
            } else if (failButton.isClicked(touchX, touchY)) {
                SceneManager.getInstance().changeScene(SceneType.FAIL);
            } else if (victoryButton.isClicked(touchX, touchY)) {
                SceneManager.getInstance().changeScene(SceneType.VICTORY);
            }
        }
    }

    @Override
    public void dispose() {
        shapeRenderer.dispose();
        batch.dispose();
        startButton.dispose();
        failButton.dispose();
        victoryButton.dispose();
    }
}
