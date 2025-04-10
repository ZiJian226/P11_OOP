package io.github.testgame.lwjgl3.abstractEngine;

import com.badlogic.gdx.physics.box2d.*;
import io.github.testgame.lwjgl3.collision.*;

public class CollisionManager implements ContactListener {
    protected final PostStepActionProcessor postStepActionProcessor = new PostStepActionProcessor();

    @Override
    public void beginContact(Contact contact) {
        Body bodyA = contact.getFixtureA().getBody();
        Body bodyB = contact.getFixtureB().getBody();
        System.out.println("Collision begin between " + bodyA.getUserData() + " and " + bodyB.getUserData());
    }

    @Override
    public void endContact(Contact contact) {
        Body bodyA = contact.getFixtureA().getBody();
        Body bodyB = contact.getFixtureB().getBody();
        System.out.println("Collision end between " + bodyA.getUserData() + " and " + bodyB.getUserData());
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {}

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {}
}
