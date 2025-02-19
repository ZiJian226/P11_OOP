package io.github.testgame.lwjgl3.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.physics.box2d.*;

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
    public abstract void draw(SpriteBatch batch, ShapeRenderer shape);
    public abstract void dispose();
    public abstract void update();
    public abstract Body getBody();
}
