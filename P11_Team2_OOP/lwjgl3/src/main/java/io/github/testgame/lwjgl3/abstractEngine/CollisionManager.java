package io.github.testgame.lwjgl3.abstractEngine;

import com.badlogic.gdx.physics.box2d.*;
import io.github.testgame.lwjgl3.entity.*;
import io.github.testgame.lwjgl3.collision.*;

import java.util.HashSet;
import java.util.Set;

public class CollisionManager implements ContactListener {
    private final Set<Contact> enemyContacts = new HashSet<>();
    private final Set<Contact> aggressiveContacts = new HashSet<>();
    private final PostStepActionProcessor postStepActionProcessor = new PostStepActionProcessor();
    private final DamageHandler damageHandler = new DamageHandler();
    private final BulletCollisionHandler bulletCollisionHandler = new BulletCollisionHandler();

    // Depend on body's entity type, call the corresponding method to handle collision
    @Override
    public void beginContact(Contact contact) {
        Body bodyA = contact.getFixtureA().getBody();
        Body bodyB = contact.getFixtureB().getBody();

        if (isPlayerCollision(bodyA, bodyB, Enemy.class)) {
            enemyContacts.add(contact);
            damageHandler.beginContact(contact);
        } else if (isPlayerCollision(bodyA, bodyB, AggressiveObject.class)) {
            aggressiveContacts.add(contact);
            damageHandler.beginContact(contact);
        } else if (isBulletEnemyCollision(bodyA, bodyB)) {
            postStepActionProcessor.addPostStepAction(() -> bulletCollisionHandler.handleBulletEnemyCollision(bodyA, bodyB));
        } else if (isBulletStaticObjectCollision(bodyA, bodyB)) {
            postStepActionProcessor.addPostStepAction(() -> bulletCollisionHandler.handleBulletStaticObjectCollision(bodyA, bodyB));
        }
    }

    // Remove contact when it ends
    @Override
    public void endContact(Contact contact) {
        enemyContacts.remove(contact);
        aggressiveContacts.remove(contact);
        damageHandler.endContact(contact);
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

    // Update the collision manager every frame
    public void update(float deltaTime) {
        damageHandler.handleContinuousDamage(deltaTime);
        postStepActionProcessor.processPostStepActions();
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {}

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {}
}
