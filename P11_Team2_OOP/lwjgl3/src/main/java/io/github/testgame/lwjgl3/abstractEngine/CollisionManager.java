package io.github.testgame.lwjgl3.abstractEngine;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.*;
import io.github.testgame.lwjgl3.entity.*;
import io.github.testgame.lwjgl3.collision.*;

import java.util.HashSet;
import java.util.Set;

public class CollisionManager implements ContactListener {
    private final Set<Contact> enemyContacts = new HashSet<>();
    private final Set<Contact> aggressiveContacts = new HashSet<>();
    private final EnemyDamageHandler enemyDamageHandler = new EnemyDamageHandler();
    private final AggressiveObjectDamageHandler aggressiveObjectDamageHandler = new AggressiveObjectDamageHandler();
    private final BulletCollisionHandler bulletCollisionHandler = new BulletCollisionHandler();
    private final PostStepActionProcessor postStepActionProcessor = new PostStepActionProcessor();

    @Override
    public void beginContact(Contact contact) {
        Body bodyA = contact.getFixtureA().getBody();
        Body bodyB = contact.getFixtureB().getBody();

        System.out.println("Collision between " + bodyA.getUserData() + " and " + bodyB.getUserData());

        if (isPlayerCollision(bodyA, bodyB, Enemy.class)) {
            enemyContacts.add(contact);
            Player player = (Player) (bodyA.getUserData() instanceof Player ? bodyA.getUserData() : bodyB.getUserData());
            Object collider = bodyA.getUserData() instanceof Player ? bodyB.getUserData() : bodyA.getUserData();
            enemyDamageHandler.beginContact(player, collider);
        }
        if (isPlayerCollision(bodyA, bodyB, AggressiveObject.class)) {
            aggressiveContacts.add(contact);
            Player player = (Player) (bodyA.getUserData() instanceof Player ? bodyA.getUserData() : bodyB.getUserData());
            Object collider = bodyA.getUserData() instanceof Player ? bodyB.getUserData() : bodyA.getUserData();
            aggressiveObjectDamageHandler.beginContact(player, collider);
        }
        if (isBulletEnemyCollision(bodyA, bodyB)) {
            postStepActionProcessor.addPostStepAction(() -> bulletCollisionHandler.handleBulletEnemyCollision(bodyA, bodyB));
        }
        if (isBulletStaticObjectCollision(bodyA, bodyB)) {
            postStepActionProcessor.addPostStepAction(() -> bulletCollisionHandler.handleBulletStaticObjectCollision(bodyA, bodyB));
        }
    }

    @Override
    public void endContact(Contact contact) {
        Body bodyA = contact.getFixtureA().getBody();
        Body bodyB = contact.getFixtureB().getBody();
        Object collider = bodyA.getUserData() instanceof Player ? bodyB.getUserData() : bodyA.getUserData();

        if (enemyContacts.remove(contact)) {
            enemyDamageHandler.endContact(collider);
        }
        if (aggressiveContacts.remove(contact)) {
            aggressiveObjectDamageHandler.endContact(collider);
        }
    }

    private boolean isPlayerCollision(Body bodyA, Body bodyB, Class<?> targetClass) {
        Object userDataA = bodyA.getUserData();
        Object userDataB = bodyB.getUserData();
        return (userDataA instanceof Player && targetClass.isInstance(userDataB)) ||
            (userDataB instanceof Player && targetClass.isInstance(userDataA));
    }

    private boolean isBulletEnemyCollision(Body bodyA, Body bodyB) {
        Object userDataA = bodyA.getUserData();
        Object userDataB = bodyB.getUserData();
        return (userDataA instanceof Bullet && userDataB instanceof Enemy) ||
            (userDataB instanceof Bullet && userDataA instanceof Enemy);
    }

    private boolean isBulletStaticObjectCollision(Body bodyA, Body bodyB) {
        Object userDataA = bodyA.getUserData();
        Object userDataB = bodyB.getUserData();
        return (userDataA instanceof Bullet && userDataB instanceof StaticObject) ||
            (userDataB instanceof Bullet && userDataA instanceof StaticObject);
    }

    public void update(Player player) {
        float deltaTime = Gdx.graphics.getDeltaTime();

        // Process damage for active contacts
        if (!enemyContacts.isEmpty()) {
            enemyDamageHandler.applyDamageToPlayer(player, deltaTime);
        }
        if (!aggressiveContacts.isEmpty()) {
            aggressiveObjectDamageHandler.applyDamageToPlayer(player, deltaTime);
        }

        postStepActionProcessor.processPostStepActions();
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {}

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {}
}
