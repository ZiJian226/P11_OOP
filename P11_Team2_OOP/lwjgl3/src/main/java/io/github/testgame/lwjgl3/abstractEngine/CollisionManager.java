package io.github.testgame.lwjgl3.abstractEngine;

import com.badlogic.gdx.physics.box2d.*;
import io.github.testgame.lwjgl3.entity.*;
import io.github.testgame.lwjgl3.scene.SceneType;

import java.util.HashSet;
import java.util.Set;
import java.util.Queue;
import java.util.LinkedList;

public class CollisionManager implements ContactListener {
    private final Set<Contact> enemyContacts = new HashSet<>();
    private final Set<Contact> aggressiveContacts = new HashSet<>();
    // Queue to store post step actions
    // (This linked list is needed to store the actions that need to be executed after the physics step)
    // (If not, the physics world will be in an inconsistent state and crash)
    private final Queue<Runnable> postStepActions = new LinkedList<>();
    private float damageTimer = 0;
    private final EntityManager entityManager;

    public CollisionManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    // Depend on body's entity type, call the corresponding method to handle collision
    @Override
    public void beginContact(Contact contact) {
        Body bodyA = contact.getFixtureA().getBody();
        Body bodyB = contact.getFixtureB().getBody();

        if (isPlayerCollision(bodyA, bodyB, Enemy.class)) {
            enemyContacts.add(contact);
            handleDamage();
        } else if (isPlayerCollision(bodyA, bodyB, AggressiveObject.class)) {
            aggressiveContacts.add(contact);
            handleDamage();
        } else if (isBulletEnemyCollision(bodyA, bodyB)) {
            postStepActions.add(() -> handleBulletEnemyCollision(bodyA, bodyB));
        } else if (isBulletStaticObjectCollision(bodyA, bodyB)) {
            postStepActions.add(() -> handleBulletStaticObjectCollision(bodyA, bodyB));
        }
    }

    // Remove contact when it ends
    @Override
    public void endContact(Contact contact) {
        enemyContacts.remove(contact);
        aggressiveContacts.remove(contact);
    }

    // Check if the collision is between player and enemy / aggressive object
    private boolean isPlayerCollision(Body bodyA, Body bodyB, Class<?> targetClass) {
        Object userDataA = bodyA.getUserData();
        Object userDataB = bodyB.getUserData();
        return (userDataA instanceof Player && targetClass.isInstance(userDataB)) ||
            (userDataB instanceof Player && targetClass.isInstance(userDataA));
    }

    // Check if the collision is between bullet and enemy
    private boolean isBulletEnemyCollision(Body bodyA, Body bodyB) {
        Object userDataA = bodyA.getUserData();
        Object userDataB = bodyB.getUserData();
        return (userDataA instanceof Bullet && userDataB instanceof Enemy) ||
            (userDataB instanceof Bullet && userDataA instanceof Enemy);
    }

    // Check if the collision is between bullet and static object
    private boolean isBulletStaticObjectCollision(Body bodyA, Body bodyB) {
        Object userDataA = bodyA.getUserData();
        Object userDataB = bodyB.getUserData();
        return (userDataA instanceof Bullet && userDataB instanceof StaticObject) ||
            (userDataB instanceof Bullet && userDataA instanceof StaticObject);
    }

    // Teleport bullet and enemy out of the scene, Player winning condition
    private void handleBulletEnemyCollision(Body bodyA, Body bodyB) {
        Bullet bullet = null;
        Enemy enemy = null;

        // Arrange the bullet and enemy
        if (bodyA.getUserData() instanceof Bullet && bodyB.getUserData() instanceof Enemy) {
            bullet = (Bullet) bodyA.getUserData();
            enemy = (Enemy) bodyB.getUserData();
        } else if (bodyB.getUserData() instanceof Bullet && bodyA.getUserData() instanceof Enemy) {
            bullet = (Bullet) bodyB.getUserData();
            enemy = (Enemy) bodyA.getUserData();
        }

        if (bullet == null || enemy == null) return;

        // Winning condition
        Player player = bullet.getPlayer();
        if (player != null) {
            player.setScore(player.getScore() + 1);
            if (player.getScore() >= 10){
                SceneManager.getInstance().changeScene(SceneType.VICTORY);
                player.setHealth(10);
                player.setScore(0);
            }
        }

        // Teleport bullet and enemy out of the scene
        final float offScreenX = -10000;
        final float offScreenY = -10000;

        bullet.getBody().setTransform(offScreenX, offScreenY, 0);
        enemy.getBody().setTransform(offScreenX, offScreenY, 0);

    }

    // Destroy bullet when it hits static object
    private void handleBulletStaticObjectCollision(Body bodyA, Body bodyB) {
        Bullet bullet = null;

        if (bodyA.getUserData() instanceof Bullet) {
            bullet = (Bullet) bodyA.getUserData();
        } else if (bodyB.getUserData() instanceof Bullet) {
            bullet = (Bullet) bodyB.getUserData();
        }

        if (bullet == null) return;

        // Teleport bullet out of the scene
        final float offScreenX = -10000;
        final float offScreenY = -10000;

        bullet.getBody().setTransform(offScreenX, offScreenY, 0);
    }

    // Update the collision manager every frame
    public void update(float deltaTime) {
        handleContinuousDamage(deltaTime);
        processPostStepActions();
    }

    // Execute all post step actions in the queue
    private void processPostStepActions() {
        while (!postStepActions.isEmpty()) {
            postStepActions.poll().run();
        }
    }

    // Ensure continuous damage every second for player towards enemy and aggressive object
    private void handleContinuousDamage(float deltaTime) {
        if (!enemyContacts.isEmpty() || !aggressiveContacts.isEmpty()) {
            damageTimer += deltaTime;
            if (damageTimer >= 1.0f) {
                handleDamage();
                damageTimer = 0;
            }
        }
    }

    // Handle damage for player when it collides with enemy or aggressive object (Together with lose condition)
    private void handleDamage() {
        Player player = null;
        int damage = 0;

        if (!enemyContacts.isEmpty()) {
            player = getPlayerFromContact(enemyContacts.iterator().next());
            damage += enemyContacts.size();
        }

        if (!aggressiveContacts.isEmpty()) {
            if (player == null) {
                player = getPlayerFromContact(aggressiveContacts.iterator().next());
            }
            damage += aggressiveContacts.size();
        }

        // Lose condition
        if (player != null && damage > 0) {
            player.setHealth(player.getHealth() - damage);
            if (player.getHealth() <= 0) {
                SceneManager.getInstance().changeScene(SceneType.FAIL);
                player.setHealth(10);
                player.setScore(0);
            }
        }
    }

    // Retrieve player body info from contact
    private Player getPlayerFromContact(Contact contact) {
        Object userDataA = contact.getFixtureA().getBody().getUserData();
        Object userDataB = contact.getFixtureB().getBody().getUserData();
        if (userDataA instanceof Player) {
            return (Player) userDataA;
        }
        if (userDataB instanceof Player) {
            return (Player) userDataB;
        }
        return null;
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {}

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {}
}
