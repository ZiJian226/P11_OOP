package io.github.testgame.lwjgl3.collision;

import com.badlogic.gdx.physics.box2d.Contact;
import io.github.testgame.lwjgl3.entity.*;
import io.github.testgame.lwjgl3.abstractEngine.*;
import io.github.testgame.lwjgl3.scene.*;

import java.util.HashSet;
import java.util.Set;

public class DamageHandler {
    private float damageTimer = 0;
    private final Set<Contact> playerEnemyContacts = new HashSet<>();
    private final Set<Contact> playerAggressiveContacts = new HashSet<>();

    // Ensure continuous damage every second for player towards enemy and aggressive object
    public void handleContinuousDamage(float deltaTime) {
        int totalContacts = playerEnemyContacts.size() + playerAggressiveContacts.size();
        if (totalContacts > 0) {
            damageTimer += deltaTime;
            float damageInterval = 1.0f / totalContacts;
            if (damageTimer >= damageInterval) {
                handleDamage();
                damageTimer = 0; // Reset the timer after applying damage
            }
        } else {
            damageTimer = 0; // Reset the timer only if there are no collisions
        }
    }

    // Handle damage for player when it collides with enemy or aggressive object
    private void handleDamage() {
        Player player = getPlayerFromContact(playerEnemyContacts.isEmpty() ? playerAggressiveContacts.iterator().next() : playerEnemyContacts.iterator().next());
        if (player != null) {
            int damage = playerEnemyContacts.size() + playerAggressiveContacts.size();
            System.out.println("Applying damage: " + damage);
            player.setHealth(player.getHealth() - damage);
            System.out.println("Player health: " + player.getHealth());
            if (player.getHealth() <= 0) {
                SceneManager.getInstance().changeScene(SceneType.FAIL);
                player.setHealth(10);
                player.setScore(0);
            }
        } else {
            System.out.println("No player found or no damage to apply.");
        }
    }

    // Retrieve player body info from contact
    private Player getPlayerFromContact(Contact contact) {
        Object userDataA = contact.getFixtureA().getBody().getUserData();
        Object userDataB = contact.getFixtureB().getBody().getUserData();
        System.out.println("Contact between: " + userDataA + " and " + userDataB);
        if (userDataA instanceof Player) {
            return (Player) userDataA;
        }
        if (userDataB instanceof Player) {
            return (Player) userDataB;
        }
        return null;
    }

    // Add contact to the set when collision begins
    public void beginContact(Contact contact) {
        if (isPlayerEnemyContact(contact)) {
            playerEnemyContacts.add(contact);
        } else if (isPlayerAggressiveContact(contact)) {
            playerAggressiveContacts.add(contact);
        }
    }

    // Remove contact from the set when collision ends
    public void endContact(Contact contact) {
        playerEnemyContacts.remove(contact);
        playerAggressiveContacts.remove(contact);
    }

    // Check if the contact is between player and enemy
    private boolean isPlayerEnemyContact(Contact contact) {
        Object userDataA = contact.getFixtureA().getBody().getUserData();
        Object userDataB = contact.getFixtureB().getBody().getUserData();
        return (userDataA instanceof Player && userDataB instanceof Enemy) ||
            (userDataB instanceof Player && userDataA instanceof Enemy);
    }

    // Check if the contact is between player and aggressive object
    private boolean isPlayerAggressiveContact(Contact contact) {
        Object userDataA = contact.getFixtureA().getBody().getUserData();
        Object userDataB = contact.getFixtureB().getBody().getUserData();
        return (userDataA instanceof Player && userDataB instanceof AggressiveObject) ||
            (userDataB instanceof Player && userDataA instanceof AggressiveObject);
    }
}
