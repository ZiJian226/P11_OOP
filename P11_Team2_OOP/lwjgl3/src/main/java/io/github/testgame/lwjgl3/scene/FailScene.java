package io.github.testgame.lwjgl3.scene;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
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
import io.github.testgame.lwjgl3.scene.sceneHelper.SceneFactory;
import io.github.testgame.lwjgl3.scene.sceneHelper.SceneType;
import io.github.testgame.lwjgl3.scene.sceneHelper.Transition;

public class FailScene extends Scene {
    private BitmapFont font;
    private Skin skin;
    private SceneManager sceneManager;
    private AudioManager audioManager;
    private IOManager ioManager;
    private TextButton menuButton;
    private TextButton retryButton;
    private Transition sceneTransition;
    private SceneFactory sceneFactory;
    private boolean originalMuteState;
    private boolean soundEffectPlayed = false;

    public FailScene(SceneManager sceneManager, AudioManager audioManager, IOManager ioManager,
                     Transition sceneTransition, SceneFactory sceneFactory) {
        this.sceneManager = sceneManager;
        this.audioManager = audioManager;
        this.ioManager = ioManager;
        this.sceneTransition = sceneTransition;
        this.sceneFactory = sceneFactory;
    }

    @Override
    public void create() {
        font = new BitmapFont();
        font.getData().setScale(2);

        // Create skin for UI components
        skin = new Skin();
        createBasicSkin();

        // Create a title label
        Label reasonLabel = new Label("No Health Left", skin, "title");
        Label titleLabel = new Label("You Lose!", skin, "title");

        // Create a button to return to the main menu
        menuButton = new TextButton("Menu", skin);
        menuButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (ioManager != null) {
                    ioManager.clearKeysPressed();
                }
                // Restore audio state FIRST, then transition
                restoreAudioState();
                sceneFactory.disposeScene(SceneType.FAIL);
                sceneTransition.startTransition(SceneType.MAIN_MENU);
            }
        });
        // 🔁 NEW: Retry button
        retryButton = new TextButton("Retry", skin);
        retryButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (ioManager != null) {
                    ioManager.clearKeysPressed();
                }
                // Restore audio state FIRST, then transition
                restoreAudioState();
                sceneFactory.disposeScene(SceneType.FAIL);
                sceneTransition.startTransition(SceneType.GAME);
            }
        });

        // Set up the table layout
        Table table = new Table();
        table.setFillParent(true);
        table.center();

        // Center "No Health Left" and "You Lose!"
        table.add(reasonLabel).expandX().center().padBottom(10);
        table.row();
        table.add(titleLabel).expandX().center().padBottom(50);
        table.row();

        // Center buttons
        Table buttonTable = new Table();
        buttonTable.add(menuButton).width(200).height(80).padRight(20);
        buttonTable.add(retryButton).width(200).height(80);

        table.add(buttonTable).expandX().center();
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

    private void restoreAudioState() {
        // Use preferences to ensure audio state is persisted
        Preferences prefs = Gdx.app.getPreferences("GamePreferences");
        boolean shouldBeMuted = prefs.getBoolean("isMuted", false);

        // Only unmute if it shouldn't be muted according to preferences
        if (!shouldBeMuted) {
            audioManager.unmuteMusic("background");
        }
    }

    @Override
    public void render() {
        // Clear the scene with a red background
        Gdx.gl.glClearColor(130/255f, 40/255f, 40/255f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Update and draw stage
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();

        if (!soundEffectPlayed) {
            originalMuteState = audioManager.isMusicMuted("background");
            audioManager.muteMusic("background");
            audioManager.playSoundEffect("lose");
            soundEffectPlayed = true;
        }
    }

    @Override
    public void dispose() {
        // Ensure music is restored even if scene is disposed
        restoreAudioState();

        shapeRenderer.dispose();
        batch.dispose();
        font.dispose();
        skin.dispose();
        stage.dispose();
    }

    @Override
    public void reset() {
        // Reset sound played flag
        this.soundEffectPlayed = false;

        // Store original music state when scene is reset
        originalMuteState = audioManager.isMusicMuted("background");

        // Mute music after storing original state
        audioManager.muteMusic("background");

        // Reset UI elements if needed
        if (stage != null) {
            stage.clear();
            create();
        }

        // Mark scene as active in lifecycle
        setLifeCycle(true);
    }
}
