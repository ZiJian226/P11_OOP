package io.github.testgame.lwjgl3.entity.staticObject;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;

public class Magazine extends NeutralObject {
    private float ammo;
    private Body body;
    public Magazine() {
        super(null, 0, 0);
        this.ammo = 0;
    }
    public Magazine(String textureFile, float x, float y) {
        super(textureFile, x, y);
        this.ammo = 0;
    }
    public Magazine(World world, String textureFile, float x, float y, float ammo) {
        super(world, textureFile, x, y);
        this.body = super.getBody();
        setAmmo(ammo);
    }
    public float getAmmo() {
        return ammo;
    }
    public void setAmmo(float ammo) {
        this.ammo = ammo;
    }
}
