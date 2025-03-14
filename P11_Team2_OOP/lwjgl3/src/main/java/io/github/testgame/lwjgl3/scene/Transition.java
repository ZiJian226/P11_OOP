package io.github.testgame.lwjgl3.scene;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;

public class Transition {
    private Stage fromStage;
    private Stage toStage;
    private float duration;
    private float elapsedTime;
    private boolean transitioning;
    private ShapeRenderer shapeRenderer;

    // Center of the screen
    private float centerX;
    private float centerY;

    // Maximum radius for the iris effect
    private float maxRadius;

    public Transition(float duration) {
        this.duration = duration;
        this.transitioning = false;
        this.shapeRenderer = new ShapeRenderer();
    }

    public void start(Stage fromStage, Stage toStage) {
        this.fromStage = fromStage;
        this.toStage = toStage;
        this.elapsedTime = 0;
        this.transitioning = true;

        // Set center and max radius based on screen dimensions
        this.centerX = Gdx.graphics.getWidth() / 2f;
        this.centerY = Gdx.graphics.getHeight() / 2f;

        // Calculate maximum radius to fully cover the screen (diagonal)
        this.maxRadius = new Vector2(centerX, centerY).len() * 1.2f;
    }

    public void update(float deltaTime) {
        if (!transitioning) return;

        elapsedTime += deltaTime;
        if (elapsedTime >= duration) {
            transitioning = false;
        }
    }

    public void render(SpriteBatch batch) {
        if (!transitioning) return;

        float progress = elapsedTime / duration;
        float currentRadius = progress * maxRadius;

        // Ensure proper batch handling
        if (batch.isDrawing()) batch.end();

        // First render the FROM scene (will be visible outside the circle)
        fromStage.draw();

        // Begin stencil buffer for masking
        Gdx.gl.glEnable(GL20.GL_STENCIL_TEST);
        Gdx.gl.glClearStencil(0);
        Gdx.gl.glClear(GL20.GL_STENCIL_BUFFER_BIT);

        // Set stencil operations
        Gdx.gl.glStencilFunc(GL20.GL_ALWAYS, 1, 1);
        Gdx.gl.glStencilOp(GL20.GL_REPLACE, GL20.GL_REPLACE, GL20.GL_REPLACE);

        // Draw the circular mask into stencil buffer
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        Gdx.gl.glColorMask(false, false, false, false); // Don't draw to color buffer
        shapeRenderer.circle(centerX, centerY, currentRadius);
        shapeRenderer.end();

        // Set stencil function to only pass where stencil value is 1
        Gdx.gl.glColorMask(true, true, true, true); // Draw to color buffer again
        Gdx.gl.glStencilFunc(GL20.GL_EQUAL, 1, 1);

        // Draw the TO scene, only visible through the circle
        toStage.draw();

        // Disable stencil testing
        Gdx.gl.glDisable(GL20.GL_STENCIL_TEST);
    }

    public boolean isTransitioning() {
        return transitioning;
    }

    public void dispose() {
        if (shapeRenderer != null) {
            shapeRenderer.dispose();
        }
    }
}
