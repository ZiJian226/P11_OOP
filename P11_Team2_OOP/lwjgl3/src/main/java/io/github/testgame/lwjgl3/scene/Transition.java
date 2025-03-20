package io.github.testgame.lwjgl3.scene;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import io.github.testgame.lwjgl3.abstractEngine.SceneManager;

public class Transition {
    private Stage fromStage;
    private Stage toStage;
    private ShapeRenderer shapeRenderer;
    private SceneType targetScene;
    private SceneManager sceneManager;

    // Transition states
    private enum TransitionState {
        IDLE,
        FADE_OUT,
        SWITCH_SCENE,
        FADE_IN,
        COMPLETED
    }

    private TransitionState state = TransitionState.IDLE;
    private float duration = 0.5f; // Total duration for each fade phase (seconds)
    private float timer = 0;
    private float alpha = 0;

    public Transition(SceneManager sceneManager) {
        this.sceneManager = sceneManager;
        this.shapeRenderer = new ShapeRenderer();
    }

    public void startTransition(SceneType targetScene) {
        if (sceneManager.isCurrentScene(SceneType.MAIN_MENU)) {
            this.targetScene = targetScene;
            this.state = TransitionState.FADE_OUT;
            this.timer = 0;
            this.alpha = 0;
        } else {
            // For other transitions, just change the scene immediately
            sceneManager.changeScene(targetScene);
        }
    }

    public boolean isTransitioning() {
        return state != TransitionState.IDLE && state != TransitionState.COMPLETED;
    }

    public void update(float delta) {
        if (!isTransitioning()) return;

        timer += delta;

        switch (state) {
            case FADE_OUT:
                // Fading to white
                alpha = Math.min(1.0f, timer / duration);
                if (alpha >= 1.0f) {
                    state = TransitionState.SWITCH_SCENE;
                    timer = 0;
                }
                break;

            case SWITCH_SCENE:
                // Actual scene change happens here
                sceneManager.changeScene(targetScene);
                state = TransitionState.FADE_IN;
                timer = 0;
                break;

            case FADE_IN:
                // Fading from white
                alpha = Math.max(0.0f, 1.0f - (timer / duration));
                if (alpha <= 0.0f) {
                    state = TransitionState.COMPLETED;
                    timer = 0;
                }
                break;

            case COMPLETED:
                state = TransitionState.IDLE;
                break;
        }
    }

    public void render() {
        if (!isTransitioning() || state == TransitionState.SWITCH_SCENE) return;

        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(0, 0, 0, alpha);
        shapeRenderer.rect(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        shapeRenderer.end();

        Gdx.gl.glDisable(GL20.GL_BLEND);
    }

    public void dispose() {
        if (shapeRenderer != null) {
            shapeRenderer.dispose();
        }
    }
}
