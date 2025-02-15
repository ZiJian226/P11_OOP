package com.mygdx.game.entity;

public class NeutralObject extends StaticObject {
    public NeutralObject(){
        super(null, 0, 0);
    }
    public NeutralObject(String textureFile){
        super(textureFile, 0, 0);
    }
    public NeutralObject(String textureFile, float x, float y){
        super(textureFile, x, y);
    }
}
