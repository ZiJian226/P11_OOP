package io.github.testgame.lwjgl3.collision;

import com.badlogic.gdx.physics.box2d.Body;
import io.github.testgame.lwjgl3.entity.Bubble;
import io.github.testgame.lwjgl3.entity.Enemy;
import io.github.testgame.lwjgl3.entity.Player;
import io.github.testgame.lwjgl3.abstractEngine.*;
import io.github.testgame.lwjgl3.scene.*;

public class BubbleCollisionHandler {
    private SceneManager sceneManager;
    private AudioManager audioManager;

    public BubbleCollisionHandler(SceneManager sceneManager, AudioManager audioManager) {
        this.sceneManager = sceneManager;
        this.audioManager = audioManager;
    }

    // Teleport bubble and enemy out of the scene, Player winning condition
    public void handleBubbleEnemyCollision(Body bodyA, Body bodyB) {
        Bubble bubble = null;
        Enemy enemy = null;

        if (bodyA.getUserData() instanceof Bubble && bodyB.getUserData() instanceof Enemy) {
            bubble = (Bubble) bodyA.getUserData();
            enemy = (Enemy) bodyB.getUserData();
        } else if (bodyB.getUserData() instanceof Bubble && bodyA.getUserData() instanceof Enemy) {
            bubble = (Bubble) bodyB.getUserData();
            enemy = (Enemy) bodyA.getUserData();
        }

        if (bubble == null || enemy == null) return;

        Player player = bubble.getPlayer();
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

        bubble.getBody().setTransform(offScreenX, offScreenY, 0);
        enemy.getBody().setTransform(offScreenX, offScreenY, 0);
        audioManager.playSoundEffect("enemy");
    }
    // Destroy bubble when it hits static object
    public void handleBubbleStaticObjectCollision(Body bodyA, Body bodyB) {
        Bubble bubble = null;

        if (bodyA.getUserData() instanceof Bubble) {
            bubble = (Bubble) bodyA.getUserData();
        } else if (bodyB.getUserData() instanceof Bubble) {
            bubble = (Bubble) bodyB.getUserData();
        }

        if (bubble == null) return;

        final float offScreenX = -10000;
        final float offScreenY = -10000;

        bubble.getBody().setTransform(offScreenX, offScreenY, 0);
    }
}
