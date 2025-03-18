package io.github.testgame.lwjgl3.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import io.github.testgame.lwjgl3.GameMaster;
import io.github.testgame.lwjgl3.abstractEngine.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static io.github.testgame.lwjgl3.engineHelper.EntityHelper.isOutOfScreen;

public class Player extends Character {
    private IOManager ioManager;
    private AudioManager audioManager;
    private float score, health, force;
    private List<Bubble> bubbles;
    private Vector2 playerPosition, mousePosition, direction;
    private Body body;

    public Player(){
        super(null, 0, 0, 0);
    }
    public Player(String textureFile){
        super(textureFile, 0, 0, 0);
    }
    public Player(World world, String textureFile, float x, float y, float force, float speed, IOManager ioManager, AudioManager audioManager) {
        super(textureFile, x, y, speed);
        this.force = force;
        this.ioManager = ioManager;
        this.health = 10;
        this.score = 0;
        bubbles = new ArrayList<>();
        direction = new Vector2(0, 0);
        playerPosition = new Vector2(getX() + getWidth() / 2, getY() + getHeight() / 2);
        mousePosition = new Vector2(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY());
        this.body = createBox(world, x, y, getWidth(), getHeight(), false);
        this.body.setUserData(this);
        this.audioManager = audioManager;
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
            spawnBubble();
        }
        playerPosition.set(getBody().getPosition().x*32, getBody().getPosition().y*32);
        mousePosition.set(Gdx.input.getX() + playerPosition.x - (float) Gdx.graphics.getWidth() / 2,
            Gdx.graphics.getHeight() - Gdx.input.getY() + playerPosition.y - (float) Gdx.graphics.getHeight() / 2);
        direction.set(mousePosition).sub(playerPosition).nor();

        updateRotation(direction.x, direction.y);

        MovementManager movementManager = new MovementManager(ioManager);
        movementManager.manualMovement(this);
    }

    // The method below is used for player to spawn bubble and handle the bubble spawned

    public void spawnBubble() {
        playerPosition.set(getBody().getPosition().x * 32, getBody().getPosition().y * 32);
        Bubble bubble = new Bubble(body.getWorld(), "bubble.png", this, getSpeed() * 200);
        bubble.setDirection(direction);
        bubbles.add(bubble);
        audioManager.playSoundEffect("bubble");
    }
    public void drawBubbles(SpriteBatch batch, ShapeRenderer shape) {
        for (Bubble bubble : bubbles) {
            bubble.draw(batch, shape);
        }
    }
    public void updateBubbles() {
        Iterator<Bubble> bubbleIterator = bubbles.iterator();
        while (bubbleIterator.hasNext()) {
            Bubble bubble = bubbleIterator.next();
            bubble.update();
            if (isOutOfScreen(bubble, this)) {
                bubble.getBody().getWorld().destroyBody(bubble.getBody());
                bubble.dispose();
                bubbleIterator.remove();
            }
        }
    }

    public List<Bubble> getBubbles() {
        return new ArrayList<>(bubbles);
    }

    public void removeBubble(Bubble bubble) {
        bubbles.remove(bubble);
    }
}
