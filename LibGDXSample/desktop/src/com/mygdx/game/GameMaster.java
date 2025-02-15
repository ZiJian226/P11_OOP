package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.abstractEngine.CollisionManager;
import com.mygdx.game.abstractEngine.EntityManager;
import com.mygdx.game.abstractEngine.IOManager;
import com.mygdx.game.abstractEngine.SceneManager;
import com.mygdx.game.entity.Enemy;
import com.mygdx.game.entity.Entity;
import com.mygdx.game.entity.Player;
import com.mygdx.game.screen.FailScene;
import com.mygdx.game.screen.MainMenu;
import com.mygdx.game.screen.VictoryScene;

public class GameMaster extends ApplicationAdapter{
	private SpriteBatch batch;
	private ShapeRenderer shape;
	private SceneManager sceneManager;
	private MainMenu mainMenu;
	private VictoryScene victoryScene;
	private FailScene failScene;
	private World world;
	private Camera camera;
	private Vector3 camPosition;
	private Entity player;
	private EntityManager enemy;
	private EntityManager neutralObject;
	private EntityManager aggressiveObject;

	private Box2DDebugRenderer b2dr;

    @Override
	public void create() {
		batch = new SpriteBatch();
		shape = new ShapeRenderer();

        IOManager ioManager = new IOManager();
		System.out.println("IOManager initialized: " + (ioManager != null));  //debug
		mainMenu = new MainMenu();
		failScene = new FailScene();
		victoryScene = new VictoryScene();
		world = new World(new Vector2(0,0), false);
		camera = new Camera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		camPosition = new Vector3();

		player = new Player(world, "player.png", (float)Gdx.graphics.getWidth() / 2, (float)Gdx.graphics.getHeight() / 2, 5000,2, ioManager);
		neutralObject = new EntityManager();
		aggressiveObject = new EntityManager();
		enemy = new EntityManager();

		b2dr = new Box2DDebugRenderer(true, false, false, false, false, true);

		Gdx.input.setInputProcessor(ioManager);
		mainMenu.create();
		failScene.create();
		victoryScene.create();
		sceneManager = SceneManager.getInstance();
		world.setContactListener(new CollisionManager());

		neutralObject.initializeNeutralEntities("neutralObject.png", 10);
		aggressiveObject.initializeAggressiveEntities("aggressiveObject.png", 10);
		enemy.scheduleEnemySpawning(world,10, player);
	}

	@Override
	public void render() {
		// Check the current scene and render accordingly
        switch (sceneManager.getCurrentScene()) {
            case "MainMenu":
                mainMenu.render(); // Render the main menu

                break;
            case "Game":
                ScreenUtils.clear(0, 0, 0.2f, 1);
                world.step(1 / 60f, 6, 2);
                batch.begin();
                shape.begin(ShapeRenderer.ShapeType.Filled);

                neutralObject.draw(batch, shape);
                aggressiveObject.draw(batch, shape);

                player.draw(batch, shape);
                player.update();
                ((Player) player).drawBullets(batch, shape);
                ((Player) player).updateBullets();

                for (int i = 0; i < enemy.size(); i++) {
                    ((Enemy) enemy.get(i)).setPlayer((Player) player);
                    enemy.get(i).draw(batch, shape);
                    enemy.get(i).update();
                }

				shape.end();
				batch.end();

				float delta = Gdx.graphics.getDeltaTime();
				update(delta);

                break;
            case "Fail":
                failScene.render();
                break;
            case "Victory":
                victoryScene.render();
                break;
        }
	}

	public void update(float delta){
		world.step(1/60f, 6,2);

		camPosition.x = ((Player)player).getPosition().x;
		camPosition.y = ((Player)player).getPosition().y;
		camera.cameraUpdate(delta,camPosition);
		batch.setProjectionMatrix(camera.camera.combined);
		shape.setProjectionMatrix(camera.camera.combined);

		b2dr.render(world,camera.camera.combined.scl(32));
	}

	@Override
	public void dispose() {
		batch.dispose();
		shape.dispose();
		mainMenu.dispose();
		failScene.dispose();
		victoryScene.dispose();
		world.dispose();
		player.dispose();
		neutralObject.dispose();
		aggressiveObject.dispose();
		enemy.dispose();
	}
}

