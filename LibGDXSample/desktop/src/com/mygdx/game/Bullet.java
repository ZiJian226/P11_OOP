package com.mygdx.game;

public class Bullet extends NonCharacter{
    public Bullet(){
        super(null, 0, 0, 0);
    }
    public Bullet(String textureFile){
        super(textureFile, 0, 0, 0);
    }
    public Bullet(String textureFile, float x, float y, float speed){
        super(textureFile, x, y, speed);
    }
    @Override
    public void moveAIControlled(){}
}
