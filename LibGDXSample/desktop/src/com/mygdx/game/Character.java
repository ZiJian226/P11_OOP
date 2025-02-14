package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Character extends MoveableObject{
    private Texture texture;
    private boolean flipX;

    public Character(){
        super(null, 0, 0, 0);
    }
    public Character(String textureFile){
        super(textureFile, 0, 0, 0);
    }
    public Character(String textureFile, float x, float y, float speed){
        super(textureFile, x, y, speed);
        this.texture = new Texture(textureFile);
    }
    public boolean getFlipX() {
        return flipX;
    }
    public void setFlipX(boolean flipX) {
        this.flipX = flipX;
    }

    @Override
    public void draw(SpriteBatch batch) {
        batch.draw(texture, getX(), getY(), getWidth() / 2, getHeight() / 2, getWidth(), getHeight(), 1, 1, getRotation(), 0, 0, texture.getWidth(), texture.getHeight(), getFlipX(), false);
    }

    @Override
    public void dispose() {
        texture.dispose();
    }

    public void setDirection(float directionX, float directionY){};
    public void updateRotation(float deltaX, float deltaY){};
}
