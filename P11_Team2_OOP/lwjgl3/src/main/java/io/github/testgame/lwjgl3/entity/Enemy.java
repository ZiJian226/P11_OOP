package io.github.testgame.lwjgl3.entity;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import io.github.testgame.lwjgl3.abstractEngine.*;

public class Enemy extends Character {
    private MovementManager movementManager;
    private Player player;
    private Body body;
    private Vector2 position;

    public Enemy(){
        super(null, 0, 0, 0);
    }
    public Enemy(String textureFile){
        super(textureFile, 0, 0, 0);
    }
    public Enemy(World world, String textureFile, float x, float y, float speed) {
        super(textureFile, x, y, speed);
        this.body = createBox(world, x, y, getWidth(), getHeight(), false);
        this.body.setUserData(this);
        this.position = new Vector2(getX() + getWidth() / 2, getY() + getHeight() / 2);
    }
    @Override
    public Vector2 getPosition(){
        return position;
    }
    public void setPlayer(Player player){
        this.player = player;
    }
    @Override
    public float getForce() {
        return 0;
    }
    @Override
    public Body getBody(){
        return body;
    }
    @Override
    public void moveAIControlled(){
        movementManager = new MovementManager();
        movementManager.autoMovement(this, player);
    }
}
