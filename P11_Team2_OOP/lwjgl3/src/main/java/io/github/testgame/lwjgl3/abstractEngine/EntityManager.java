package io.github.testgame.lwjgl3.abstractEngine;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.physics.box2d.World;
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
    public void remove(Entity entity) {
        entityList.remove(entity); // Use the list's `remove(Object)` method
    }
    public final Entity get(int index){
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
    }
    public void dispose() {
        for (Entity entity : entityList) {
            entity.dispose();
        }
    }
    public int size(){
        return entityList.size();
    }
}
