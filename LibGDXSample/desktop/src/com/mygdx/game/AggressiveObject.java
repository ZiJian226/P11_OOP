package com.mygdx.game;

public class AggressiveObject extends StaticObject{
    protected float damage;                 // For collision
    public AggressiveObject(){
        super(null, 0, 0);
    }
    public AggressiveObject(String textureFile){
        super(textureFile, 0, 0);
    }
    public AggressiveObject(String textureFile, float x, float y){
        super(textureFile, x, y);
    }
    public float getDamage(){
        return damage;
    }
    public void setDamage(float damage){
        this.damage = damage;
    }
    public void result(){
        // player health minus damage
    }
}
