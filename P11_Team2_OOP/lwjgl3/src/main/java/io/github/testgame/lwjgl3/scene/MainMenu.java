package io.github.testgame.lwjgl3.scene;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import io.github.testgame.lwjgl3.abstractEngine.*;

public class MainMenu extends Scene {
    private BitmapFont font;
    private UIManager uiManager;
    private SceneManager sceneManager;
    private AudioManager audioManager;
    private Skin skin;
    private TextButton startButton, failButton, victoryButton, muteButton;
    private boolean isMuted = false;
    private Slider volumeSlider;
    private Preferences prefs;
    private float previousVolume = 1f;
    private Transition sceneTransition;

    public MainMenu(SceneManager sceneManager, UIManager uiManager, AudioManager audioManager, Transition sceneTransition)  {
        this.sceneManager = sceneManager;
        this.uiManager = uiManager;
        this.audioManager = audioManager;
        this.sceneTransition = sceneTransition;
        Gdx.input.setInputProcessor(stage); // Use stage from parent Scene class
    }

    @Override
    public void create() {
        prefs = Gdx.app.getPreferences("GamePreferences");

        float savedVolume = prefs.getFloat("musicVolume", 1f);

        font = new BitmapFont();
        font.getData().setScale(2);

        audioManager.loadMusic("background", "audio/background_music.mp3");
        audioManager.playMusic("background", true);

        // Create skin for UI components
        skin = new Skin();
        createBasicSkin();

        // Volume Slider
        volumeSlider = new Slider(0f, 1f, 0.01f, false, skin); // Slider from 0 (mute) to 1 (max)
        volumeSlider.setValue(savedVolume); // Default value: max volume
        volumeSlider.addListener(event -> {
            float volume = volumeSlider.getValue();
            audioManager.getMusicTracks().values().forEach(music -> music.setVolume(volume));
            prefs.putFloat("musicVolume", volume);
            prefs.flush();
            return false;
        });

        audioManager.getMusicTracks().values().forEach(music -> music.setVolume(savedVolume));

        audioManager.loadSoundEffect("ammo", "audio/sound_effect_ammo.mp3");
        audioManager.loadSoundEffect("noAmmo", "audio/sound_effect_noAmmo.mp3");
        audioManager.loadSoundEffect("reload", "audio/sound_effect_reload.mp3");
        audioManager.loadSoundEffect("modifier", "audio/sound_effect_modifier.mp3");
        audioManager.loadSoundEffect("damage", "audio/sound_effect_damage.mp3");
        audioManager.loadSoundEffect("enemy", "audio/sound_effect_enemy.mp3");
        audioManager.loadSoundEffect("start", "audio/sound_effect_start.mp3");
        audioManager.loadSoundEffect("win", "audio/sound_effect_win.mp3");
        audioManager.loadSoundEffect("lose", "audio/sound_effect_lose.mp3");

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
                // Use transition instead of direct scene change
                sceneTransition.startTransition(SceneType.GAME);
            }
        });

        failButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
//                sceneManager.changeScene(SceneType.FAIL);
                sceneTransition.startTransition(SceneType.FAIL);
            }
        });

        victoryButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
//                sceneManager.changeScene(SceneType.VICTORY);
                sceneTransition.startTransition(SceneType.VICTORY);
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
        table.add(new Label("Volume", skin)).padTop(20).row();
        table.add(volumeSlider).width(300).height(30).padTop(10).row();

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

        // Slider style
        Slider.SliderStyle sliderStyle = new Slider.SliderStyle();
        sliderStyle.background = skin.newDrawable("pink", Color.LIGHT_GRAY);
        Drawable knobDrawable = skin.newDrawable("pink", Color.BLUE);
        knobDrawable.setMinWidth(20);
        knobDrawable.setMinHeight(20);
        sliderStyle.knob = knobDrawable;
        skin.add("default-horizontal", sliderStyle);
    }

    private void toggleMute() {
        isMuted = !isMuted;
        if (isMuted) {
            previousVolume = volumeSlider.getValue();
            audioManager.getMusicTracks().values().forEach(music -> music.setVolume(0f));
            volumeSlider.setValue(0f);
            muteButton.setText("Unmute Music");
        } else {
            audioManager.getMusicTracks().values().forEach(music -> music.setVolume(previousVolume));
            volumeSlider.setValue(previousVolume);
            muteButton.setText("Mute Music");
        }
    }
    @Override
    public void reload() {
        // Retrieve the saved volume from preferences
        float savedVolume = prefs.getFloat("musicVolume", 1f);
        previousVolume = savedVolume; // Restore the previous volume
        isMuted = (savedVolume == 0f); // Determine if the music is muted based on saved volume

        // Update slider and music state
        volumeSlider.setValue(savedVolume);
        audioManager.getMusicTracks().values().forEach(music -> music.setVolume(savedVolume));

        // Update mute button text
        muteButton.setText(isMuted ? "Unmute Music" : "Mute Music");
    }

    @Override
    public void render() {
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
