package io.github.testgame.lwjgl3.entity;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import io.github.testgame.lwjgl3.collision.CollisionFilters;

public class AggressiveObject extends StaticObject {
    private Body body;
    public AggressiveObject(){
        super(null, 0, 0);
    }
    public AggressiveObject(String textureFile){
        super(textureFile, 0, 0);
    }
    public AggressiveObject(World world, String textureFile, float x, float y){
        super(textureFile, x, y);
        this.body = createBox(world, x, y, getWidth(), getHeight(), true, CollisionFilters.CATEGORY_AGGRESSIVE, CollisionFilters.MASK_AGGRESSIVE);
        this.body.setUserData(this);
    }
    @Override
    public Body getBody(){
        return body;
    }
}
