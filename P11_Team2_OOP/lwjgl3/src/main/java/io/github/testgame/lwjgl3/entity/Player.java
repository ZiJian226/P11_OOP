package io.github.testgame.lwjgl3.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import io.github.testgame.lwjgl3.abstractEngine.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static io.github.testgame.lwjgl3.abstractEngine.EntityManager.isOutOfScreen;

public class Player extends Character {
    private IOManager ioManager;
    private float score, health, force;
    private List<Bullet> bullets;
    private Vector2 playerPosition, mousePosition, direction;
    private Body body;

    public Player(){
        super(null, 0, 0, 0);
    }
    public Player(String textureFile){
        super(textureFile, 0, 0, 0);
    }
    public Player(World world, String textureFile, float x, float y, float force, float speed, IOManager ioManager) {
        super(textureFile, x, y, speed);
        this.force = force;
        this.ioManager = ioManager;
        this.health = 10;
        this.score = 0;
        bullets = new ArrayList<>();
        direction = new Vector2(0, 0);
        playerPosition = new Vector2(getX() + getWidth() / 2, getY() + getHeight() / 2);
        mousePosition = new Vector2(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY());
        this.body = createBox(world, x, y, getWidth(), getHeight(), false);
        this.body.setUserData(this);
    }
    public Vector2 getDirection(){
        return direction;
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
    @Override
    public Body getBody(){
        return body;
    }
    @Override
    public void moveUserControlled() {
        if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
            spawnBullet();
        }
        playerPosition.set(getBody().getPosition().x*32, getBody().getPosition().y*32);
        mousePosition.set(Gdx.input.getX() + playerPosition.x - (float) Gdx.graphics.getWidth() / 2,
            Gdx.graphics.getHeight() - Gdx.input.getY() + playerPosition.y - (float) Gdx.graphics.getHeight() / 2);
        direction.set(mousePosition).sub(playerPosition).nor();

        updateRotation(direction.x, direction.y);

        MovementManager movementManager = new MovementManager(ioManager);
        movementManager.manualMovement(this);
    }

    // The method below is used for player to spawn bullet and handle the bullet spawned

    public void spawnBullet() {
        playerPosition.set(getBody().getPosition().x*32, getBody().getPosition().y*32);
        Bullet bullet = new Bullet(body.getWorld(), "bullet.png", this, getSpeed() * 200);
        bullet.setDirection(direction);
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
            if (isOutOfScreen(bullet, this)) {
                bullet.getBody().getWorld().destroyBody(bullet.getBody());
                bullet.dispose();
                bulletIterator.remove();
            }
        }
    }

    public List<Bullet> getBullets() {
        return new ArrayList<>(bullets);
    }

    public void removeBullet(Bullet bullet) {
        bullets.remove(bullet);
    }
}
