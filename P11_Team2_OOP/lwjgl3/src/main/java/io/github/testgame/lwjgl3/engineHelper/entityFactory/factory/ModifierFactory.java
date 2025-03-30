package io.github.testgame.lwjgl3.engineHelper.entityFactory.factory;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.World;
import io.github.testgame.lwjgl3.entity.Entity;
import io.github.testgame.lwjgl3.entity.staticObject.Modifier;
import io.github.testgame.lwjgl3.entity.moveableObject.Player;

public class ModifierFactory implements EntityFactory {
    @Override
    public Entity createEntity(World world, String textureFile, float x, float y, Player player) {
        return new Modifier(world, textureFile, x, y, MathUtils.random(0.1f, 2));
    }
}
