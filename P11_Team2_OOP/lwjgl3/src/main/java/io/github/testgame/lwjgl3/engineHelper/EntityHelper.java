package io.github.testgame.lwjgl3.engineHelper;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Timer;
import io.github.testgame.lwjgl3.abstractEngine.EntityManager;
import io.github.testgame.lwjgl3.entity.*;

import java.util.ArrayList;
import java.util.List;

public class EntityHelper extends EntityManager {
    private final EntityManager entityManager;
    private static final List<EntityManager> allEntityManagers = new ArrayList<>();

    public EntityHelper(EntityManager entityManager) {
        this.entityManager = entityManager;
        allEntityManagers.add(entityManager);
    }

    @Override
    public void update(World world, Player player) {
        super.update(world, player);
        respawnEntities(world, player);

        // Timeout logic for power-ups
        for (int i = 0; i < size(); i++) {
            Entity entity = get(i);
            if (entity instanceof PowerUp) {
                PowerUp powerUp = (PowerUp) entity;
                powerUp.reduceLifespan(Gdx.graphics.getDeltaTime());
                if (powerUp.getLifespan() <= 0) {
                    world.destroyBody(powerUp.getBody());
                    remove(powerUp);
                    System.out.println("1 Power-up removed after timeout (5 seconds).");
                }
            }
        }
    }

    // This method is used for NeutralObject and AggressiveObject from GameMaster, it is just simple for loop based on count
    public void initializeEntities(World world, String textureFile, int count, Player player, EntityType entityType) {
        for (int i = 0; i < count; i++) {
            spawnEntity(world, textureFile, generateValidPosition(player), entityType, player);
        }
    }
    // This method is used to generate valid position for entities to spawn
    private Vector2 generateValidPosition(Player player) {
        float x, y;
        do {
            x = player.getBody().getPosition().x * 32 + MathUtils.random(-1.5f, 1.5f) * Gdx.graphics.getWidth();
            y = player.getBody().getPosition().y * 32 + MathUtils.random(-1.5f, 1.5f) * Gdx.graphics.getHeight();
        } while (!isPositionValid(x, y));
        return new Vector2(x, y);
    }
    // This method checks position validity ensure no overlapping of specified type of entities
    public boolean isPositionValid(float x, float y) {
        for (EntityManager manager : allEntityManagers) {
            for (int i = 0; i < manager.size(); i++) {
                Entity entity = manager.get(i);
                if (entity instanceof TextureObject) {
                    TextureObject textureObject = (TextureObject) entity;
                    if (Math.abs(textureObject.getX() - x) < textureObject.getWidth() &&
                        Math.abs(textureObject.getY() - y) < textureObject.getHeight()) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    // This method is used to generate respawn position for entities (just position, not entity)
    public Vector2 generateRespawnPosition(Player player) {
        int side = MathUtils.random(3);
        float playerX = player.getBody().getPosition().x * 32;
        float playerY = player.getBody().getPosition().y * 32;
        float x, y;

        switch (side) {
            case 0: // top
                x = playerX + MathUtils.random(-1.5f, 1.5f) * Gdx.graphics.getWidth();
                y = playerY + Gdx.graphics.getHeight();
                break;
            case 1: // right
                x = playerX + Gdx.graphics.getWidth();
                y = playerY + MathUtils.random(-1.5f, 1.5f) * Gdx.graphics.getHeight();
                break;
            case 2: // bottom
                x = playerX + MathUtils.random(-1.5f, 1.5f) * Gdx.graphics.getWidth();
                y = playerY - Gdx.graphics.getHeight();
                break;
            default: // left
                x = playerX - Gdx.graphics.getWidth();
                y = playerY + MathUtils.random(-1.5f, 1.5f) * Gdx.graphics.getHeight();
        }
        return new Vector2(x, y);
    }
    // This method will create entity based on specified type (Enemy, NeutralObject, AggressiveObject, Magazine)
    public void spawnEntity(World world, String textureFile, Vector2 position, EntityType entityType, Player player) {
        Entity entity;
        switch (entityType) {
            case ENEMY:
                entity = new Enemy(world, textureFile, position.x, position.y, 5);
                ((Enemy) entity).setPlayer(player);
                break;
            case NEUTRAL_OBJECT:
                entity = new NeutralObject(world, textureFile, position.x, position.y);
                break;
            case AGGRESSIVE_OBJECT:
                entity = new AggressiveObject(world, textureFile, position.x, position.y);
                break;
            case MAGAZINE:
                entity = new Magazine(world, textureFile, position.x, position.y, MathUtils.random(1, 10));
                break;
            case MODIFIER:
                entity = new Modifier(world, textureFile, position.x, position.y, MathUtils.random(0.1f, 2));
                break;
            case POWERUP:
                entity = new PowerUp(world, textureFile, position.x, position.y, 5);
                break;
            default:
                return;
        }
        entityManager.add(entity);
    }
    // This method will schedule enemy spawning based on count
    public void scheduleEnemySpawning(World world, float count, Entity player) {
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                if (entityManager.size() < count) {
                    Vector2 pos;
                    do {
                        pos = new Vector2(
                            MathUtils.random(0.9f) * Gdx.graphics.getWidth(),
                            MathUtils.random(0.9f) * Gdx.graphics.getHeight()
                        );
                    } while (Vector2.dst(pos.x, pos.y, player.getX(), player.getY()) < 200);
                    spawnEntity(world, "texture/virus.png", pos, EntityType.ENEMY, (Player) player);
                }
            }
        }, 0, 1/2f);
    }
    public void schedulePowerUpSpawning(World world, Player player) {
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                if (entityManager.size() < 7) { // Limit to 7 power-ups
                    Vector2 position = generateValidPosition(player);
                    spawnEntity(world, "texture/powerup.png", position, EntityType.POWERUP, player);
                }
            }
        }, 0, 3); // Try spawning every 5 seconds
    }

    // This method will respawn entities if they are out of screen
    private void respawnEntities(World world, Player player) {
        for (int i = 0; i < entityManager.size(); i++) {
            Entity entity = entityManager.get(i);
            if ((entity instanceof Enemy || entity instanceof NeutralObject || entity instanceof AggressiveObject)
                && isOutOfScreen(entity, player)) {
                world.destroyBody(entity.getBody());
                entityManager.remove(i);
                Vector2 pos;
                do {
                    pos = generateRespawnPosition(player);
                } while (!isPositionValid(pos.x, pos.y));

                EntityType entityType = entity instanceof Enemy ? EntityType.ENEMY :
                    entity instanceof AggressiveObject ? EntityType.AGGRESSIVE_OBJECT :
                    entity instanceof Magazine ? EntityType.MAGAZINE :
                    entity instanceof Modifier ? EntityType.MODIFIER :
                    entity instanceof PowerUp ? EntityType.POWERUP : EntityType.NEUTRAL_OBJECT;

                spawnEntity(world, entity instanceof Enemy ? "texture/virus.png" :
                        entity instanceof AggressiveObject ? "texture/mud.png" :
                        entity instanceof Magazine ? "texture/magazine.png" :
                        entity instanceof Modifier ? "texture/modifier.png" :
                        entity instanceof PowerUp ? "texture/powerup.png" : "texture/soap.png",
                    pos, entityType, player);
            }
        }
    }
    // This method will check if entity is out of screen
    // (based on player position, render distance is about 3x3 screen)
    // refer to generateRespawnPosition method where range is -1.5 to 1.5 in mathutils.random
    public static boolean isOutOfScreen(Entity entity, Player player) {
        return entity.getBody().getPosition().x * 32 < player.getBody().getPosition().x * 32 - Gdx.graphics.getWidth() ||
            entity.getBody().getPosition().x * 32 > player.getBody().getPosition().x * 32 + Gdx.graphics.getWidth() ||
            entity.getBody().getPosition().y * 32 < player.getBody().getPosition().y * 32 - Gdx.graphics.getHeight() ||
            entity.getBody().getPosition().y * 32 > player.getBody().getPosition().y * 32 + Gdx.graphics.getHeight();
    }
}
