package io.github.testgame.lwjgl3.scene;

import com.badlogic.gdx.ApplicationAdapter;

public abstract class Scene extends ApplicationAdapter {
    public Scene() {
    }
    public abstract void create();
    public abstract void render();
    public abstract void dispose();
}
