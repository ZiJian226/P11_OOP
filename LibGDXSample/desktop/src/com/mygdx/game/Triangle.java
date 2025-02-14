//package com.mygdx.game;
//
//import com.badlogic.gdx.Gdx;
//import com.badlogic.gdx.Input;
//import com.badlogic.gdx.graphics.Color;
//import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
//
//public class Triangle extends Entity{
//    private Color color;
//    private float size;
//    public Triangle(){
//        super(0, 0, 0);
//    }
//    public Triangle(Color color, float x, float y, float speed){
//        super(0, 0, 0);
//        this.color = color;
//    }
//    public Triangle(Color color, float x, float y, float speed, float size){
//        super(x, y, speed);
//        this.color = color;
//        this.size = size;
//    }
//
//    @Override
//    public void draw(ShapeRenderer shape){
//        shape.setColor(color);
//        shape.triangle(getX() - size, getY() - size, getX() + size, getY() - size, getX(), getY() + size);
//    }
//
//    public void moveUserControlled() {
//        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
//            if (getX() >= size) {
//                setX(getX() - getSpeed() * Gdx.graphics.getDeltaTime());
//            }
//        }
//        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
//            if (getX() <= Gdx.graphics.getWidth() - size) {
//                setX(getX() + getSpeed() * Gdx.graphics.getDeltaTime());
//            }
//        }
//    }
//
//    public void moveAIControlled(){
//        if (getX() >= Gdx.graphics.getWidth() - size) {
//            setSpeed(-Math.abs(getSpeed()));
//        } else if (getX() <= size) {
//            setSpeed(Math.abs(getSpeed()));
//        }
//        setX(getX() + getSpeed() * Gdx.graphics.getDeltaTime());
//    };
//
//    public void update() {
//        moveUserControlled();
//        System.out.println("Triangle at (" + getX() + "," + getY() + ") position");
//    }
//}
