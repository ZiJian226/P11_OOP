package io.github.testgame.lwjgl3.engineHelper;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.Body;
import io.github.testgame.lwjgl3.abstractEngine.IOManager;
import io.github.testgame.lwjgl3.abstractEngine.MovementManager;
import io.github.testgame.lwjgl3.entity.iMoveable;

import java.util.Map;
import java.util.Set;

public class MovementHelper extends MovementManager {
    private IOManager ioManager;
    private Map<String, Integer> keyMapping;

    public MovementHelper() {
        super();
    }

    public MovementHelper(IOManager ioManager, Map<String, Integer> keyMapping) {
        super(ioManager, keyMapping);
        this.ioManager = ioManager;
        this.keyMapping = keyMapping;
    }

    // General movement as user moveable object, linked to IOManager
    @Override
    public void manualMovement(iMoveable entity) {
        if (ioManager == null || keyMapping == null) {
            System.out.println("IOManager or KeyMapping is null!"); // Debugging message
            return;
        }

        Set<Integer> keysPressed = ioManager.getKeysPressed();
        float xForce = 0;
        float yForce = 0;

        if (keysPressed.contains(keyMapping.get("MOVE_UP")) && entity.getY() <= Gdx.graphics.getHeight() - entity.getHeight()) {
            yForce += 1;
        }
        if (keysPressed.contains(keyMapping.get("MOVE_DOWN")) && entity.getY() >= 0) {
            yForce -= 1;
        }
        if (keysPressed.contains(keyMapping.get("MOVE_LEFT")) && entity.getX() >= 0) {
            xForce -= 1;
        }
        if (keysPressed.contains(keyMapping.get("MOVE_RIGHT")) && entity.getX() <= Gdx.graphics.getWidth() - entity.getWidth()) {
            xForce += 1;
        }

        if (keysPressed.contains(keyMapping.get("JUMP")) && ( keysPressed.contains(keyMapping.get("MOVE_LEFT")) || keysPressed.contains(keyMapping.get("MOVE_RIGHT")) )){
            entity.getBody().applyForceToCenter(xForce * entity.getForce(), 0, false);
        }

        if (keysPressed.contains(keyMapping.get("JUMP")) && ( keysPressed.contains(keyMapping.get("MOVE_UP")) || keysPressed.contains(keyMapping.get("MOVE_DOWN")) )){
            entity.getBody().applyForceToCenter(0, yForce * entity.getForce(), false);
        }

        // Apply movement force
        entity.getBody().setLinearVelocity(xForce * entity.getSpeed(), yForce * entity.getSpeed());
    }

    // General movement as tracker toward its target (basic AI bot as enemy)
    @Override
    public void autoMovement(iMoveable entity, iMoveable target) {
        if (target != null) {
            float deltaX = target.getBody().getPosition().x - entity.getBody().getPosition().x;
            float deltaY = target.getBody().getPosition().y - entity.getBody().getPosition().y;
            float distance = (float) Math.sqrt(deltaX * deltaX + deltaY * deltaY);

            if (distance > 0) {
                float forceTowardsPlayer = 5.0f;
                float moveX = (deltaX / distance) * forceTowardsPlayer * entity.getSpeed();
                float moveY = (deltaY / distance) * forceTowardsPlayer * entity.getSpeed();

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
