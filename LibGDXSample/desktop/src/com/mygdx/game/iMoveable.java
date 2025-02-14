package com.mygdx.game;

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
}
