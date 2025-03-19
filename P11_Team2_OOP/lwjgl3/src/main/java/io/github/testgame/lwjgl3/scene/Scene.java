package io.github.testgame.lwjgl3.scene;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public abstract class Scene {
    protected SpriteBatch batch;
    protected ShapeRenderer shapeRenderer;
    protected Stage stage;

    public Scene() {
        this.batch = new SpriteBatch();
        this.shapeRenderer = new ShapeRenderer();
        this.stage = new Stage(new ScreenViewport());
    }

    //abstract classes
    public abstract void create();
    public abstract void render();
    public abstract void dispose();

    public Stage getStage() {
        return stage;
    }

    public void reload() {}

    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

}
