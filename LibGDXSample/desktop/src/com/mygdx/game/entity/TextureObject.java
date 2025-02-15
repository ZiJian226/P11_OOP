package com.mygdx.game.entity;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class TextureObject extends Entity {
    private Texture tex;

    public TextureObject(){
        super(0, 0);
        this.tex = null;
    }
    public TextureObject(String textureFile){
        super(0, 0);
        this.tex = new Texture(textureFile);
    }
    public TextureObject(String textureFile, float x, float y) {
        super (x, y);
        this.tex = new Texture(textureFile);
    }
    protected Texture getTexture(){
        return tex;
    }
    public float getWidth(){
        return tex.getWidth();
    }
    public float getHeight(){
        return tex.getHeight();
    }
    @Override
    public void draw(SpriteBatch batch, ShapeRenderer shape){
        batch.draw(tex, getX(), getY());
    }
    @Override
    public void dispose(){
        tex.dispose();
    }
    public void moveUserControlled(){};
    public void moveAIControlled(){};
    public void update() {
        moveAIControlled();
        moveUserControlled();
//        System.out.println("TextureObject of " + tex.toString() + " at " + getX() + "," + getY() + " position");
    }
}
