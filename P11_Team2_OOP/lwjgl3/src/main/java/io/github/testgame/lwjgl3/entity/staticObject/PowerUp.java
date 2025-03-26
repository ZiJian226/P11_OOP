package io.github.testgame.lwjgl3.entity.staticObject;

import com.badlogic.gdx.physics.box2d.World;

public class PowerUp extends NeutralObject {
    private float lifespan; // In seconds

    public PowerUp(World world, String textureFile, float x, float y, float lifespan) {
        super(world, textureFile, x, y);
        this.lifespan = lifespan;
    }

    public float getLifespan() {
        return lifespan;
    }

    public void reduceLifespan(float delta) {
        lifespan -= delta;
    }
}
