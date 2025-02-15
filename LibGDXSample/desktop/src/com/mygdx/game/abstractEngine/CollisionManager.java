package com.mygdx.game.abstractEngine;

import com.badlogic.gdx.physics.box2d.*;
import com.mygdx.game.entity.Enemy;
import com.mygdx.game.entity.Player;

public class CollisionManager implements ContactListener {

    @Override
    public void beginContact(Contact contact) {
        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();

        if (isPlayerAndEnemyCollision(fixtureA, fixtureB)) {
            handlePlayerEnemyCollision(fixtureA, fixtureB);
        }
    }

    @Override
    public void endContact(Contact contact) {
        // Handle end of contact if needed
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {
        // Handle pre-solve if needed
    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {
        // Handle post-solve if needed
    }

    private boolean isPlayerAndEnemyCollision(Fixture fixtureA, Fixture fixtureB) {
        return (fixtureA.getUserData() instanceof Player && fixtureB.getUserData() instanceof Enemy) ||
                (fixtureA.getUserData() instanceof Enemy && fixtureB.getUserData() instanceof Player);
    }

    private void handlePlayerEnemyCollision(Fixture fixtureA, Fixture fixtureB) {
        Player player = (Player) (fixtureA.getUserData() instanceof Player ? fixtureA.getUserData() : fixtureB.getUserData());
        Enemy enemy = (Enemy) (fixtureA.getUserData() instanceof Enemy ? fixtureA.getUserData() : fixtureB.getUserData());

        // Handle collision logic, e.g., reduce player health
        player.setHealth(player.getHealth() - enemy.getDamage());
    }
}