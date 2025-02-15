package com.mygdx.game;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

public interface iMoveable {
    public float getX();
    public void setX(float x);
    public float getY();
    public void setY(float y);
    public float getSpeed();
    public void setSpeed(float speed);
    public float getWidth();
    public float getHeight();
    public void setDirection(float directionX, float directionY);
    public void updateRotation(float deltaX, float deltaY);

    public Body getBody();
    public float getForce();
    public Vector2 getPosition();
}
