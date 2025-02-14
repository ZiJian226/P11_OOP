package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Player extends Character{
    private float score, health;
    private List<Bullet> bullets;
    private Vector2 playerPosition, mousePosition, direction;

    public Player(){
        super(null, 0, 0, 0);
    }
    public Player(String textureFile){
        super(textureFile, 0, 0, 0);
    }
    public Player(String textureFile, float x, float y, float speed){
        super(textureFile, x, y, speed);
        bullets = new ArrayList<>();
        direction = new Vector2(0,0);
        playerPosition = new Vector2(getX() + getWidth() / 2, getY() + getHeight() / 2);
        mousePosition = new Vector2(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY());
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
    public List<Bullet> getBullets() {
        return bullets;
    }

    @Override
    public void moveUserControlled() {
        if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
            spawnBullet();
        }
        updateRotation();

        MovementManager movementManager = new MovementManager();
        movementManager.manualMovement(this);
    }

    public void updateRotation() {
        playerPosition.set(getX() + getWidth() / 4, getY() + getHeight() / 2);
        mousePosition.set(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY());
        direction.set(mousePosition).sub(playerPosition).nor();
        float rotation = (float) Math.toDegrees(Math.atan2(direction.y, direction.x));
        boolean flipX = false;

        if (rotation > 90 || rotation < -90) {
            flipX = true;
            rotation = rotation - 180;
        }

        setRotation(rotation);
        setFlipX(flipX);
    }

    public void spawnBullet() {
        playerPosition.set(getX() + getWidth() / 4, getY() + getHeight() / 2);
        Bullet bullet = new Bullet("bullet.png", playerPosition.x, playerPosition.y, getSpeed() * 2);
        bullet.setDirection(direction.x, direction.y);
        bullets.add(bullet);;
    }
    public void drawBullets(SpriteBatch batch) {
        for (Bullet bullet : bullets) {
            bullet.draw(batch);
        }
    }
    public void updateBullets() {
        Iterator<Bullet> bulletIterator = bullets.iterator();
        while (bulletIterator.hasNext()) {
            Bullet bullet = bulletIterator.next();
            bullet.update();
            if (bullet.isOutOfScreen()) {
                bullet.dispose();
                bulletIterator.remove();
            }
        }
    }

}
