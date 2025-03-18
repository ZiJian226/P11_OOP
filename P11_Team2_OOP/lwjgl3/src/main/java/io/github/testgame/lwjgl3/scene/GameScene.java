package io.github.testgame.lwjgl3.scene;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import io.github.testgame.lwjgl3.Camera;
import io.github.testgame.lwjgl3.engineHelper.*;
import io.github.testgame.lwjgl3.abstractEngine.*;
import io.github.testgame.lwjgl3.entity.*;

public class GameScene extends Scene implements iGameScene {
    private SpriteBatch batch, uiBatch;
    private ShapeRenderer shape;
    private BitmapFont font;
    private World world;
    private Camera camera;
    private Vector3 camPosition;
    private Entity player;
    private EntityManager enemy, neutralObject, aggressiveObject;
    private EntityHelper enemyHelper, neutralObjectHelper, aggressiveObjectHelper;
    private CollisionHelper collisionHelper;
    private Box2DDebugRenderer b2dr;
    private IOManager ioManager;
    private SceneManager sceneManager;
    private AudioManager audioManager;

    public GameScene(SceneManager sceneManager, AudioManager audioManager) {
        this.sceneManager = sceneManager;
        this.audioManager = audioManager;
    }

    @Override
    public void create() {
        batch = new SpriteBatch();
        uiBatch = new SpriteBatch();
        shape = new ShapeRenderer();

        ioManager = new IOManager();
        Gdx.input.setInputProcessor(ioManager);
        camera = new Camera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camPosition = new Vector3();

        resetGame();
        b2dr = new Box2DDebugRenderer(true, false, false, false, false, true);
    }

    @Override
    public void render() {
        if (Gdx.input.getInputProcessor() != ioManager) {
            Gdx.input.setInputProcessor(ioManager);
        }

        Gdx.gl.glClearColor(0, 172/255f, 193/255f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        world.step(1 / 60f, 6, 2);
        update(Gdx.graphics.getDeltaTime());

        batch.begin();
        shape.begin(ShapeRenderer.ShapeType.Filled);

        neutralObject.draw(batch, shape);
        aggressiveObject.draw(batch, shape);

        player.draw(batch, shape);
        player.update();
        ((Player) player).drawBubbles(batch, shape);
        ((Player) player).updateBubbles();

        for (int i = 0; i < enemy.size(); i++) {
            ((Enemy) enemy.get(i)).setPlayer((Player) player);
            enemy.get(i).draw(batch, shape);
            enemy.get(i).update();
        }

        shape.end();
        batch.end();

        uiBatch.begin();
        font.draw(uiBatch, "Score: " + ((Player) player).getScore(), 10,  40);
        font.draw(uiBatch, "Health: " + ((Player) player).getHealth(), 10, 80);
        uiBatch.end();
    }

    @Override
    public void dispose() {
        batch.dispose();
        uiBatch.dispose();
        shape.dispose();
        font.dispose();
        world.dispose();
        b2dr.dispose();
        player.dispose();
        enemy.dispose();
        neutralObject.dispose();
        aggressiveObject.dispose();
        collisionHelper.getDamageFlashEffect().dispose();
    }

    // Used to update game rendering
    public void update(float delta){
        world.step(1 / 60f, 6, 2);
        camPosition.x = ((Player)player).getPosition().x;
        camPosition.y = ((Player)player).getPosition().y;
        camera.cameraUpdate(delta,camPosition);
        batch.setProjectionMatrix(camera.camera.combined);
        shape.setProjectionMatrix(camera.camera.combined);

        b2dr.render(world,camera.camera.combined.scl(32));

        enemyHelper.update(world, (Player) player);
        neutralObjectHelper.update(world, (Player) player);
        aggressiveObjectHelper.update(world, (Player) player);
        collisionHelper.update((Player) player);
        collisionHelper.getDamageFlashEffect().update(Gdx.graphics.getDeltaTime());
        collisionHelper.getDamageFlashEffect().render();
    }

    // Main purpose is used to reset the game (start game and end game)
    public void resetGame() {
        if (player != null) player.dispose();
        if (neutralObject != null) neutralObject.dispose();
        if (aggressiveObject != null) aggressiveObject.dispose();
        if (enemy != null) enemy.dispose();
        if (font != null) font.dispose();
        if (world != null) world.dispose();

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("segoeuithisz.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 46;
        parameter.color = Color.NAVY;
        font = new BitmapFont();
        font = generator.generateFont(parameter);
        generator.dispose();

        world = new World(new Vector2(0, 0), false);

        player = new Player(world, "player.png", (float) Gdx.graphics.getWidth() / 2, (float) Gdx.graphics.getHeight() / 2, 5000, 10, ioManager, audioManager);

        neutralObject = new EntityManager();
        aggressiveObject = new EntityManager();
        enemy = new EntityManager();

        enemyHelper = new EntityHelper(enemy);
        neutralObjectHelper = new EntityHelper(neutralObject);
        aggressiveObjectHelper = new EntityHelper(aggressiveObject);

        neutralObjectHelper.initializeEntities(world, "soap.png", 10, (Player) player, EntityType.NEUTRAL_OBJECT);
        aggressiveObjectHelper.initializeEntities(world, "mud.png", 10, (Player) player, EntityType.AGGRESSIVE_OBJECT);
        enemyHelper.scheduleEnemySpawning(world, 10, player);

        collisionHelper = new CollisionHelper(sceneManager, audioManager);
        world.setContactListener(collisionHelper);
    }
}
