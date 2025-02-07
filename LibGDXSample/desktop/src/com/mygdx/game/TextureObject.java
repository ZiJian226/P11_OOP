package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class TextureObject extends Entity{
    private Texture tex;

    public TextureObject(){
        super(0, 0, 0);
        this.tex = null;
    }
    public TextureObject(String textureFile){
        super(0, 0, 0);
        this.tex = new Texture(textureFile);
    }
    public TextureObject(String textureFile, float x, float y, float speed) {
        super (x, y, speed);
        this.tex = new Texture(textureFile);
    }
    public float getWidth(){
        return tex.getWidth();
    }
    public float getHeight(){
        return tex.getHeight();
    }
    @Override
    public void draw(SpriteBatch batch){
        batch.draw(tex, getX(), getY());
    }
    public void moveUserControlled(){};
    public void moveAIControlled(){};
    public void dispose(){
        tex.dispose();
    }
    public void update() {
        moveAIControlled();
        moveUserControlled();
        System.out.println("TextureObject of " + tex.toString() + " at " + getX() + "," + getY() + " position");
    }

}
