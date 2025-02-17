package io.github.testgame.lwjgl3.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

public class Character extends MoveableObject {
    private boolean flipX;

    public Character(){
        super(null, 0, 0, 0);
    }
    public Character(String textureFile){
        super(textureFile, 0, 0, 0);
    }
    public Character(String textureFile, float x, float y, float speed){
        super(textureFile, x, y, speed);
    }
    public Body getBody(){
        return null;
    }
    public boolean getFlipX() {
        return flipX;
    }
    public void setFlipX(boolean flipX) {
        this.flipX = flipX;
    }
    public float getForce(){
        return 0;
    }
    public Vector2 getPosition(){
        return null;
    }
    @Override
    public void draw(SpriteBatch batch, ShapeRenderer shape) {
        batch.draw(getTexture(), getBody().getPosition().x*32-getWidth()/2, getBody().getPosition().y*32-getHeight()/2, getWidth() / 2, getHeight() / 2, getWidth(), getHeight(), 1, 1, getRotation(), 0, 0, getTexture().getWidth(), getTexture().getHeight(), getFlipX(), false);
    }
    public void updateRotation(float deltaX, float deltaY){};
}
