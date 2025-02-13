package com.mygdx.game;

public class MoveableObject extends TextureObject {
    private float speed;
    private MoveType moveType;

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
}
