package com.mygdx.game.entity;

import com.badlogic.gdx.physics.box2d.*;
import com.mygdx.game.abstractEngine.IOManager;
import com.mygdx.game.abstractEngine.MovementManager;

public class Enemy extends Character {
    private IOManager ioManager;
    private float damage;
    private Player player;

    public Enemy(){
        super(null,null, 0, 0, 0);
    }
    public Enemy(String textureFile){
        super(null,textureFile, 0, 0, 0);
    }
    public Enemy(World world, String textureFile, float x, float y, float speed) {
        super(world,textureFile, x, y, speed);
    }
    public float getDamage(){
        return damage;
    }
    public void setDamage(float damage){
        this.damage = damage;
    }
    public void setPlayer(Player player){
        this.player = player;
    }
    public Player getPlayer(){
        return player;
    }
    @Override
    public void moveAIControlled(){
        MovementManager movementManager = new MovementManager(ioManager);
        movementManager.autoMovement(this, player);
    }
    @Override
    public void updateRotation(float deltaX, float deltaY) {
        float rotation = (float) Math.toDegrees(Math.atan2(deltaY, deltaX));
        boolean flipX = false;
        setFlipX(false);

        if (rotation > 90 || rotation < -90) {
            flipX = true;
            rotation = rotation - 180;
        }

        setRotation(rotation);
        setFlipX(flipX);
    }
}
