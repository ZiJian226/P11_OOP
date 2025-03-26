package io.github.testgame.lwjgl3.engineHelper.entityFactory.factory;

import com.badlogic.gdx.physics.box2d.World;
import io.github.testgame.lwjgl3.entity.Entity;
import io.github.testgame.lwjgl3.entity.moveableObject.Player;

public interface EntityFactory {
    Entity createEntity(World world, String textureFile, float x, float y, Player player);
}
