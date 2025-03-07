package io.github.testgame.lwjgl3.collision;

import io.github.testgame.lwjgl3.abstractEngine.SceneManager;

public class EnemyDamageHandler extends DamageHandler {

    public EnemyDamageHandler(SceneManager sceneManager) {
        super(sceneManager);
    }

    @Override
    protected int getDamageAmount() {
        return 1; // Enemy deals 1 damage
    }

    @Override
    protected long getDamageInterval() {
        return 1000; // 1 second interval
    }

    protected boolean hasPeriodicDamage() {
        return true; // Enemies do periodic damage
    }
}
