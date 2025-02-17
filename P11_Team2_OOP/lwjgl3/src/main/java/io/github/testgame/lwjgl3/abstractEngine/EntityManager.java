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
        entityList = new ArrayList<Entity>();
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

    // The code below are specific

    public void initializeEntities(World world, String textureFile, int count, Player player, Class<? extends Entity> entityType) {
        for (int i = 0; i < count; i++) {
            spawnEntity(world, textureFile, generateValidPosition(player), entityType, player);
        }
    }
    private Vector2 generateValidPosition(Player player) {
        float x, y;
        do {
            x = player.getBody().getPosition().x * 32 + MathUtils.random(-1.5f, 1.5f) * Gdx.graphics.getWidth();
            y = player.getBody().getPosition().y * 32 + MathUtils.random(-1.5f, 1.5f) * Gdx.graphics.getHeight();
        } while (!isPositionValid(x, y));
        return new Vector2(x, y);
    }
    public boolean isPositionValid(float x, float y) {
        return entityList.stream().noneMatch(entity ->
                Math.abs(entity.getX() - x) < ((TextureObject) entity).getWidth() &&
                        Math.abs(entity.getY() - y) < ((TextureObject) entity).getHeight()
        );
    }
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
    public void spawnEntity(World world, String textureFile, Vector2 position, Class<? extends Entity> entityType, Player player) {
        Entity entity;
        if (entityType == Enemy.class) {
            entity = new Enemy(world, textureFile, position.x, position.y, 50);
            ((Enemy) entity).setPlayer(player);
        } else if (entityType == NeutralObject.class) {
            entity = new NeutralObject(world, textureFile, position.x, position.y);
        } else if (entityType == AggressiveObject.class) {
            entity = new AggressiveObject(world, textureFile, position.x, position.y);
        } else {
            return;
        }
        add(entity);
    }
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
                    spawnEntity(world, "enemy.png", pos, Enemy.class, (Player) player);
                }
            }
        }, 0, 1/2f);
    }
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
                spawnEntity(world, entity instanceof Enemy ? "enemy.png" :
                                entity instanceof NeutralObject ? "neutralObject.png" : "aggressiveObject.png",
                        pos, entity.getClass(), player);
            }
        }
    }
    public static boolean isOutOfScreen(Entity entity, Player player) {
        return entity.getBody().getPosition().x * 32 < player.getBody().getPosition().x * 32 - Gdx.graphics.getWidth() ||
                entity.getBody().getPosition().x * 32 > player.getBody().getPosition().x * 32 + Gdx.graphics.getWidth() ||
                entity.getBody().getPosition().y * 32 < player.getBody().getPosition().y * 32 - Gdx.graphics.getHeight() ||
                entity.getBody().getPosition().y * 32 > player.getBody().getPosition().y * 32 + Gdx.graphics.getHeight();
    }
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
