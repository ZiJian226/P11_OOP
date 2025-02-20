package io.github.testgame.lwjgl3.entity;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import io.github.testgame.lwjgl3.collision.CollisionFilters;

public class NeutralObject extends StaticObject {
    private Body body;
    public NeutralObject(){
        super(null, 0, 0);
    }
    public NeutralObject(String textureFile){
        super(textureFile, 0, 0);
    }
    public NeutralObject(World world, String textureFile, float x, float y){
        super(textureFile, x, y);
        this.body = createBox(world, x, y, getWidth(), getHeight(), true, CollisionFilters.CATEGORY_NEUTRAL, CollisionFilters.MASK_NEUTRAL);
        this.body.setUserData(this);
    }
    @Override
    public Body getBody(){
        return body;
    }
}
