package com.mygdx.game;

public abstract class MoveableObject extends TextureObject implements iMoveable{
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
}
