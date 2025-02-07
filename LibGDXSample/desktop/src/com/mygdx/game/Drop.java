package com.mygdx.game;

import com.badlogic.gdx.Gdx;

public class Drop extends TextureObject{
    public Drop(){
        super(null, 0, 0, 0);
    }
    public Drop(String textureFile){
        super(textureFile, 0, 0, 0);
    }
    public Drop(String image, float x, float y, float speed){
        super(image, x, y, speed);
    }
    @Override
    public void moveAIControlled(){
        setY(getY() - getSpeed());
        if (getY() <= -getHeight()) {
            setSpeed(getSpeed() < 10 ? getSpeed() + getSpeed() : 10);
            setY(Gdx.graphics.getHeight());
            setX((float) (Math.random() * (Gdx.graphics.getWidth() - getWidth())));
        }
    }
}
