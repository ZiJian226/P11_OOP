package com.mygdx.game;

public class Enemy extends Character{
    protected float damage;
    public Enemy(){
        super(null, 0, 0, 0);
    }
    public Enemy(String textureFile){
        super(textureFile, 0, 0, 0);
    }
    public Enemy(String textureFile, float x, float y, float speed){
        super(textureFile, x, y, speed);
    }
    public float getDamage(){
        return damage;
    }
    public void setDamage(float damage){
        this.damage = damage;
    }
    public void moveAIControlled(){}
}
