package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Timer;

import java.util.Iterator;

public class GameMaster extends ApplicationAdapter{
	private SpriteBatch batch;
	private Entity player;
	private EntityManager enemy;
	private EntityManager neutralObject;
	private EntityManager aggressiveObject;

//	private ShapeRenderer shape;
//	private EntityManager droplets;
//	private Entity bucket;
//	private Entity circle;
//	private Entity triangle;

	@Override
	public void create() {
		batch = new SpriteBatch();
		player = new Player("player.png", (float)Gdx.graphics.getWidth() / 2, (float)Gdx.graphics.getHeight() / 2, 200);
		neutralObject = new EntityManager();
		aggressiveObject = new EntityManager();
		enemy = new EntityManager();
		neutralObject.initializeNeutralEntities("neutralObject.png", 10);
		aggressiveObject.initializeAggressiveEntities("aggressiveObject.png", 10);
		enemy.scheduleEnemySpawning(10, player);

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

		neutralObject.draw(batch);
		aggressiveObject.draw(batch);

		player.draw(batch);
		player.update();
		((Player)player).drawBullets(batch);
		((Player)player).updateBullets();

		for (int i = 0; i < enemy.size(); i++) {
			((Enemy)enemy.get(i)).setPlayer((Player) player);
			enemy.get(i).draw(batch);
			enemy.get(i).update();
		}

		batch.end();



//		Iterator<Bullet> bulletIterator = ((Player)player).getBullets().iterator();
//		while (bulletIterator.hasNext()) {
//			Bullet bullet = bulletIterator.next();
//			bullet.draw(batch);
//			bullet.update();
//			if (bullet.isOutOfScreen()) {
//				bullet.dispose();
//				bulletIterator.remove();
//			}
//		}



//		for (int i = 0; i < enemy.size(); i++) {
//			((Enemy)enemy.get(i)).setPlayer((Player) player);
//			enemy.draw(batch);
//			enemy.get(i).update();
//		}

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
//		batch.end();
	}

	@Override
	public void dispose() {
		batch.dispose();
		player.dispose();
		neutralObject.dispose();
		aggressiveObject.dispose();
		enemy.dispose();

//		for (int i = 0; i < enemy.size(); i++) {
//			enemy.get(i).dispose();
//		}
//		for (Bullet bullet : ((Player)player).getBullets()) {
//			bullet.dispose();
//		}
//		for (int i = 0; i < NeutralObject.size(); i++) {
//			NeutralObject.get(i).dispose();
//		}
//		for (int i = 0; i < AggressiveObject.size(); i++) {
//			AggressiveObject.get(i).dispose();
//		}

//		shape.dispose();
//		((TextureObject)bucket).dispose();
//		for (int i = 0; i < droplets.size(); i++) {
//			droplets.remove(i);
//		}
	}
}

