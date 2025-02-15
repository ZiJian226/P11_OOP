package com.mygdx.game.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.mygdx.game.abstractEngine.IOManager;
import com.mygdx.game.abstractEngine.MovementManager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Player extends Character {
    private IOManager ioManager;
    private float score, health, force;
    private List<Bullet> bullets;
    private Vector2 playerPosition, mousePosition, direction;


    public Player(){
        super(null, null, 0, 0, 0);
    }
    public Player(String textureFile){
        super(null, textureFile, 0, 0, 0);
    }
    public Player(World world, String textureFile, float x, float y, float force, float speed, IOManager ioManager) {
        super(world, textureFile, x, y, speed);
        this.force = force;
        this.ioManager = ioManager;
        bullets = new ArrayList<>();
        direction = new Vector2(0, 0);
        playerPosition = new Vector2(getX() + getWidth() / 2, getY() + getHeight() / 2);
        mousePosition = new Vector2(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY());
        setBody(createBox(world, x, y, getWidth(), getHeight(), false));
        getBody().setUserData(this);
    }
    public void setDirection(float directionX, float directionY){
        direction.set(directionX, directionY);
    }
    public Vector2 getPosition(){
        return playerPosition;
    }
    @Override
    public float getForce(){
        return force;
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

        MovementManager movementManager = new MovementManager(ioManager);
        movementManager.manualMovement(this);
    }

    public void updateRotation() {
        playerPosition.set(getBody().getPosition().x*32, getBody().getPosition().y*32);
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
        playerPosition.set(getBody().getPosition().x*32, getBody().getPosition().y*32);
        Bullet bullet = new Bullet("bullet.png", playerPosition.x, playerPosition.y, getSpeed() * 200);
        bullet.setDirection(direction.x, direction.y);
        bullets.add(bullet);;
    }
    public void drawBullets(SpriteBatch batch, ShapeRenderer shape) {
        for (Bullet bullet : bullets) {
            bullet.draw(batch, shape);
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
