package io.github.testgame.lwjgl3.collision;

import com.badlogic.gdx.math.Vector2;
import io.github.testgame.lwjgl3.entity.*;
import io.github.testgame.lwjgl3.abstractEngine.SceneManager;
import io.github.testgame.lwjgl3.scene.SceneType;

public class DamageHandler {
    public void applyDamageAndPushPlayer(Player player, Object collider, float damage) {
        player.setHealth(player.getHealth() - damage);
        if (player.getHealth() <= 0) {
            SceneManager.getInstance().changeScene(SceneType.FAIL);
            player.setHealth(10);
            player.setScore(0);
        } else {
            pushPlayerAway(player, collider);
        }
    }

    private void pushPlayerAway(Player player, Object collider) {
        Vector2 playerPosition = player.getBody().getPosition();
        Vector2 colliderPosition;

        if (collider instanceof Enemy) {
            colliderPosition = ((Enemy) collider).getBody().getPosition();
        } else if (collider instanceof AggressiveObject) {
            colliderPosition = ((AggressiveObject) collider).getBody().getPosition();
        } else {
            return; // If the collider is neither Enemy nor AggressiveObject, do nothing
        }

        // Determine direction with both position, then push player away by 1 unit
        Vector2 pushDirection = playerPosition.cpy().sub(colliderPosition).nor().scl(1);
        // Set player position to the new position
        player.getBody().setTransform(player.getBody().getPosition().add(pushDirection), player.getBody().getAngle());
    }
}
