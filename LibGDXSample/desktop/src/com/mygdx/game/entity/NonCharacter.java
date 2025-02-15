package com.mygdx.game.entity;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

public class NonCharacter extends MoveableObject {
    public NonCharacter(){
        super(null, 0, 0, 0);
    }
    public NonCharacter(String textureFile){
        super(textureFile, 0, 0, 0);
    }
    public NonCharacter(String textureFile, float x, float y, float speed){
        super(textureFile, x, y, speed);
    }
    public Body getBody(){
        return null;
    }
    public float getForce(){
        return 0;
    }
    public Vector2 getPosition(){
        return null;
    }
    public void setDirection(float directionX, float directionY){};
    public void updateRotation(float deltaX, float deltaY){};
}
