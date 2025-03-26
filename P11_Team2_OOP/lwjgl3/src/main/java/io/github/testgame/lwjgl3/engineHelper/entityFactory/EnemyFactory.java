package io.github.testgame.lwjgl3.engineHelper.entityFactory;

import com.badlogic.gdx.physics.box2d.World;
import io.github.testgame.lwjgl3.entity.moveableObject.Enemy;
import io.github.testgame.lwjgl3.entity.Entity;
import io.github.testgame.lwjgl3.entity.moveableObject.Player;

public class EnemyFactory implements EntityFactory {
    @Override
    public Entity createEntity(World world, String textureFile, float x, float y, Player player) {
        Enemy enemy = new Enemy(world, textureFile, x, y, 5);
        enemy.setPlayer(player);
        return enemy;
    }
}
