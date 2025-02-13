package com.mygdx.game;

public class Player extends Character{
    private float score;
    private float health;
    public Player(){
        super(null, 0, 0, 0);
    }
    public Player(String textureFile){
        super(textureFile, 0, 0, 0);
    }
    public Player(String textureFile, float x, float y, float speed){
        super(textureFile, x, y, speed);
    }
    public float getScore(){
        return score;
    }
    public void setScore(float score){
        this.score = score;
    }
    public float getHealth(){
        return health;
    }
    public void setHealth(float health){
        this.health = health;
    }
    public void moveUserControlled(){
        // Move the player based on user input
    }
}
