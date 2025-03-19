package io.github.testgame.lwjgl3.collision;

import com.badlogic.gdx.physics.box2d.Body;
import io.github.testgame.lwjgl3.abstractEngine.AudioManager;
import io.github.testgame.lwjgl3.abstractEngine.SceneManager;
import io.github.testgame.lwjgl3.entity.Magazine;
import io.github.testgame.lwjgl3.entity.Modifier;
import io.github.testgame.lwjgl3.entity.Player;

public class StaticObjectCollisionHandler {
    private SceneManager sceneManager;
    private AudioManager audioManager;

    public StaticObjectCollisionHandler(SceneManager sceneManager, AudioManager audioManager){
        this.sceneManager = sceneManager;
        this.audioManager = audioManager;
    }

    public void handleStaticObjectPlayerCollision(Body bodyA, Body bodyB) {
        Player player = null;
        Magazine magazine = null;
        Modifier modifier = null;

        // Determine which body is the player and which is the magazine
        if (bodyA.getUserData() instanceof Player && bodyB.getUserData() instanceof Magazine) {
            player = (Player) bodyA.getUserData();
            magazine = (Magazine) bodyB.getUserData();
        } else if (bodyB.getUserData() instanceof Player && bodyA.getUserData() instanceof Magazine) {
            player = (Player) bodyB.getUserData();
            magazine = (Magazine) bodyA.getUserData();
        } else if (bodyA.getUserData() instanceof Player && bodyB.getUserData() instanceof Modifier) {
            player = (Player) bodyA.getUserData();
            modifier = (Modifier) bodyB.getUserData();
        } else if (bodyB.getUserData() instanceof Player && bodyA.getUserData() instanceof Modifier) {
            player = (Player) bodyB.getUserData();
            modifier = (Modifier) bodyA.getUserData();
        }

        // If we have both a player and a magazine, handle the collision
        if (player != null && magazine != null) {
            // Add magazine ammo to player's ammo
            player.setAmmoCount(player.getAmmoCount() + magazine.getAmmo());

            // Play reload sound effect
            audioManager.playSoundEffect("reload");

            // Move the magazine off-screen (same approach used in AmmoCollisionHandler)
            final float offScreenX = -10000;
            final float offScreenY = -10000;
            magazine.getBody().setTransform(offScreenX, offScreenY, 0);
        } else if (player != null && modifier != null) {
            // Apply modifier effect to player
            modifier.applyEffect(player);

            // Play modifier sound effect
            audioManager.playSoundEffect("modifier");

            // Move the modifier off-screen
            final float offScreenX = -10000;
            final float offScreenY = -10000;
            modifier.getBody().setTransform(offScreenX, offScreenY, 0);
        }
    }
}
