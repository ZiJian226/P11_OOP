package io.github.testgame.lwjgl3.engineHelper.entityFactory;

import com.badlogic.gdx.physics.box2d.World;
import io.github.testgame.lwjgl3.entity.Entity;
import io.github.testgame.lwjgl3.entity.staticObject.NeutralObject;
import io.github.testgame.lwjgl3.entity.moveableObject.Player;

public class NeutralObjectFactory implements EntityFactory {
    @Override
    public Entity createEntity(World world, String textureFile, float x, float y, Player player) {
        return new NeutralObject(world, textureFile, x, y);
    }
}
