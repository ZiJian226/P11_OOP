package io.github.testgame.lwjgl3.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;

public class Ammo extends NonCharacter {
    private float directionX, directionY, rotation;
    private Body body;
    private Player player;

    public Ammo(){
        super(null, 0, 0, 0);
    }
    public Ammo(String textureFile){
        super(textureFile, 0, 0, 0);
    }
    public Ammo(World world, String textureFile, Player player, float speed){
        super(textureFile, player.getBody().getPosition().x*32, player.getBody().getPosition().y*32, speed);
        this.player = player;
        this.body = createBox(world, player.getBody().getPosition().x * 32 + player.getDirection().x * player.getWidth(),
            player.getBody().getPosition().y * 32 + player.getDirection().y * player.getHeight(), getWidth(), getHeight(), false);
        this.body.setUserData(this);
    }
    @Override
    public Body getBody(){
        return body;
    }
    public Player getPlayer(){
        return player;
    }
    public void setDirection(Vector2 direction){
        this.directionY = direction.y;
        this.directionX = direction.x;
        this.rotation = (float) Math.atan2(direction.y, direction.x);
    }
    @Override
    public void moveAIControlled() {
        body.setTransform(body.getPosition().x + directionX * getSpeed() * Gdx.graphics.getDeltaTime() / 32,
                body.getPosition().y + directionY * getSpeed() * Gdx.graphics.getDeltaTime() / 32,
                rotation);

        setX(body.getPosition().x * 32);
        setY(body.getPosition().y * 32);
    }
    @Override
    public void draw(SpriteBatch batch, ShapeRenderer shape) {
        if (body != null && getTexture() != null) {
            batch.draw(getTexture(), body.getPosition().x * 32 - getWidth() / 2,
                body.getPosition().y * 32 - getHeight() / 2,
                getWidth() / 2, getHeight() / 2, getWidth(), getHeight(),
                1F, 1F, (float) Math.toDegrees(body.getAngle()),
                0, 0, getTexture().getWidth(), getTexture().getHeight(),
                false, false);
        }
    }
}
