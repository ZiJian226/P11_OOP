package io.github.testgame.lwjgl3.screen;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public abstract class Scene {
    protected SpriteBatch batch;
    protected ShapeRenderer shapeRenderer;

    public Scene() {
        this.batch = new SpriteBatch();
        this.shapeRenderer = new ShapeRenderer();
    }

    public abstract void create();
    public abstract void render();
    public abstract void dispose();
}
