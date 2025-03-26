package io.github.testgame.lwjgl3.abstractEngine;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.utils.Disposable;

import java.util.HashMap;
import java.util.Map;

public class AudioManager implements Disposable {
    private final Map<String, Music> musicTracks;
    private final Map<String, Boolean> musicMuteStates;
    private final Map<String, Float> musicVolumeLevels;
    private final Map<String, Sound> soundEffects;

    public AudioManager() {
        musicTracks = new HashMap<>();
        musicMuteStates = new HashMap<>();
        musicVolumeLevels = new HashMap<>();
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

    public void pauseMusic(String name) {
        Music music = musicTracks.get(name);
        if (music != null && music.isPlaying()) {
            music.pause();
        }
    }

    public void resumeMusic(String name) {
        Music music = musicTracks.get(name);
        if (music != null) {
            music.play();
        }
    }

    public boolean isMusicLoaded(String name) {
        return musicTracks.containsKey(name);
    }

    public boolean isMusicPlaying(String name) {
        return musicTracks.containsKey(name) && musicTracks.get(name).isPlaying();
    }

    public boolean isSoundEffectLoaded(String name) {
        return soundEffects.containsKey(name);
    }

    public boolean isMusicMuted(String musicName) {
        return musicMuteStates.getOrDefault(musicName, false);
    }

    public void muteMusic(String musicName) {
        Music music = musicTracks.get(musicName);
        if (music != null) {
            music.setVolume(0f);
            musicMuteStates.put(musicName, true);
        }
    }

    public void unmuteMusic(String musicName) {
        Music music = musicTracks.get(musicName);
        if (music != null) {
            music.setVolume(1f); // Or original volume
            musicMuteStates.put(musicName, false);
        }
    }

    public void setMusicVolume(String musicName, float volume) {
        Music music = musicTracks.get(musicName);
        if (music != null) {
            musicVolumeLevels.put(musicName, volume);
            if (!isMusicMuted(musicName)) {
                music.setVolume(volume);
            }
        }
    }

    public float getMusicVolume(String musicName) {
        return musicVolumeLevels.getOrDefault(musicName, 1.0f);
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

    public Map<String, Music> getMusicTracks() {
        return musicTracks;
    }
}
