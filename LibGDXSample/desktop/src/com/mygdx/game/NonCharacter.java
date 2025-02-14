package com.mygdx.game;

public class NonCharacter extends MoveableObject{
    public NonCharacter(){
        super(null, 0, 0, 0);
    }
    public NonCharacter(String textureFile){
        super(textureFile, 0, 0, 0);
    }
    public NonCharacter(String textureFile, float x, float y, float speed){
        super(textureFile, x, y, speed);
    }
    public void setDirection(float directionX, float directionY){};
    public void updateRotation(float deltaX, float deltaY){};
}
