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
    private GameScene gameScene;
    private Skin skin;
    private TextButton startButton, failButton, victoryButton, muteButton, instructionsButton;
    private boolean isMuted = false;
    private Slider volumeSlider;
    private Preferences prefs;
    private float previousVolume = 1f;
    private Transition sceneTransition;

    public MainMenu(SceneManager sceneManager, UIManager uiManager, AudioManager audioManager, Transition sceneTransition, GameScene gameScene)  {
        this.sceneManager = sceneManager;
        this.uiManager = uiManager;
        this.audioManager = audioManager;
        this.sceneTransition = sceneTransition;
        this.gameScene = gameScene;
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
        Label difficultyLabel = new Label("Select Difficulty", skin);

        // Create the three buttons with their original names
        startButton = new TextButton("Start Game", skin);
        failButton = new TextButton("Fail", skin);
        victoryButton = new TextButton("Victory", skin);
        muteButton = new TextButton("Mute Music", skin);
        instructionsButton = new TextButton("Instructions", skin);

        // Difficulty buttons
        TextButton easyButton = new TextButton("Easy", skin);
        TextButton mediumButton = new TextButton("Medium", skin);
        TextButton hardButton = new TextButton("Hard", skin);

        //select difficulty level
        easyButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                audioManager.playSoundEffect("ammo");
                prefs.putString("difficulty", "easy");
                prefs.flush();
                System.out.println("Difficulty set to Easy");
            }
        });

        mediumButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                audioManager.playSoundEffect("ammo");
                prefs.putString("difficulty", "medium");
                prefs.flush();
                System.out.println("Difficulty set to Medium");
            }
        });

        hardButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                audioManager.playSoundEffect("ammo");
                prefs.putString("difficulty", "hard");
                prefs.flush();
                System.out.println("Difficulty set to Hard");
            }
        });

        // Set up button click handlers
        startButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                audioManager.playSoundEffect("start");
                gameScene.resetGame();
                sceneTransition.startTransition(SceneType.GAME);
            }
        });

        failButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                sceneTransition.startTransition(SceneType.FAIL);
            }
        });

        victoryButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                sceneTransition.startTransition(SceneType.VICTORY);
            }
        });
        muteButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                toggleMute();
            }
        });

        instructionsButton = new TextButton("Instructions", skin);
        instructionsButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                audioManager.playSoundEffect("ammo");
                sceneManager.changeScene(SceneType.INSTRUCTIONS);
            }
        });

        // Set up the table layout
        Table table = new Table();
        table.setFillParent(true);
        table.add(titleLabel).padBottom(50).colspan(3).row();

        table.add(difficultyLabel).colspan(3).padBottom(10).row();
        table.add(easyButton).width(150).height(60).padRight(20);
        table.add(mediumButton).width(150).height(60).padRight(20);
        table.add(hardButton).width(150).height(60).row();
        table.add().colspan(3).height(20).row();

        table.add(startButton).width(490).height(70).colspan(3).padTop(25).padBottom(25).row();
//        table.add(failButton).width(300).height(60).padBottom(20).row();
//        table.add(victoryButton).width(300).height(60).padBottom(20).row();
        table.add(instructionsButton).width(490).height(60).colspan(3).padTop(25).padBottom(25).row();
        table.add(new Label("Volume", skin)).colspan(3).padTop(25).row();
        table.add(volumeSlider).width(490).height(30).colspan(3).padTop(25).row();

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
