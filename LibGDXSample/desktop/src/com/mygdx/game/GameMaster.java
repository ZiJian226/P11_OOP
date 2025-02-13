package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Timer;

public class GameMaster extends ApplicationAdapter{
	private SpriteBatch batch;
	private Entity player;
	private Entity enemy;
	private Entity bullet;
	private Entity NeutralObject;
	private Entity AggressiveObject;

//	private ShapeRenderer shape;
//	private EntityManager droplets;
//	private Entity bucket;
//	private Entity circle;
//	private Entity triangle;

	@Override
	public void create() {
		batch = new SpriteBatch();
		player = new Player("bucket.png", 0, 0, 100);
		enemy = new Enemy("bucket.png", 100, 100, 100);
		bullet = new Bullet("bucket.png", 200, 200, 100);
		NeutralObject = new NeutralObject("bucket.png", 300, 300);
		AggressiveObject = new AggressiveObject("bucket.png", 400, 400);


//		shape = new ShapeRenderer();
//		droplets = new EntityManager();
//		bucket = new Bucket("bucket.png", (float)Gdx.graphics.getWidth() / 2, 0, 1000);
//		circle = new Circle(Color.FIREBRICK, 50, 500, 200, 200);
//		triangle = new Triangle(Color.FOREST, 60, 60, 200, 50);
//
//		Timer.schedule(new Timer.Task(){
//			@Override
//			public void run() {
//				if (droplets.size() < 10) {
//					droplets.add(new Drop("droplet.png", MathUtils.random(0.9f) * Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), 2));
//				}
//			}
//		}, 0, 1/2f);


	}

	@Override
	public void render() {
		ScreenUtils.clear(0,0,0.2f,1);
		batch.begin();
//		shape.begin(ShapeRenderer.ShapeType.Filled);
//
//		for (int i = 0; i < droplets.size(); i++) {
//			droplets.draw(batch);
//			droplets.get(i).update();
//		}
//
//		triangle.draw(shape);
//		triangle.update();
//
//		circle.draw(shape);
//		circle.update();
//
//		bucket.draw(batch);
//		bucket.update();
//
//		shape.end();

		player.draw(batch);
		enemy.draw(batch);
		bullet.draw(batch);
		NeutralObject.draw(batch);
		AggressiveObject.draw(batch);

		batch.end();
	}

	@Override
	public void dispose() {
		batch.dispose();
		player.dispose();
		enemy.dispose();
		bullet.dispose();
		NeutralObject.dispose();
		AggressiveObject.dispose();

//		shape.dispose();
//		((TextureObject)bucket).dispose();
//		for (int i = 0; i < droplets.size(); i++) {
//			droplets.remove(i);
//		}
	}
}

