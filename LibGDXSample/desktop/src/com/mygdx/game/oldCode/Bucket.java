//package com.mygdx.game;
//
//import com.badlogic.gdx.Gdx;
//import com.badlogic.gdx.Input;
//
//public class Bucket extends TextureObject{
//    public Bucket(){
//        super(null, 0,0,0);
//    }
//    public Bucket(String textureFile){
//        super(textureFile, 0, 0, 0);
//    }
//    public Bucket(String textureFile, float x, float y, float speed){
//        super(textureFile, x, y, speed);
//    }
//    @Override
//    public void moveUserControlled(){
//        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
//            if (getX() >= 0) {
//                setX(getX() - getSpeed() * Gdx.graphics.getDeltaTime());
//            }
//        }
//        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
//            if (getX() <= Gdx.graphics.getWidth() - getWidth()) {
//                setX(getX() + getSpeed() * Gdx.graphics.getDeltaTime());
//            }
//        }
//    }
//}
