package io.github.testgame.lwjgl3.entity.staticObject;

public class ModifierEffect {
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

    public float getDuration() {
        return duration;
    }

    public void reduceDuration(float deltaTime) {
        duration -= deltaTime;
    }

    public boolean isExpired() {
        return duration <= 0;
    }
}
