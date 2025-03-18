package io.github.testgame.lwjgl3.collision;

import io.github.testgame.lwjgl3.abstractEngine.AudioManager;
import io.github.testgame.lwjgl3.abstractEngine.SceneManager;

public class  AggressiveObjectDamageHandler extends DamageHandler {

    public AggressiveObjectDamageHandler(SceneManager sceneManager, AudioManager audioManager, DamageFlashEffect damageFlashEffect) {
        super(sceneManager, audioManager, damageFlashEffect);
    }

    @Override
    protected int getDamageAmount() {
        return 10; // Aggressive object deals 1 damage
    }

    @Override
    protected long getDamageInterval() {
        return 1000; // 1 second interval
    }
    protected boolean hasPeriodicDamage() {
        return false; // Enemies do periodic damage
    }
}
