package com.mygdx.game;

public class Character extends MoveableObject{
    public Character(){
        super(null, 0, 0, 0);
    }
    public Character(String textureFile){
        super(textureFile, 0, 0, 0);
    }
    public Character(String textureFile, float x, float y, float speed){
        super(textureFile, x, y, speed);
    }
    public void moveAIControlled(){}
    public void moveUserControlled(){}
}
