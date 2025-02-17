package com.mygdx.game.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.physics.box2d.Body;

public class StaticObject extends TextureObject {

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
    @Override
    public void draw(SpriteBatch batch, ShapeRenderer shape) {
        batch.draw(getTexture(), getBody().getPosition().x*32-getWidth()/2,
                getBody().getPosition().y*32-getHeight()/2, getWidth() / 2,
                getHeight() / 2, getWidth(), getHeight(), 1F, 1,
                (float) Math.toDegrees(getBody().getAngle()), 0, 0, getTexture().getWidth()
                , getTexture().getHeight(), false, false);
    }
}
