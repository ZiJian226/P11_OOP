package io.github.testgame.lwjgl3.entity;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.physics.box2d.*;

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
    @Override
    public void update() {
        moveAIControlled();
        moveUserControlled();
//        System.out.println("TextureObject of " + tex.toString() + " at " + getX() + "," + getY() + " position");
    }
    @Override
    public Body getBody(){
        return null;
    }
    public void moveUserControlled(){};
    public void moveAIControlled(){};
    public Body createBox(World world, float x, float y, float width, float height, boolean isStatic){
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(width/2/32, height/2/32);
        Body pBody = CreateBody(world, shape, isStatic, x, y);
        return pBody;
    }
    public static Body CreateBody(World world, Shape shape, boolean staticobj, float x, float y){
        Body body;
        BodyDef bdef = new BodyDef();
        if(staticobj){
            bdef.type = BodyDef.BodyType.StaticBody;
        } else {
            bdef.type = BodyDef.BodyType.DynamicBody;
        }
        bdef.position.set(x/32, y/32);
        bdef.fixedRotation = true;
        body = world.createBody(bdef);
        body.createFixture(shape, 1.0f);
        shape.dispose();
        return body;
    };
}
