package io.github.testgame.lwjgl3.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class Modifier extends NeutralObject {
    private float multiplier;
    private Body body;

    // Store active modifiers and their durations
    private static Set<ModifierEffect> activeModifiers = new HashSet<>();

    // Status message timers for health and ammo
    private static float healthStatusTimer = 0;
    private static float ammoStatusTimer = 0;
    private static String healthStatusText = "";
    private static String ammoStatusText = "";

    // Store the last active speed modifier for status text
    private static ModifierEffect activeSpeedModifier = null;

    public Modifier() {
        super(null, 0, 0);
        this.multiplier = 0;
    }

    public Modifier(World world, String textureFile, float x, float y, float multiplier) {
        super(world, textureFile, x, y);
        this.body = super.getBody();
        this.multiplier = multiplier;
    }

    public void applyEffect(Player player) {
        // Random modifier type selection
        int effectType = (int) (Math.random() * 3);
        ModifierEffect effect = null;

        switch (effectType) {
            case 0: // Speed modifier
                effect = new ModifierEffect(ModifierType.SPEED, multiplier, 10);
                player.setSpeed(player.getSpeed() * multiplier);
                activeSpeedModifier = effect;
                break;
            case 1: // Health modifier
                effect = new ModifierEffect(ModifierType.HEALTH, multiplier, 0);
                player.setHealth((int)(player.getHealth() * multiplier));
                healthStatusText = "Health " + (multiplier > 1 ? "buffed" : "debuffed") +
                    " by " + String.format("%.2f", multiplier * 100)  + "%";
                healthStatusTimer = 1.0f; // Set display timer to 1 second
                break;
            case 2: // Ammo modifier
                effect = new ModifierEffect(ModifierType.AMMO, multiplier, 0);
                player.setAmmoCount((int)(player.getAmmoCount() * multiplier));
                ammoStatusText = "Ammo " + (multiplier > 1 ? "buffed" : "debuffed") +
                    " by " + String.format("%.2f", multiplier * 100) + "%";
                ammoStatusTimer = 1.0f; // Set display timer to 1 second
                break;
        }

        if (effect != null && effect.getType() == ModifierType.SPEED) {
            activeModifiers.add(effect);
        }
    }

    public static String getActiveStatusText(Player player) {
        StringBuilder statusText = new StringBuilder();

        // Add health status if timer is active
        if (healthStatusTimer > 0) {
            statusText.append(healthStatusText).append("\n");
        }

        // Add ammo status if timer is active
        if (ammoStatusTimer > 0) {
            statusText.append(ammoStatusText).append("\n");
        }

        // Add speed modifier status if active (unchanged logic)
        if (activeSpeedModifier != null && activeModifiers.contains(activeSpeedModifier)) {
            statusText.append("Speed ").append(activeSpeedModifier.getMultiplier() > 1 ? "buffed" : "debuffed")
                .append(" by ").append(String.format("%.2f", activeSpeedModifier.getMultiplier() * 100)).append("% for (")
                .append((int) activeSpeedModifier.duration).append("s)\n");
        }

        return statusText.toString();
    }

    public static void updateActiveModifiers(Player player) {
        float deltaTime = Gdx.graphics.getDeltaTime();

        // Update health and ammo status timers
        if (healthStatusTimer > 0) {
            healthStatusTimer -= deltaTime;
        }

        if (ammoStatusTimer > 0) {
            ammoStatusTimer -= deltaTime;
        }

        // Update active speed modifiers (unchanged logic)
        Iterator<ModifierEffect> iterator = activeModifiers.iterator();
        while (iterator.hasNext()) {
            ModifierEffect effect = iterator.next();
            effect.reduceDuration(deltaTime);

            if (effect.isExpired()) {
                if (effect.getType() == ModifierType.SPEED) {
                    player.setSpeed(player.getSpeed() / effect.getMultiplier());
                    if (effect == activeSpeedModifier) {
                        activeSpeedModifier = null;
                    }
                }
                iterator.remove();
            }
        }
    }

    private enum ModifierType {
        SPEED, HEALTH, AMMO
    }

    private static class ModifierEffect {
        private ModifierType type;
        private float multiplier;
        private float duration;

        public ModifierEffect(ModifierType type, float multiplier, float duration) {
            this.type = type;
            this.multiplier = multiplier;
            this.duration = duration;
        }

        public ModifierType getType() {
            return type;
        }

        public float getMultiplier() {
            return multiplier;
        }

        public void reduceDuration(float deltaTime) {
            duration -= deltaTime;
        }

        public boolean isExpired() {
            return duration <= 0;
        }
    }
}
