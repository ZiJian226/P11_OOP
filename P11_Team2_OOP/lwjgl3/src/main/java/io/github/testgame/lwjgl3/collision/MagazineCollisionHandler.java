package io.github.testgame.lwjgl3.collision;

import com.badlogic.gdx.physics.box2d.Body;
import io.github.testgame.lwjgl3.abstractEngine.AudioManager;
import io.github.testgame.lwjgl3.abstractEngine.SceneManager;
import io.github.testgame.lwjgl3.entity.Magazine;
import io.github.testgame.lwjgl3.entity.Player;

public class MagazineCollisionHandler {
    private SceneManager sceneManager;
    private AudioManager audioManager;

    public MagazineCollisionHandler(SceneManager sceneManager, AudioManager audioManager){
        this.sceneManager = sceneManager;
        this.audioManager = audioManager;
    }

    public void handleMagazinePlayerCollision(Body bodyA, Body bodyB) {
        Player player = null;
        Magazine magazine = null;

        // Determine which body is the player and which is the magazine
        if (bodyA.getUserData() instanceof Player && bodyB.getUserData() instanceof Magazine) {
            player = (Player) bodyA.getUserData();
            magazine = (Magazine) bodyB.getUserData();
        } else if (bodyB.getUserData() instanceof Player && bodyA.getUserData() instanceof Magazine) {
            player = (Player) bodyB.getUserData();
            magazine = (Magazine) bodyA.getUserData();
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
        }
    }
}
