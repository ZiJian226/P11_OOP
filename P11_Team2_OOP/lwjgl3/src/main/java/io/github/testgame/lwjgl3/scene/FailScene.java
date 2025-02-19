package io.github.testgame.lwjgl3.scene;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import io.github.testgame.lwjgl3.abstractEngine.SceneManager;
import io.github.testgame.lwjgl3.GameMaster;

//extend
public class FailScene extends Scene {
    private Button menuButton;
    private BitmapFont menuFont;
    private BitmapFont failFont;

    @Override
    public void create() {
        menuFont = new BitmapFont();
        failFont = new BitmapFont();
        failFont.setColor(Color.BLACK);
        failFont.getData().setScale(2);

        // Create a button to return to the main menu
        float buttonWidth = 200;
        float buttonHeight = 100;
        float x = (Gdx.graphics.getWidth() - buttonWidth) / 2;
        float y = (Gdx.graphics.getHeight() - buttonHeight) / 2 - 100;
        menuButton = new Button(x, y, buttonWidth, buttonHeight, Color.BLACK, Color.BLACK, menuFont, "Menu");

        menuButton.setButtonColor(Color.DARK_GRAY);
        menuButton.setTextColor(Color.WHITE);
    }

    @Override
    public void render() {
        // Clear the scene with a red background
        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClear(Gdx.gl.GL_COLOR_BUFFER_BIT);

        batch.begin();
        failFont.draw(batch, "You Lose!", Gdx.graphics.getWidth() / 2 - 60, Gdx.graphics.getHeight() / 2 + 100);
        batch.end();

        menuButton.render(shapeRenderer, batch);

        if (Gdx.input.justTouched()) {
            int touchX = Gdx.input.getX();
            int touchY = Gdx.graphics.getHeight() - Gdx.input.getY();

            if (menuButton.isClicked(touchX, touchY)) {
                GameMaster.getInstance().resetGame();
                SceneManager.getInstance().changeScene(SceneType.MAIN_MENU);
            }
        }
    }

    @Override
    public void dispose() {
        shapeRenderer.dispose();
        batch.dispose();
        menuButton.dispose();
    }
}
