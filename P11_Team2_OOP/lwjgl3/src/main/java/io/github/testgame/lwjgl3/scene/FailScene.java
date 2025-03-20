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
import io.github.testgame.lwjgl3.abstractEngine.AudioManager;
import io.github.testgame.lwjgl3.abstractEngine.IOManager;
import io.github.testgame.lwjgl3.abstractEngine.SceneManager;

public class FailScene extends Scene {
    private BitmapFont font;
    private Skin skin;
    private SceneManager sceneManager;
    private AudioManager audioManager;
    private IOManager ioManager;
    private GameScene gameScene;
    private TextButton menuButton;
    private boolean originalMuteState;
    private boolean playSound = false;

    public FailScene(SceneManager sceneManager, GameScene gameScene, AudioManager audioManager, IOManager ioManager) {
        this.sceneManager = sceneManager;
        this.gameScene = gameScene;
        this.audioManager = audioManager;
        this.ioManager = ioManager;
    }

    @Override
    public void create() {
        font = new BitmapFont();
        font.getData().setScale(2);

        // Create skin for UI components
        skin = new Skin();
        createBasicSkin();

        // Create a title label
        Label titleLabel = new Label("You Lose!", skin, "title");

        // Create a button to return to the main menu
        menuButton = new TextButton("Menu", skin);
        menuButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (ioManager != null) {
                    ioManager.clearKeysPressed();
                }
                playSound = false;
                if (!originalMuteState) {
                    audioManager.unmuteMusic("background");
                }
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
        // Clear the scene with a red background
        Gdx.gl.glClearColor(130/255f, 40/255f, 40/255f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Update and draw stage
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();

        if (!playSound) {
            originalMuteState = audioManager.isMusicMuted("background");
            audioManager.muteMusic("background");
            audioManager.playSoundEffect("lose");
            playSound = true;
        }
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
