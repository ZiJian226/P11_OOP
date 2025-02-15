//package com.mygdx.game;
//
//import com.badlogic.gdx.Gdx;
//import com.badlogic.gdx.Input;
//import com.badlogic.gdx.graphics.Color;
//import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
//
//public class Circle extends Entity{
//    private Color color;
//    private float radius;
//
//    public Circle(){
//        super(0, 0, 0);
//        this.radius = 0;
//    }
//    public Circle(Color color, float radius, float x, float y, float speed){
//        super(x, y, speed);
//        this.color = color;
//        this.radius = radius;
//    }
//    public void setRadius(float radius){
//        this.radius = radius;
//    }
//    public float getRadius(){
//        return radius;
//    }
//    @Override
//    public void draw(ShapeRenderer shape){
//        shape.setColor(color);
//        shape.circle(getX(), getY(), getRadius());
//    }
//    public void moveUserControlled() {
//        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
//            if (getY() <= Gdx.graphics.getHeight() - getRadius()) {
//                setY(getY() + getSpeed() * Gdx.graphics.getDeltaTime());
//            }
//        }
//        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
//            if (getY() >= 0 + getRadius()) {
//                setY(getY() - getSpeed() * Gdx.graphics.getDeltaTime());
//            }
//        }
//    }
//    public void moveAIControlled(){
//        if (getY() >= Gdx.graphics.getHeight() - getRadius()) {
//            setSpeed(-Math.abs(getSpeed()));
//        } else if (getY() <= getRadius()) {
//            setSpeed(Math.abs(getSpeed()));
//        }
//        setY(getY() + getSpeed() * Gdx.graphics.getDeltaTime());
//    };
//    public void update() {
//        moveUserControlled();
//        System.out.println("Circle at (" + getX() + "," + getY() + ") position");
//    }
//}
