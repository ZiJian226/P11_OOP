package io.github.testgame.lwjgl3.scene;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import io.github.testgame.lwjgl3.abstractEngine.*;

public class MainMenu extends Scene {
    private BitmapFont font;
    private UIManager uiManager;
    private SceneManager sceneManager;
    private Skin skin;
    private TextButton startButton, failButton, victoryButton;

    public MainMenu(SceneManager sceneManager, UIManager uiManager) {
        this.sceneManager = sceneManager;
        this.uiManager = uiManager;
        Gdx.input.setInputProcessor(stage); // Use stage from parent Scene class
    }

    @Override
    public void create() {
        font = new BitmapFont();
        font.getData().setScale(2);

        // Create skin for UI components
        skin = new Skin();
        createBasicSkin();

        // Title Label
        Label titleLabel = new Label("Scrub and Shoot", skin, "title");

        // Create the three buttons with their original names
        startButton = new TextButton("Start Game", skin);
        failButton = new TextButton("Fail", skin);
        victoryButton = new TextButton("Victory", skin);

        // Set up button click handlers
        startButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                sceneManager.changeScene(SceneType.GAME);
            }
        });

        failButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                sceneManager.changeScene(SceneType.FAIL);
            }
        });

        victoryButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                sceneManager.changeScene(SceneType.VICTORY);
            }
        });

        // Set up the table layout
        Table table = new Table();
        table.setFillParent(true);
        table.add(titleLabel).padBottom(50).row();
        table.add(startButton).width(300).height(60).padBottom(20).row();
        table.add(failButton).width(300).height(60).padBottom(20).row();
        table.add(victoryButton).width(300).height(60).row();

        // Add table to the stage (using stage from parent Scene class)
        stage.addActor(table);
    }

    private void createBasicSkin() {
        // Create a basic white texture
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.WHITE);
        pixmap.fill();
        skin.add("white", new Texture(pixmap));
        pixmap.dispose();

        // Button style
        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.up = skin.newDrawable("white", Color.FIREBRICK);
        textButtonStyle.down = skin.newDrawable("white", Color.RED);
        textButtonStyle.over = skin.newDrawable("white", Color.PINK);
        textButtonStyle.font = font;
        skin.add("default", textButtonStyle);

        // Label style
        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = font;
        labelStyle.fontColor = Color.WHITE;
        skin.add("default", labelStyle);

        // Title style
        Label.LabelStyle titleStyle = new Label.LabelStyle();
        titleStyle.font = font;
        titleStyle.fontColor = Color.GOLD;
        skin.add("title", titleStyle);
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0.2f, 0.2f, 0.2f, 1);
        Gdx.gl.glClear(Gdx.gl.GL_COLOR_BUFFER_BIT);

        // Update and draw stage
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
    }

    @Override
    public void dispose() {
        batch.dispose();
        shapeRenderer.dispose();
        font.dispose();
        skin.dispose();
        stage.dispose();
    }
}
