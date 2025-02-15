package com.mygdx.game.entity;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

public class Character extends MoveableObject {
    private Texture texture;
    private boolean flipX;
    private Body body;

    public Character(){
        super(null, 0, 0, 0);
    }
    public Character(String textureFile){
        super(textureFile, 0, 0, 0);
    }
    public Character(World world, String textureFile, float x, float y, float speed){
        super(textureFile, x, y, speed);
        this.texture = new Texture(textureFile);
        this.body = createBox(world, x, y, texture.getWidth(), texture.getHeight(), false);
    }
    public boolean getFlipX() {
        return flipX;
    }
    public void setFlipX(boolean flipX) {
        this.flipX = flipX;
    }
    public Body getBody() {
        return body;
    }
    public void setBody(Body body) {
        this.body = body;
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
    @Override
    public void dispose() {
        texture.dispose();
    }
    public void setDirection(float directionX, float directionY){};
    public void updateRotation(float deltaX, float deltaY){};
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
            bdef.position.set(x/32, y/32);
            bdef.fixedRotation = true;
        }
        body = world.createBody(bdef);
        body.createFixture(shape, 1.0f);
        shape.dispose();
        return body;
    };
}
