package io.github.testgame.lwjgl3.abstractEngine;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.utils.Disposable;

import java.util.HashMap;
import java.util.Map;

public class AudioManager implements Disposable {
    private final Map<String, Music> musicTracks;
    private final Map<String, Sound> soundEffects;

    public AudioManager() {
        musicTracks = new HashMap<>();
        soundEffects = new HashMap<>();
    }

    public void loadMusic(String name, String filePath) {
        Music music = Gdx.audio.newMusic(Gdx.files.internal(filePath));
        musicTracks.put(name, music);
    }

    public void playMusic(String name, boolean looping) {
        Music music = musicTracks.get(name);
        if (music != null) {
            music.setLooping(looping);
            music.play();
        }
    }

    public void stopMusic(String name) {
        Music music = musicTracks.get(name);
        if (music != null) {
            music.stop();
        }
    }

    public void loadSoundEffect(String name, String filePath) {
        Sound sound = Gdx.audio.newSound(Gdx.files.internal(filePath));
        soundEffects.put(name, sound);
    }

    public void playSoundEffect(String name) {
        Sound sound = soundEffects.get(name);
        if (sound != null) {
            sound.play();
        }
    }

    @Override
    public void dispose() {
        for (Music music : musicTracks.values()) {
            music.dispose();
        }
        for (Sound sound : soundEffects.values()) {
            sound.dispose();
        }
    }
}
