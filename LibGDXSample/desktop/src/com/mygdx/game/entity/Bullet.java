package com.mygdx.game.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class Bullet extends NonCharacter {
    private float directionX, directionY, rotation;  //directionX and directionY refer to player facing direction
    private Texture texture;

    public Bullet(){
        super(null, 0, 0, 0);
    }
    public Bullet(String textureFile){
        super(textureFile, 0, 0, 0);
    }
    public Bullet(String textureFile, float x, float y, float speed){
        super(textureFile, x, y, speed);
        this.texture = new Texture(textureFile);
    }
    @Override
    public void setDirection(float directionX, float directionY){
        this.directionX = directionX;
        this.directionY = directionY;
        this.rotation = (float) Math.toDegrees(Math.atan2(directionY, directionX));
    }
    @Override
    public void moveAIControlled() {
        float moveX = directionX * getSpeed() * Gdx.graphics.getDeltaTime();
        float moveY = directionY * getSpeed() * Gdx.graphics.getDeltaTime();
        setX(getX() + moveX);
        setY(getY() + moveY);

        if (getX() < 0 || getX() > Gdx.graphics.getWidth() || getY() < 0 || getY() > Gdx.graphics.getHeight()) {
            dispose();
        }
    }

    public boolean isOutOfScreen() {
        return getX() < 0 || getX() > Gdx.graphics.getWidth() || getY() < 0 || getY() > Gdx.graphics.getHeight();
    }

    @Override
    public void draw(SpriteBatch batch, ShapeRenderer shape) {
        batch.draw(texture, getX(), getY(), (float) texture.getWidth() / 2, (float) texture.getHeight() / 2, texture.getWidth(), texture.getHeight(), 1, 1, rotation, 0, 0, texture.getWidth(), texture.getHeight(), false, false);
    }

    @Override
    public void dispose() {
        texture.dispose();
    }
}
