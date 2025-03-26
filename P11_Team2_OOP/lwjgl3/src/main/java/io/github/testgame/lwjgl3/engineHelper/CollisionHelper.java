package io.github.testgame.lwjgl3.engineHelper;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.World;
import io.github.testgame.lwjgl3.abstractEngine.AudioManager;
import io.github.testgame.lwjgl3.abstractEngine.CollisionManager;
import io.github.testgame.lwjgl3.abstractEngine.EntityManager;
import io.github.testgame.lwjgl3.abstractEngine.SceneManager;
import io.github.testgame.lwjgl3.collision.*;
import io.github.testgame.lwjgl3.entity.moveableObject.Ammo;
import io.github.testgame.lwjgl3.entity.moveableObject.Enemy;
import io.github.testgame.lwjgl3.entity.moveableObject.Player;
import io.github.testgame.lwjgl3.entity.staticObject.AggressiveObject;
import io.github.testgame.lwjgl3.entity.staticObject.Magazine;
import io.github.testgame.lwjgl3.entity.staticObject.PowerUp;
import io.github.testgame.lwjgl3.entity.staticObject.StaticObject;
import io.github.testgame.lwjgl3.scene.Transition;

import java.util.HashSet;
import java.util.Set;

public class CollisionHelper extends CollisionManager {
    private final Set<Contact> enemyContacts = new HashSet<>();
    private final Set<Contact> aggressiveContacts = new HashSet<>();
    private final EnemyDamageHandler enemyDamageHandler;
    private final AggressiveObjectDamageHandler aggressiveObjectDamageHandler;
    private final AmmoCollisionHandler ammoCollisionHandler;
    private final StaticObjectCollisionHandler staticObjectCollisionHandler;
    private final World world;
    private final EntityManager entityManager;
    private final AudioManager audioManager;
    private final DamageFlashEffect damageFlashEffect;

    public CollisionHelper(SceneManager sceneManager, AudioManager audioManager,
                           World world, EntityManager entityManager, Transition sceneTransition) {
        super();
        this.world = world;
        this.entityManager = entityManager;
        this.audioManager = audioManager;
        this.damageFlashEffect = new DamageFlashEffect();
        this.enemyDamageHandler = new EnemyDamageHandler(sceneManager, audioManager, damageFlashEffect, sceneTransition);
        this.aggressiveObjectDamageHandler = new AggressiveObjectDamageHandler(sceneManager, audioManager, damageFlashEffect, sceneTransition);
        this.ammoCollisionHandler = new AmmoCollisionHandler(sceneManager, audioManager, sceneTransition);
        this.staticObjectCollisionHandler = new StaticObjectCollisionHandler(sceneManager, audioManager);
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
        if (isPlayerCollision(bodyA, bodyB, StaticObject.class)) {
            postStepActionProcessor.addPostStepAction(() -> staticObjectCollisionHandler.handleStaticObjectPlayerCollision(bodyA, bodyB));
        }
        if (isAmmoEnemyCollision(bodyA, bodyB)) {
            postStepActionProcessor.addPostStepAction(() -> ammoCollisionHandler.handleAmmoEnemyCollision(bodyA, bodyB));
        }
        if (isAmmoStaticObjectCollision(bodyA, bodyB)) {
            postStepActionProcessor.addPostStepAction(() -> ammoCollisionHandler.handleAmmoStaticObjectCollision(bodyA, bodyB));
        }
        if (isPlayerCollision(bodyA, bodyB, PowerUp.class)) {
            Player player = (Player) (bodyA.getUserData() instanceof Player ? bodyA.getUserData() : bodyB.getUserData());
            PowerUp powerUp = (PowerUp) (bodyA.getUserData() instanceof PowerUp ? bodyA.getUserData() : bodyB.getUserData());

            postStepActionProcessor.addPostStepAction(() -> {
                audioManager.playSoundEffect("reload");
                player.setHealth(player.getHealth() + 1); // Increase health
                world.destroyBody(powerUp.getBody());
                entityManager.remove(powerUp);
            });
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

    private boolean isAmmoEnemyCollision(Body bodyA, Body bodyB) {
        Object userDataA = bodyA.getUserData();
        Object userDataB = bodyB.getUserData();
        return (userDataA instanceof Ammo && userDataB instanceof Enemy) ||
            (userDataB instanceof Ammo && userDataA instanceof Enemy);
    }

    private boolean isAmmoStaticObjectCollision(Body bodyA, Body bodyB) {
        Object userDataA = bodyA.getUserData();
        Object userDataB = bodyB.getUserData();
        return (userDataA instanceof Ammo && userDataB instanceof StaticObject || userDataB instanceof Magazine) ||
            (userDataB instanceof Ammo && userDataA instanceof StaticObject || userDataA instanceof Magazine);
    }

    public DamageFlashEffect getDamageFlashEffect() {
        return damageFlashEffect;
    }
}
