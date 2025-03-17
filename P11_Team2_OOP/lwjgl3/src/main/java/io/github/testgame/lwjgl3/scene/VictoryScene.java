package io.github.testgame.lwjgl3.scene;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import io.github.testgame.lwjgl3.abstractEngine.SceneManager;

public class VictoryScene extends Scene {
    private BitmapFont font;
    private Skin skin;
    private SceneManager sceneManager;
    private GameScene gameScene;
    private TextButton menuButton;

    public VictoryScene(SceneManager sceneManager, GameScene gameScene) {
        this.sceneManager = sceneManager;
        this.gameScene = gameScene;
    }

    @Override
    public void create() {
        font = new BitmapFont();
        font.getData().setScale(2);

        // Create skin for UI components
        skin = new Skin();
        createBasicSkin();

        // Create a title label
        Label titleLabel = new Label("You Win!", skin, "title");

        // Create a button to return to the main menu
        menuButton = new TextButton("Menu", skin);
        menuButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                gameScene.resetGame();
                sceneManager.changeScene(SceneType.MAIN_MENU);
            }
        });

        // Set up the table layout
        Table table = new Table();
        table.setFillParent(true);
        table.add(titleLabel).padBottom(100);
        table.row();
        table.add(menuButton).width(200).height(80);

        stage.addActor(table);
    }

    private void createBasicSkin() {
        // Create a basic white texture
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.WHITE);
        pixmap.fill();
        skin.add("pink", new Texture(pixmap));
        pixmap.dispose();

        // Button style
        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.up = skin.newDrawable("pink", Color.PINK);
        textButtonStyle.down = skin.newDrawable("pink", Color.FIREBRICK);
        textButtonStyle.over = skin.newDrawable("pink", Color.FIREBRICK);
        textButtonStyle.font = font;
        skin.add("default", textButtonStyle);

        // Label style
        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = font;
        labelStyle.fontColor = Color.WHITE;
        skin.add("default", labelStyle);

        Label.LabelStyle titleStyle = new Label.LabelStyle();
        titleStyle.font = font;
        titleStyle.fontColor = Color.WHITE;
        skin.add("title", titleStyle);
    }

    @Override
    public void render() {
        // Clear the scene with a green background
        Gdx.gl.glClearColor(139/255f, 195/255f, 74/255f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Update and draw stage
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
    }

    @Override
    public void dispose() {
        shapeRenderer.dispose();
        batch.dispose();
        font.dispose();
        skin.dispose();
        stage.dispose();
    }
}
