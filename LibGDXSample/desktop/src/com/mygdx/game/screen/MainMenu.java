package com.mygdx.game.screen;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.mygdx.game.abstractEngine.SceneManager;

public class MainMenu extends ApplicationAdapter {
    private ShapeRenderer shapeRenderer;
    private SpriteBatch batch;
    private BitmapFont font;
    private Button startButton;
    
    // commented out lines below are to test whether the victoryscene and failscene works
    private Button failButton;
    private Button victoryButton;

    @Override
    public void create() {
        shapeRenderer = new ShapeRenderer();
        batch = new SpriteBatch();
        font = new BitmapFont(); // Default font

        // Create the "Start" button with custom colors
        float buttonWidth = 200;
        float buttonHeight = 100;
        float x = (Gdx.graphics.getWidth() - buttonWidth) / 2;
        float y = (Gdx.graphics.getHeight() - buttonHeight) / 2 + 100;
        startButton = new Button(x, y, buttonWidth, buttonHeight, Color.WHITE, Color.BLACK, font, "Start");
        
        // commented out lines below are to test whether the victoryscene and failscene works
        failButton = new Button(x, y - 120, buttonWidth, buttonHeight, Color.RED, Color.BLACK, font, "Fail");
        victoryButton = new Button(x, y - 240, buttonWidth, buttonHeight, Color.GREEN, Color.BLACK, font, "Victory");

        startButton.setButtonColor(Color.WHITE);
        startButton.setTextColor(Color.BLACK);
    }

    @Override
    public void render() {
        // Clear the screen with a black background
        Gdx.gl.glClearColor(0.2f, 0.2f, 0.2f, 1);
        Gdx.gl.glClear(Gdx.gl.GL_COLOR_BUFFER_BIT);

        startButton.render(shapeRenderer, batch);
        
        // commented out lines below are to test whether the victoryscene and failscene works
        failButton.render(shapeRenderer, batch);
        victoryButton.render(shapeRenderer, batch);

        // Check for input
        if (Gdx.input.justTouched()) {
            int touchX = Gdx.input.getX();
            int touchY = Gdx.graphics.getHeight() - Gdx.input.getY();

            if (startButton.isClicked(touchX, touchY)) {
                SceneManager.getInstance().changeScene("Game");
            } 
            
            // commented out lines below are to test whether the victoryscene and failscene works
            else if (failButton.isClicked(touchX, touchY)) {
                SceneManager.getInstance().changeScene("Fail");
            } else if (victoryButton.isClicked(touchX, touchY)) {
                SceneManager.getInstance().changeScene("Victory");
            }
            
        }
    }

    @Override
    public void dispose() {
        shapeRenderer.dispose();
        batch.dispose();
        startButton.dispose();
    }
}