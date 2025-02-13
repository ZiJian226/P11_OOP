package com.mygdx.game;

public class StaticObject extends TextureObject{

    public StaticObject(){
        super(null, 0, 0);
    }
    public StaticObject(String textureFile){
        super(textureFile, 0, 0);
    }
    public StaticObject(String textureFile, float x, float y){
        super(textureFile, x, y);
    }
    public void result(){};
}
