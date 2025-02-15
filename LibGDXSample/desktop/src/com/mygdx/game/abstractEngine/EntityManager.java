package com.mygdx.game.abstractEngine;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Timer;
import com.mygdx.game.entity.*;

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
    public void update() {
        for (Entity entity : entityList) {
            entity.update();
        }
    }
    public void dispose() {
        for (Entity entity : entityList) {
            entity.dispose();
        }
    }
    public int size(){
        return entityList.size();
    }

    public void initializeNeutralEntities(String textureFile, int count) {
        for (int i = 0; i < count; i++) {
            float x, y;
            boolean positionValid;
            do {
                x = MathUtils.random(0.9f) * Gdx.graphics.getWidth();
                y = MathUtils.random(0.9f) * Gdx.graphics.getHeight();
                float finalX = x;
                float finalY = y;
                positionValid = entityList.stream().noneMatch(entity ->
                        Math.abs(entity.getX() - finalX) < ((TextureObject) entity).getWidth() &&
                                Math.abs(entity.getY() - finalY) < ((TextureObject) entity).getHeight()
                );
            } while (!positionValid);
            add(new NeutralObject(textureFile, x, y));
        }
    }

    public void initializeAggressiveEntities(String textureFile, int count) {
        for (int i = 0; i < count; i++) {
            float x, y;
            boolean positionValid;
            do {
                x = MathUtils.random(0.9f) * Gdx.graphics.getWidth();
                y = MathUtils.random(0.9f) * Gdx.graphics.getHeight();
                float finalX = x;
                float finalY = y;
                positionValid = entityList.stream().noneMatch(entity ->
                        Math.abs(entity.getX() - finalX) < ((TextureObject) entity).getWidth() &&
                                Math.abs(entity.getY() - finalY) < ((TextureObject) entity).getHeight()
                );
            } while (!positionValid);
            add(new AggressiveObject(textureFile, x, y));
        }
    }

    public void scheduleEnemySpawning(World world, float count, Entity player) {
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                if (entityList.size() < count) {
                    float x, y;
                    boolean positionValid;
                    do {
                        x = MathUtils.random(0.9f) * Gdx.graphics.getWidth();
                        y = MathUtils.random(0.9f) * Gdx.graphics.getHeight();
                        float distance = (float) Math.sqrt(Math.pow(x - player.getX(), 2) + Math.pow(y - player.getY(), 2));
                        positionValid = distance >= 200;
                    } while (!positionValid);
                    add(new Enemy(world,"enemy.png", x, y, 50));
                }
            }
        }, 0, 1 / 2f);
    }
}
