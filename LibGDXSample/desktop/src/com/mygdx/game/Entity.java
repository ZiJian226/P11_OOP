package com.mygdx.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public abstract class Entity implements iMoveable{
    protected float x, y, speed;

    public Entity(){
        this.x = 0;
        this.y = 0;
        this.speed = 0;
    }
    public Entity(float x, float y, float speed){
        this.x = x;
        this.y = y;
        this.speed = speed;
    }
    public Entity(float x, float y, float speed, MoveType moveType){
        this.x = x;
        this.y = y;
        this.speed = speed;
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
    public void setSpeed(float speed){
        this.speed = speed;
    }
    public float getSpeed(){
        return speed;
    }
    public void draw(ShapeRenderer shape){};
    public void draw(SpriteBatch batch){};
    public abstract void update();
}
