package io.github.testgame.lwjgl3.entity.moveableObject;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import io.github.testgame.lwjgl3.entity.TextureObject;

public abstract class MoveableObject extends TextureObject implements iMoveable {
    private float speed, rotation;

    public MoveableObject(){
        super(null, 0, 0);
    }
    public MoveableObject(String textureFile){
        super(textureFile, 0, 0);
    }
    public MoveableObject(String textureFile, float x, float y, float speed){
        super(textureFile, x, y);
        this.speed = speed;
    }
    public float getSpeed(){
        return speed;
    }
    public void setSpeed(float speed){
        this.speed = speed;
    }
    public float getRotation(){
        return rotation;
    }
    public void setRotation(float rotation){
        this.rotation = rotation;
    }
    @Override
    public abstract Body getBody();
    @Override
    public abstract float getForce();
    @Override
    public abstract Vector2 getPosition();
    @Override
    public abstract void updateRotation(float deltaX, float deltaY);
}
