package io.github.testgame.lwjgl3.collision;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class DamageFlashEffect {
    private boolean isFlashing;
    private float flashDuration;
    private float flashTimer;
    private ShapeRenderer flashRenderer;

    public DamageFlashEffect() {
        this.isFlashing = false;
        this.flashDuration = 0.1f; // 0.1 seconds flash duration
        this.flashTimer = 0f;
        this.flashRenderer = new ShapeRenderer();
    }

    public void triggerFlash() {
        isFlashing = true;
        flashTimer = flashDuration;
    }

    public void update(float deltaTime) {
        if (isFlashing) {
            flashTimer -= deltaTime;
            if (flashTimer <= 0) {
                isFlashing = false;
            }
        }
    }

    public void render() {
        if (isFlashing) {
            Gdx.gl.glEnable(GL20.GL_BLEND);
            Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

            flashRenderer.begin(ShapeRenderer.ShapeType.Filled);
            flashRenderer.setColor(1, 0, 0, 0.3f); // Semi-transparent red
            flashRenderer.rect(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
            flashRenderer.end();

            Gdx.gl.glDisable(GL20.GL_BLEND);
        }
    }

    public void dispose() {
        flashRenderer.dispose();
    }
}

