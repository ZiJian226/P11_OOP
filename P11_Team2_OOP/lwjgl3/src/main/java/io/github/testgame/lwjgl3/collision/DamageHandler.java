package io.github.testgame.lwjgl3.collision;

import com.badlogic.gdx.Gdx;
import io.github.testgame.lwjgl3.abstractEngine.AudioManager;
import io.github.testgame.lwjgl3.entity.Player;
import io.github.testgame.lwjgl3.abstractEngine.SceneManager;
import io.github.testgame.lwjgl3.scene.SceneType;

import java.util.HashMap;
import java.util.Map;

public abstract class DamageHandler {
    protected final Map<Object, Float> activeColliders = new HashMap<>();
    private float damageTimer = 0;
    private static final float DAMAGE_INTERVAL = 1.0f;
    private SceneManager sceneManager;
    private AudioManager audioManager;
    private DamageFlashEffect damageFlashEffect;

    public DamageHandler(SceneManager sceneManager, AudioManager audioManager, DamageFlashEffect damageFlashEffect) {
        this.sceneManager = sceneManager;
        this.audioManager = audioManager;
        this.damageFlashEffect = damageFlashEffect;
    }

    public void applyDamageToPlayer(Player player, float deltaTime) {
        if (activeColliders.isEmpty()) {
            damageTimer = 0;
            return;
        }

        // Only process periodic damage if the handler supports it
        if (hasPeriodicDamage()) {
            damageTimer += deltaTime;
            if (damageTimer >= DAMAGE_INTERVAL) {
                int totalDamage = activeColliders.size() * getDamageAmount();
                player.setHealth(player.getHealth() - totalDamage);
                System.out.println("Periodic damage: " + totalDamage + ", Health: " + player.getHealth());

                audioManager.playSoundEffect("damage");
                damageFlashEffect.triggerFlash();

                if (player.getHealth() <= 0) {
                    handlePlayerDeath(player);
                }
                damageTimer = 0;
            }
        }
    }

    public void beginContact(Player player, Object collider) {
        try {
            activeColliders.put(collider, 0f);
            if (this instanceof AggressiveObjectDamageHandler) {
                audioManager.playSoundEffect("damage");
                damageFlashEffect.triggerFlash();
                Gdx.app.postRunnable(() -> {
                    player.setHealth(0);
                    handlePlayerDeath(player);
                });
            } else {
                float newHealth = player.getHealth() - getDamageAmount();
                player.setHealth(newHealth);
                System.out.println("Initial contact damage, Health: " + newHealth);

                audioManager.playSoundEffect("damage");
                damageFlashEffect.triggerFlash();

                if (newHealth <= 0) {
                    handlePlayerDeath(player);
                }
            }
        } catch (Exception e) {
            System.err.println("Error in beginContact: " + e.getMessage());
        }
    }

    public void endContact(Object collider) {
        activeColliders.remove(collider);
        if (activeColliders.isEmpty()) {
            damageTimer = 0;
        }
    }

    protected void handlePlayerDeath(Player player) {
        try {
            activeColliders.clear(); // Clear colliders first
            Gdx.app.postRunnable(() -> {
                player.setHealth(10);
                player.setScore(0);
                sceneManager.changeScene(SceneType.FAIL);
            });
        } catch (Exception e) {
            System.err.println("Error in handlePlayerDeath: " + e.getMessage());
        }
    }

    protected abstract boolean hasPeriodicDamage();
    protected abstract int getDamageAmount();
    protected abstract long getDamageInterval();
}
