package com.mygdx.game.abstractEngine;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.mygdx.game.iMoveable;

import java.util.Set;

public class MovementManager{
    private IOManager ioManager;

    public MovementManager(IOManager ioManager) {
        this.ioManager = ioManager;
    }
    public void manualMovement(iMoveable entity) {
        if (ioManager == null) {
            System.out.println("IOManager is null!"); // Debugging message
            return; // Prevent NullPointerException
        }
        Set<Integer> keysPressed = ioManager.getKeysPressed();
        float xForce = 0;
        float yForce = 0;

        if (keysPressed.contains(Input.Keys.UP) && entity.getY() <= Gdx.graphics.getHeight() - entity.getHeight()) {
            yForce += 1;

        }
        if (keysPressed.contains(Input.Keys.DOWN) && entity.getY() >= 0) {
            yForce -= 1;
        }
        if (keysPressed.contains(Input.Keys.LEFT) && entity.getX() >= 0) {
            xForce -= 1;
        }
        if (keysPressed.contains(Input.Keys.RIGHT) && entity.getX() <= Gdx.graphics.getWidth() - entity.getWidth()) {
            xForce += 1;
        }
        if (keysPressed.contains(Input.Keys.SPACE) && ((keysPressed.contains(Input.Keys.LEFT) || (keysPressed.contains(Input.Keys.RIGHT))))){
            entity.getBody().applyForceToCenter(xForce * entity.getForce(), 0, false);
        }
        if (keysPressed.contains(Input.Keys.SPACE) && ((keysPressed.contains(Input.Keys.UP) || (keysPressed.contains(Input.Keys.DOWN))))){
            entity.getBody().applyForceToCenter(0, yForce * entity.getForce(), false);
        }

        entity.getBody().setLinearVelocity(xForce * entity.getSpeed(), yForce * entity.getSpeed());

    }

    public void autoMovement(iMoveable entity, iMoveable target) {
        float targetX = target.getPosition().x;
        float targetY = target.getPosition().y;
        float deltaX = targetX - entity.getX();
        float deltaY = targetY - entity.getY();
        float distance = (float) Math.sqrt(deltaX * deltaX + deltaY * deltaY);

        if (distance > 0) {
            float moveX = (deltaX / distance) * entity.getSpeed() * Gdx.graphics.getDeltaTime();
            float moveY = (deltaY / distance) * entity.getSpeed() * Gdx.graphics.getDeltaTime();
//            entity.setX(entity.getX() + moveX);
//            entity.setY(entity.getY() + moveY);
            entity.getBody().setLinearVelocity(moveX, moveY);
            entity.updateRotation(deltaX, deltaY);
        }
    }
}
