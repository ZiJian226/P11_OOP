package io.github.testgame.lwjgl3.abstractEngine;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.physics.box2d.Body;
import io.github.testgame.lwjgl3.entity.iMoveable;

import java.util.Set;

public class MovementManager{
    private IOManager ioManager;

    public MovementManager(){}

    public MovementManager(IOManager ioManager) {
        this.ioManager = ioManager;
    }

    // General movement as user moveable object, linked to IOManager
    public void manualMovement(iMoveable entity) {
        if (ioManager == null) {
            System.out.println("IOManager is null!"); // Debugging message
            return; // Prevent NullPointerException
        }
        Set<Integer> keysPressed = ioManager.getKeysPressed();
        float xForce = 0;
        float yForce = 0;

        if (keysPressed.contains(Input.Keys.W) && entity.getY() <= Gdx.graphics.getHeight() - entity.getHeight()) {
            yForce += 1;
        }
        if (keysPressed.contains(Input.Keys.S) && entity.getY() >= 0) {
            yForce -= 1;
        }
        if (keysPressed.contains(Input.Keys.A) && entity.getX() >= 0) {
            xForce -= 1;
        }
        if (keysPressed.contains(Input.Keys.D) && entity.getX() <= Gdx.graphics.getWidth() - entity.getWidth()) {
            xForce += 1;
        }
        if (keysPressed.contains(Input.Keys.SPACE) && ((keysPressed.contains(Input.Keys.A) || (keysPressed.contains(Input.Keys.D))))){
            entity.getBody().applyForceToCenter(xForce * entity.getForce(), 0, false);
        }
        if (keysPressed.contains(Input.Keys.SPACE) && ((keysPressed.contains(Input.Keys.W) || (keysPressed.contains(Input.Keys.S))))){
            entity.getBody().applyForceToCenter(0, yForce * entity.getForce(), false);
        }

        // Apply force to the object such that user will not stuck
        entity.getBody().setLinearVelocity(xForce * entity.getSpeed(), yForce * entity.getSpeed());

    }

    // General movement as tracker toward its target (basic AI bot as enemy)
    public void autoMovement(iMoveable entity, iMoveable target) {
        if (target != null) {
            float deltaX = target.getBody().getPosition().x - entity.getBody().getPosition().x;
            float deltaY = target.getBody().getPosition().y - entity.getBody().getPosition().y;
            float distance = (float) Math.sqrt(deltaX * deltaX + deltaY * deltaY);

            if (distance > 0) {
                float forceTowardsPlayer = 5.0f;
                float moveX = (deltaX / distance) * forceTowardsPlayer;
                float moveY = (deltaY / distance) * forceTowardsPlayer;

                entity.getBody().applyForceToCenter(moveX, moveY, true);

                float dampingFactor = 0.98f;
                Body body = entity.getBody();

                // Damping factor to prevent the object from moving too fast
                body.setLinearVelocity(body.getLinearVelocity().x * dampingFactor, body.getLinearVelocity().y * dampingFactor);

                entity.setX(body.getPosition().x * 32);
                entity.setY(body.getPosition().y * 32);

                entity.updateRotation(deltaX, deltaY);
            }
        }
    }
}
