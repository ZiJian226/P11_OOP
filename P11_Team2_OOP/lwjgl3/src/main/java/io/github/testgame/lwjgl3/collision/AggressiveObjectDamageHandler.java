package io.github.testgame.lwjgl3.collision;

import io.github.testgame.lwjgl3.abstractEngine.AudioManager;
import io.github.testgame.lwjgl3.abstractEngine.SceneManager;
import io.github.testgame.lwjgl3.scene.sceneHelper.Transition;

public class  AggressiveObjectDamageHandler extends DamageHandler {

    public AggressiveObjectDamageHandler(SceneManager sceneManager, AudioManager audioManager, DamageFlashEffect damageFlashEffect, Transition sceneTransition) {
        super(sceneManager, audioManager, damageFlashEffect, sceneTransition);
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
