package io.github.testgame.lwjgl3.scene.sceneHelper;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import io.github.testgame.lwjgl3.abstractEngine.SceneManager;
import io.github.testgame.lwjgl3.scene.Scene;

public class Transition {
    private TransitionState state = TransitionState.IDLE;
    private SceneType targetScene;
    private SceneManager sceneManager;
    private SceneFactory sceneFactory;
    private ShapeRenderer shapeRenderer;
    private float duration = 0.5f;
    private float timer = 0;
    private float alpha = 0;

    public Transition(SceneManager sceneManager, SceneFactory sceneFactory) {
        this.sceneManager = sceneManager;
        this.sceneFactory = sceneFactory;
        this.shapeRenderer = new ShapeRenderer();
    }

    public void startTransition(SceneType targetScene) {
        if (state != TransitionState.IDLE) return;

        this.targetScene = targetScene;
        this.state = TransitionState.FADE_OUT;
        this.timer = 0;
    }

    private void handleSceneLifecycle(SceneType targetScene) {
        // Get current scene and mark it for deactivation
        Scene currentScene = sceneManager.getCurrentSceneObject();
        if (currentScene != null) {
            currentScene.setLifeCycle(false);
        }

        // Get or create target scene
        Scene nextScene = sceneFactory.getScene(targetScene);

        // Reset and activate the target scene
        if (nextScene != null) {
            nextScene.reset();
            nextScene.setLifeCycle(true);
            sceneManager.addScene(targetScene, nextScene);
            sceneManager.changeScene(targetScene);

            // Set input processor to the new scene's stage
            Gdx.input.setInputProcessor(nextScene.getStage());
        }
    }

    public void update(float delta) {
        if (state == TransitionState.IDLE) return;

        timer += delta;

        switch(state) {
            case FADE_OUT:
                alpha = Math.min(timer / (duration / 2), 1);
                if (timer >= duration / 2) {
                    state = TransitionState.CHANGING;
                    timer = 0;
                }
                break;

            case CHANGING:
                handleSceneLifecycle(targetScene);
                state = TransitionState.FADE_IN;
                timer = 0;
                break;

            case FADE_IN:
                alpha = Math.max(1 - timer / (duration / 2), 0);
                if (timer >= duration / 2) {
                    state = TransitionState.COMPLETED;
                }
                break;

            case COMPLETED:
                state = TransitionState.IDLE;
                break;
        }
    }

    public void render() {
        if (state == TransitionState.IDLE) return;

        Gdx.gl.glEnable(GL20.GL_BLEND);
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
