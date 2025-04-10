package io.github.testgame.lwjgl3.collision;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.physics.box2d.Body;
import io.github.testgame.lwjgl3.entity.moveableObject.Ammo;
import io.github.testgame.lwjgl3.entity.moveableObject.Enemy;
import io.github.testgame.lwjgl3.entity.moveableObject.Player;
import io.github.testgame.lwjgl3.abstractEngine.*;
import io.github.testgame.lwjgl3.scene.sceneHelper.SceneFactory;
import io.github.testgame.lwjgl3.scene.sceneHelper.SceneType;
import io.github.testgame.lwjgl3.scene.sceneHelper.Transition;

public class AmmoCollisionHandler {
    private SceneManager sceneManager;
    private AudioManager audioManager;
    private Transition sceneTransition;
    private SceneFactory sceneFactory;

    public AmmoCollisionHandler(SceneManager sceneManager, AudioManager audioManager,
                                Transition sceneTransition) {
        this.sceneManager = sceneManager;
        this.audioManager = audioManager;
        this.sceneTransition = sceneTransition;
    }

    // Teleport bubble and enemy out of the scene, Player winning condition
    public void handleAmmoEnemyCollision(Body bodyA, Body bodyB) {
        Ammo ammo;
        Enemy enemy = null;

        if (bodyA.getUserData() instanceof Ammo && bodyB.getUserData() instanceof Enemy) {
            ammo = (Ammo) bodyA.getUserData();
            enemy = (Enemy) bodyB.getUserData();
        } else if (bodyB.getUserData() instanceof Ammo && bodyA.getUserData() instanceof Enemy) {
            ammo = (Ammo) bodyB.getUserData();
            enemy = (Enemy) bodyA.getUserData();
        } else {
            ammo = null;
        }

        if (ammo == null || enemy == null) return;

        Player player = ammo.getPlayer();
        if (player != null) {
            player.setScore(player.getScore() + 1);
//            win condition
            Preferences prefs = Gdx.app.getPreferences("GamePreferences");
            String difficulty = prefs.getString("difficulty", "easy");

            int requiredScore;
            switch (difficulty.toLowerCase()) {
                case "hard":
                    requiredScore = 20;
                    break;
                case "medium":
                    requiredScore = 15;
                    break;
                default:
                    requiredScore = 10;
                    break;
            }

            if (player.getScore() >= requiredScore) {
                sceneTransition.startTransition(SceneType.VICTORY); // Use transition instead
            }
        }
        final float offScreenX = -10000;
        final float offScreenY = -10000;

        ammo.getBody().setTransform(offScreenX, offScreenY, 0);
        enemy.getBody().setTransform(offScreenX, offScreenY, 0);
        audioManager.playSoundEffect("enemy");
    }
    // Destroy ammo when it hits static object
    public void handleAmmoStaticObjectCollision(Body bodyA, Body bodyB) {
        Ammo ammo = null;

        if (bodyA.getUserData() instanceof Ammo) {
            ammo = (Ammo) bodyA.getUserData();
        } else if (bodyB.getUserData() instanceof Ammo) {
            ammo = (Ammo) bodyB.getUserData();
        }

        if (ammo == null) return;

        final float offScreenX = -10000;
        final float offScreenY = -10000;

        ammo.getBody().setTransform(offScreenX, offScreenY, 0);
    }
}
