package io.github.testgame.lwjgl3.collision;

import com.badlogic.gdx.physics.box2d.Body;
import io.github.testgame.lwjgl3.entity.Bullet;
import io.github.testgame.lwjgl3.entity.Enemy;
import io.github.testgame.lwjgl3.entity.Player;
import io.github.testgame.lwjgl3.abstractEngine.*;
import io.github.testgame.lwjgl3.scene.*;

public class BulletCollisionHandler {
    private SceneManager sceneManager;

    public BulletCollisionHandler(SceneManager sceneManager) {
        this.sceneManager = sceneManager;
    }

    // Teleport bullet and enemy out of the scene, Player winning condition
    public void handleBulletEnemyCollision(Body bodyA, Body bodyB) {
        Bullet bullet = null;
        Enemy enemy = null;

        if (bodyA.getUserData() instanceof Bullet && bodyB.getUserData() instanceof Enemy) {
            bullet = (Bullet) bodyA.getUserData();
            enemy = (Enemy) bodyB.getUserData();
        } else if (bodyB.getUserData() instanceof Bullet && bodyA.getUserData() instanceof Enemy) {
            bullet = (Bullet) bodyB.getUserData();
            enemy = (Enemy) bodyA.getUserData();
        }

        if (bullet == null || enemy == null) return;

        Player player = bullet.getPlayer();
        if (player != null) {
            player.setScore(player.getScore() + 1);
            if (player.getScore() >= 10) {
                sceneManager.changeScene(SceneType.VICTORY);
                player.setHealth(10);
                player.setScore(0);
            }
        }

        final float offScreenX = -10000;
        final float offScreenY = -10000;

        bullet.getBody().setTransform(offScreenX, offScreenY, 0);
        enemy.getBody().setTransform(offScreenX, offScreenY, 0);
    }
    // Destroy bullet when it hits static object
    public void handleBulletStaticObjectCollision(Body bodyA, Body bodyB) {
        Bullet bullet = null;

        if (bodyA.getUserData() instanceof Bullet) {
            bullet = (Bullet) bodyA.getUserData();
        } else if (bodyB.getUserData() instanceof Bullet) {
            bullet = (Bullet) bodyB.getUserData();
        }

        if (bullet == null) return;

        final float offScreenX = -10000;
        final float offScreenY = -10000;

        bullet.getBody().setTransform(offScreenX, offScreenY, 0);
    }
}
