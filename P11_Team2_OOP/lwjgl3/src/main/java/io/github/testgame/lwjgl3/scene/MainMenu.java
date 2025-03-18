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
import io.github.testgame.lwjgl3.abstractEngine.*;

public class MainMenu extends Scene {
    private BitmapFont font;
    private UIManager uiManager;
    private SceneManager sceneManager;
    private AudioManager audioManager;
    private Skin skin;
    private TextButton startButton, failButton, victoryButton, muteButton;
    private boolean isMuted = false;

    public MainMenu(SceneManager sceneManager, UIManager uiManager, AudioManager audioManager) {
        this.sceneManager = sceneManager;
        this.uiManager = uiManager;
        this.audioManager = audioManager;
        Gdx.input.setInputProcessor(stage); // Use stage from parent Scene class
    }

    @Override
    public void create() {
        font = new BitmapFont();
        font.getData().setScale(2);

        audioManager.loadMusic("background", "background_music.mp3");
        audioManager.playMusic("background", true);

        audioManager.loadSoundEffect("ammo", "sound_effect_ammo.mp3");
        audioManager.loadSoundEffect("noAmmo", "sound_effect_noAmmo.mp3");
        audioManager.loadSoundEffect("reload", "sound_effect_reload.mp3");
        audioManager.loadSoundEffect("damage", "sound_effect_damage.mp3");
        audioManager.loadSoundEffect("enemy", "sound_effect_enemy.mp3");
        audioManager.loadSoundEffect("start", "sound_effect_start.mp3");
        audioManager.loadSoundEffect("win", "sound_effect_win.mp3");
        audioManager.loadSoundEffect("lose", "sound_effect_lose.mp3");

        // Create skin for UI components
        skin = new Skin();
        createBasicSkin();

        // Title Label
        Label titleLabel = new Label("Scrub and Shoot", skin, "title");

        // Create the three buttons with their original names
        startButton = new TextButton("Start Game", skin);
        failButton = new TextButton("Fail", skin);
        victoryButton = new TextButton("Victory", skin);
        muteButton = new TextButton("Mute Music", skin);


        // Set up button click handlers
        startButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                audioManager.playSoundEffect("start");
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
        muteButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                toggleMute();
            }
        });

        // Set up the table layout
        Table table = new Table();
        table.setFillParent(true);
        table.add(titleLabel).padBottom(50).row();
        table.add(startButton).width(300).height(60).padBottom(20).row();
        table.add(failButton).width(300).height(60).padBottom(20).row();
        table.add(victoryButton).width(300).height(60).row();

        Table rootTable = new Table();
        rootTable.setFillParent(true);
        rootTable.top().right().pad(20);
        rootTable.add(muteButton).width(200).height(60);

        // Add table to the stage (using stage from parent Scene class)
        stage.addActor(table);
        stage.addActor(rootTable);
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

        // Title style
        Label.LabelStyle titleStyle = new Label.LabelStyle();
        titleStyle.font = font;
        titleStyle.fontColor = Color.GOLD;
        skin.add("title", titleStyle);
    }

    private void toggleMute() {
        isMuted = !isMuted;
        if (isMuted) {
            audioManager.muteMusic("background");
            muteButton.setText("Unmute Music");
        } else {
            audioManager.unmuteMusic("background");
            muteButton.setText("Mute Music");
        }
    }

    @Override
    public void render() {
        // Sync button state with actual music state
        boolean actualMuteState = audioManager.isMusicMuted("background");
        if (isMuted != actualMuteState) {
            isMuted = actualMuteState;
            muteButton.setText(isMuted ? "Unmute Music" : "Mute Music");
        }

        Gdx.gl.glClearColor(0, 172/255f, 193/255f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

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
