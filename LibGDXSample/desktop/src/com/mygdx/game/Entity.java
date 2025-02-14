package com.mygdx.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public abstract class Entity{
    private float x, y;

    public Entity(){
        this.x = 0;
        this.y = 0;
    }
    public Entity(float x, float y){
        this.x = x;
        this.y = y;
    }
    public void setX(float x){
        this.x = x;
    }
    public float getX(){
        return x;
    }
    public void setY(float y){
        this.y = y;
    }
    public float getY(){
        return y;
    }
    public void draw(SpriteBatch batch){};
    public void dispose(){};
//    public void draw(ShapeRenderer shape){};
    public abstract void update();
}
