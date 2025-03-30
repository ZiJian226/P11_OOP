package io.github.testgame.lwjgl3.engineHelper.entityFactory;

import io.github.testgame.lwjgl3.engineHelper.entityFactory.factory.*;
import io.github.testgame.lwjgl3.entity.*;

public class EntityFactoryProducer {
    public static EntityFactory getFactory(EntityType entityType) {
        switch (entityType) {
            case ENEMY:
                return new EnemyFactory();
            case POWERUP:
                return new PowerUpFactory();
            case MAGAZINE:
                return new MagazineFactory();
            case NEUTRAL_OBJECT:
                return new NeutralObjectFactory();
            case AGGRESSIVE_OBJECT:
                return new AggressiveObjectFactory();
            case MODIFIER:
                return new ModifierFactory();
            default:
                throw new IllegalArgumentException("Unknown entity type: " + entityType);
        }
    }
}
