package io.github.testgame.lwjgl3.engineHelper.entityFactory.factory;

import com.badlogic.gdx.physics.box2d.World;
import io.github.testgame.lwjgl3.entity.staticObject.AggressiveObject;
import io.github.testgame.lwjgl3.entity.Entity;
import io.github.testgame.lwjgl3.entity.moveableObject.Player;

public class AggressiveObjectFactory implements EntityFactory {
    @Override
    public Entity createEntity(World world, String textureFile, float x, float y, Player player) {
        return new AggressiveObject(world, textureFile, x, y);
    }
}
