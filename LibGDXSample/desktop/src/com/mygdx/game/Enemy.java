package com.mygdx.game;

import com.badlogic.gdx.Gdx;

public class Enemy extends Character{
    private float damage;
    private Player player;

    public Enemy(){
        super(null, 0, 0, 0);
    }
    public Enemy(String textureFile){
        super(textureFile, 0, 0, 0);
    }
    public Enemy(String textureFile, float x, float y, float speed){
        super(textureFile, x, y, speed);
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
//        if (isCollisionWithPlayer()) {
//            System.out.println("Collision detected!");
//            pushPlayerAway((player.getX()-getX()) / 3, (player.getY()-getY()) / 3);
//        }

        MovementManager movementManager = new MovementManager();
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

//    public boolean isCollisionWithPlayer() {
//        float enemyX = getX();
//        float enemyY = getY();
//        float enemyWidth = getWidth();
//        float enemyHeight = getHeight();
//
//        float playerX = player.getX();
//        float playerY = player.getY();
//        float playerWidth = player.getWidth();
//        float playerHeight = player.getHeight();
//
//        return enemyX < playerX + playerWidth &&
//                enemyX + enemyWidth > playerX &&
//                enemyY < playerY + playerHeight &&
//                enemyY + enemyHeight > playerY;
//    }
//    public void pushPlayerAway(float deltaX, float deltaY) {
//        float distance = (float) Math.sqrt(deltaX * deltaX + deltaY * deltaY);
//        if (distance > 0) {
//            float pushX = (deltaX / distance) * 10;
//            float pushY = (deltaY / distance) * 10;
//            float newX = player.getX() + pushX;
//            float newY = player.getY() + pushY;
//
//            if (newX < 0) newX = 0;
//            if (newX > Gdx.graphics.getWidth() - player.getWidth()) newX = Gdx.graphics.getWidth() - player.getWidth();
//            if (newY < 0) newY = 0;
//            if (newY > Gdx.graphics.getHeight() - player.getHeight()) newY = Gdx.graphics.getHeight() - player.getHeight();
//
//            player.setX(newX);
//            player.setY(newY);
//        }
//    }
}
