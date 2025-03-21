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

public class VictoryScene extends Scene {
    private BitmapFont font;
    private Skin skin;
    private SceneManager sceneManager;
    private AudioManager audioManager;
    private IOManager ioManager;
    private GameScene gameScene;
    private TextButton menuButton;
    private TextButton retryButton;
    private boolean originalMuteState;
    private boolean playSound = false;

    public VictoryScene(SceneManager sceneManager, AudioManager audioManager, IOManager ioManager) {
        this.sceneManager = sceneManager;
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
        Label titleLabel = new Label("You Win!", skin, "title");

        // ✅ Retry Button to Restart the Game
        retryButton = new TextButton("Retry", skin);
        retryButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (ioManager != null) {
                    ioManager.clearKeysPressed();
                }
                playSound = false;
                if (!originalMuteState) {
                    audioManager.unmuteMusic("background");
                }
                gameScene.resetGame(); // ✅ Reset game state
                sceneManager.changeScene(SceneType.GAME); // ✅ Switch to GameScene
            }
        });
        // Create a button to return to the main menu
        menuButton = new TextButton("Menu", skin);
        menuButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
//                gameScene.resetGame();
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

        // Set up the main layout
        Table table = new Table();
        table.setFillParent(true);
        table.center();
        table.add(titleLabel).padBottom(100);
        table.row();

        Table buttonTable = new Table();
        buttonTable.add(retryButton).width(200).height(80).padRight(20);
        buttonTable.add(menuButton).width(200).height(80);

        table.add(buttonTable).padTop(20);

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

        if (!playSound) {
            originalMuteState = audioManager.isMusicMuted("background");
            audioManager.muteMusic("background");
            audioManager.playSoundEffect("win");
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
