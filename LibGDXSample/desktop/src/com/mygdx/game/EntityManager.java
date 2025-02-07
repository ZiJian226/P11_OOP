package com.mygdx.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.ArrayList;
import java.util.List;

public class EntityManager{
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
    public void draw(SpriteBatch batch){
        for (Entity entity : entityList){
            entity.draw(batch);
        }
    }
    public void moveAIControlled(){
        for (Entity entity : entityList){
            entity.moveAIControlled();
        }
    }
    public int size(){
        return entityList.size();
    }
}
