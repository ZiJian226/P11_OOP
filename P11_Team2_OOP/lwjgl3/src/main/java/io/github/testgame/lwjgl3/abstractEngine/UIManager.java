package io.github.testgame.lwjgl3.abstractEngine;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import io.github.testgame.lwjgl3.scene.Button;

import java.util.ArrayList;
import java.util.List;

public class UIManager {
    private final List<Button> buttons;

    public UIManager() {
        buttons = new ArrayList<>();
    }

    public void addButton(Button button) {
        buttons.add(button);
    }

    public void render(ShapeRenderer shapeRenderer, SpriteBatch batch) {
        for (Button button : buttons) {
            button.render(shapeRenderer, batch);
        }
    }

    public void handleInput() {
        if (Gdx.input.justTouched()) {
            int touchX = Gdx.input.getX();
            int touchY = Gdx.graphics.getHeight() - Gdx.input.getY();

            for (Button button : buttons) {
                if (button.isClicked(touchX, touchY)) {
                    button.onClick();
                }
            }
        }
    }

    public void dispose() {
        for (Button button : buttons) {
            button.dispose();
        }
    }
}
