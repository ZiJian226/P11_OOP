package io.github.testgame.lwjgl3.engineHelper;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import io.github.testgame.lwjgl3.abstractEngine.CollisionManager;
import io.github.testgame.lwjgl3.abstractEngine.SceneManager;
import io.github.testgame.lwjgl3.entity.*;
import io.github.testgame.lwjgl3.collision.*;

import java.util.HashSet;
import java.util.Set;

public class CollisionHelper extends CollisionManager {
    private final Set<Contact> enemyContacts = new HashSet<>();
    private final Set<Contact> aggressiveContacts = new HashSet<>();
    private final EnemyDamageHandler enemyDamageHandler;
    private final AggressiveObjectDamageHandler aggressiveObjectDamageHandler;
    private final BubbleCollisionHandler bubbleCollisionHandler;

    public CollisionHelper(SceneManager sceneManager) {
        super();
        this.enemyDamageHandler = new EnemyDamageHandler(sceneManager);
        this.aggressiveObjectDamageHandler = new AggressiveObjectDamageHandler(sceneManager);
        this.bubbleCollisionHandler = new BubbleCollisionHandler(sceneManager);
    }

    @Override
    public void beginContact(Contact contact) {
        Body bodyA = contact.getFixtureA().getBody();
        Body bodyB = contact.getFixtureB().getBody();
        System.out.println("Collision begin between " + bodyA.getUserData() + " and " + bodyB.getUserData());

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
        if (isBubbleEnemyCollision(bodyA, bodyB)) {
            postStepActionProcessor.addPostStepAction(() -> bubbleCollisionHandler.handleBubbleEnemyCollision(bodyA, bodyB));
        }
        if (isBubbleStaticObjectCollision(bodyA, bodyB)) {
            postStepActionProcessor.addPostStepAction(() -> bubbleCollisionHandler.handleBubbleStaticObjectCollision(bodyA, bodyB));
        }
    }

    @Override
    public void endContact(Contact contact) {
        Body bodyA = contact.getFixtureA().getBody();
        Body bodyB = contact.getFixtureB().getBody();
        System.out.println("Collision end between " + bodyA.getUserData() + " and " + bodyB.getUserData());
        Object collider = bodyA.getUserData() instanceof Player ? bodyB.getUserData() : bodyA.getUserData();

        if (enemyContacts.remove(contact)) {
            enemyDamageHandler.endContact(collider);
        }
        if (aggressiveContacts.remove(contact)) {
            aggressiveObjectDamageHandler.endContact(collider);
        }
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

    private boolean isPlayerCollision(Body bodyA, Body bodyB, Class<?> targetClass) {
        Object userDataA = bodyA.getUserData();
        Object userDataB = bodyB.getUserData();
        return (userDataA instanceof Player && targetClass.isInstance(userDataB)) ||
            (userDataB instanceof Player && targetClass.isInstance(userDataA));
    }

    private boolean isBubbleEnemyCollision(Body bodyA, Body bodyB) {
        Object userDataA = bodyA.getUserData();
        Object userDataB = bodyB.getUserData();
        return (userDataA instanceof Bubble && userDataB instanceof Enemy) ||
            (userDataB instanceof Bubble && userDataA instanceof Enemy);
    }

    private boolean isBubbleStaticObjectCollision(Body bodyA, Body bodyB) {
        Object userDataA = bodyA.getUserData();
        Object userDataB = bodyB.getUserData();
        return (userDataA instanceof Bubble && userDataB instanceof StaticObject) ||
            (userDataB instanceof Bubble && userDataA instanceof StaticObject);
    }
}
