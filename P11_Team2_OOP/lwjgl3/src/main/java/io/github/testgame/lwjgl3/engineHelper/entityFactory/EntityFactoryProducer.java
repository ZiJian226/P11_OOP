package io.github.testgame.lwjgl3.engineHelper.entityFactory;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.World;
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

    private static class EnemyFactory implements EntityFactory {
        @Override
        public Entity createEntity(World world, String textureFile, float x, float y, Player player) {
            Enemy enemy = new Enemy(world, textureFile, x, y, 5);
            enemy.setPlayer(player);
            return enemy;
        }
    }

    private static class PowerUpFactory implements EntityFactory {
        @Override
        public Entity createEntity(World world, String textureFile, float x, float y, Player player) {
            return new PowerUp(world, textureFile, x, y, 5);
        }
    }

    private static class MagazineFactory implements EntityFactory {
        @Override
        public Entity createEntity(World world, String textureFile, float x, float y, Player player) {
            return new Magazine(world, textureFile, x, y, MathUtils.random(1, 10));
        }
    }

    private static class NeutralObjectFactory implements EntityFactory {
        @Override
        public Entity createEntity(World world, String textureFile, float x, float y, Player player) {
            return new NeutralObject(world, textureFile, x, y);
        }
    }

    private static class AggressiveObjectFactory implements EntityFactory {
        @Override
        public Entity createEntity(World world, String textureFile, float x, float y, Player player) {
            return new AggressiveObject(world, textureFile, x, y);
        }
    }

    private static class ModifierFactory implements EntityFactory {
        @Override
        public Entity createEntity(World world, String textureFile, float x, float y, Player player) {
            return new Modifier(world, textureFile, x, y, MathUtils.random(0.1f, 2));
        }
    }
}
