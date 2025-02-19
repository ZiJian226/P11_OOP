package io.github.testgame.lwjgl3.abstractEngine;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Timer;
import io.github.testgame.lwjgl3.entity.*;

import java.util.ArrayList;
import java.util.List;

public class EntityManager {
    private final List<Entity> entityList;

    public EntityManager(){
        entityList = new ArrayList<>();
    }
    public void add(Entity entity){
        entityList.add(entity);
    }
    public void remove(int index){
        entityList.remove(index);
    }
    public Entity get(int index){
        return entityList.get(index);
    }
    public void draw(SpriteBatch batch, ShapeRenderer shape){
        for (Entity entity : entityList){
            entity.draw(batch, shape);
        }
    }
    public void update(World world, Player player){
        for (Entity entity : entityList) {
            entity.update();
        }
        respawnEntities(world, player);
    }
    public void dispose() {
        for (Entity entity : entityList) {
            entity.dispose();
        }
    }
    public int size(){
        return entityList.size();
    }

    // The code below are specific for current game logic use case, Majority is to reduce code in GameMaster

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
        return entityList.stream().noneMatch(entity ->
                Math.abs(entity.getX() - x) < ((TextureObject) entity).getWidth() &&
                        Math.abs(entity.getY() - y) < ((TextureObject) entity).getHeight()
        );
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
    // This method will create entity based on specified type (Enemy, NeutralObject, AggressiveObject)
    public void spawnEntity(World world, String textureFile, Vector2 position, EntityType entityType, Player player) {
        Entity entity;
        switch (entityType) {
            case ENEMY:
                entity = new Enemy(world, textureFile, position.x, position.y, 50);
                ((Enemy) entity).setPlayer(player);
                break;
            case NEUTRAL_OBJECT:
                entity = new NeutralObject(world, textureFile, position.x, position.y);
                break;
            case AGGRESSIVE_OBJECT:
                entity = new AggressiveObject(world, textureFile, position.x, position.y);
                break;
            default:
                return;
        }
        add(entity);
    }
    // This method will schedule enemy spawning based on count
    public void scheduleEnemySpawning(World world, float count, Entity player) {
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                if (entityList.size() < count) {
                    Vector2 pos;
                    do {
                        pos = new Vector2(
                                MathUtils.random(0.9f) * Gdx.graphics.getWidth(),
                                MathUtils.random(0.9f) * Gdx.graphics.getHeight()
                        );
                    } while (Vector2.dst(pos.x, pos.y, player.getX(), player.getY()) < 200);
                    spawnEntity(world, "enemy.png", pos, EntityType.ENEMY, (Player) player);
                }
            }
        }, 0, 1/2f);
    }
    // This method will respawn entities if they are out of screen
    private void respawnEntities(World world, Player player) {
        for (int i = 0; i < entityList.size(); i++) {
            Entity entity = entityList.get(i);
            if ((entity instanceof Enemy || entity instanceof NeutralObject || entity instanceof AggressiveObject)
                && isOutOfScreen(entity, player)) {
                world.destroyBody(entity.getBody());
                entityList.remove(i);
                Vector2 pos;
                do {
                    pos = generateRespawnPosition(player);
                } while (!isPositionValid(pos.x, pos.y));
                EntityType entityType = entity instanceof Enemy ? EntityType.ENEMY :
                    entity instanceof NeutralObject ? EntityType.NEUTRAL_OBJECT : EntityType.AGGRESSIVE_OBJECT;
                spawnEntity(world, entity instanceof Enemy ? "enemy.png" :
                        entity instanceof NeutralObject ? "neutralObject.png" : "aggressiveObject.png",
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
    // This Vector2 class is generally used for position calculation
    public static class Vector2 {
        float x;
        float y;
        Vector2(float x, float y) {
            this.x = x;
            this.y = y;
        }
        static float dst(float x1, float y1, float x2, float y2) {
            return (float) Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));
        }
    }

}
