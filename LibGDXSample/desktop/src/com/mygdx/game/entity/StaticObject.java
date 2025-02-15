package com.mygdx.game.entity;

import com.badlogic.gdx.graphics.Texture;

public class StaticObject extends TextureObject {
    private static Texture tex;

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
