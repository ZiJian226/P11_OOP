package io.github.testgame.lwjgl3.scene;

public interface iGameScene {
    public void create();
    public void render();
    public void dispose();
    public void update(float delta);
    public void resetGame();
}
