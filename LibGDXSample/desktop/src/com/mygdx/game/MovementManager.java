package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

public class MovementManager {

    public void manualMovement(iMoveable entity) {
        boolean moved = false;
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT) || Gdx.input.isKeyPressed(Input.Keys.A)) {
            if (entity.getX() >= 0) {
                entity.setX(entity.getX() - entity.getSpeed() * Gdx.graphics.getDeltaTime());
                entity.setDirection(-1, 0);
                moved = true;
            }
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) || Gdx.input.isKeyPressed(Input.Keys.D)) {
            if (entity.getX() <= Gdx.graphics.getWidth() - entity.getWidth()) {
                entity.setX(entity.getX() + entity.getSpeed() * Gdx.graphics.getDeltaTime());
                entity.setDirection(1, 0);
                moved = true;
            }
        }
        if (Gdx.input.isKeyPressed(Input.Keys.UP) || Gdx.input.isKeyPressed(Input.Keys.W)) {
            if (entity.getY() <= Gdx.graphics.getHeight() - entity.getHeight()) {
                entity.setY(entity.getY() + entity.getSpeed() * Gdx.graphics.getDeltaTime());
                entity.setDirection(0, 1);
                moved = true;
            }
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN) || Gdx.input.isKeyPressed(Input.Keys.S)) {
            if (entity.getY() >= 0) {
                entity.setY(entity.getY() - entity.getSpeed() * Gdx.graphics.getDeltaTime());
                entity.setDirection(0, -1);
                moved = true;
            }
        }
    }

    public void autoMovement(iMoveable entity, iMoveable target) {
        float targetX = target.getX();
        float targetY = target.getY();
        float deltaX = targetX - entity.getX();
        float deltaY = targetY - entity.getY();
        float distance = (float) Math.sqrt(deltaX * deltaX + deltaY * deltaY);

        if (distance > 0) {
            float moveX = (deltaX / distance) * entity.getSpeed() * Gdx.graphics.getDeltaTime();
            float moveY = (deltaY / distance) * entity.getSpeed() * Gdx.graphics.getDeltaTime();
            entity.setX(entity.getX() + moveX);
            entity.setY(entity.getY() + moveY);
            entity.updateRotation(deltaX, deltaY);
        }
    }
}
