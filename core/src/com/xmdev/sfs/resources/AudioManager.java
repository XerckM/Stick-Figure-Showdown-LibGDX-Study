package com.xmdev.sfs.resources;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

import java.util.ArrayList;

public class AudioManager {
    // settings
    private boolean musicEnabled = true;
    private boolean soundsEnabled = true;

    // music
    private final Music music;

    // UI sounds
    private final Sound clickSound;

    // game sounds
    private final Sound blockSound;
    private final Sound booSound;
    private final Sound cheerSound;
    private final Sound hitSound;

    // arraylist of all sounds
    private final ArrayList<Sound> allGameSounds;

    public AudioManager(AssetManager assetManager) {
        // get all audio assets from the asset manager
        music = assetManager.get(Assets.MUSIC);
        clickSound = assetManager.get(Assets.CLICK_SOUND);
        blockSound = assetManager.get(Assets.BLOCK_SOUND);
        booSound = assetManager.get(Assets.BOO_SOUND);
        cheerSound = assetManager.get(Assets.CHEER_SOUND);
        hitSound = assetManager.get(Assets.HIT_SOUND);

        // create an arraylist of all game sounds for easy pausing, resuming, and stopping all at once
        allGameSounds = new ArrayList<>();
        allGameSounds.add(blockSound);
        allGameSounds.add(booSound);
        allGameSounds.add(cheerSound);
        allGameSounds.add(hitSound);

        // set music to loop
        music.setLooping(true);
    }

    public void enableMusic() {
        // enable music
        musicEnabled = true;

        // if music isn't already playing, play it
        if (!music.isPlaying()) {
            music.play();
        }
    }

    public void disableMusic() {
        // disable music
        musicEnabled = false;

        // if music is playing, stop it
        if (music.isPlaying()) {
            music.stop();
        }
    }

    public void toggleMusic() {
        // enable or disable music
        if (musicEnabled) {
            disableMusic();
        } else {
            enableMusic();
        }
    }

    public void playMusic() {
        // if music is enabled and isn't playing, play it
        if (musicEnabled && !music.isPlaying()) {
            music.play();
        }
    }

    public void pauseMusic() {
        // if music is enabled and currently playing, pause it
        if (musicEnabled && music.isPlaying()) {
            music.pause();
        }
    }

    public void enableSounds() {
        // enable sounds
        soundsEnabled = true;
    }

    public void disableSounds() {
        // disable sounds
        soundsEnabled = false;
    }

    public void playSound(String soundAsset) {
        // if sounds are enabled, play requested sound
        if (soundsEnabled) {
            switch (soundAsset) {
                case Assets.CLICK_SOUND:
                    clickSound.play();
                    break;
                case Assets.BLOCK_SOUND:
                    blockSound.play();
                    break;
                case Assets.BOO_SOUND:
                    booSound.play();
                    break;
                case Assets.CHEER_SOUND:
                    cheerSound.play();
                    break;
                case Assets.HIT_SOUND:
                    hitSound.play();
            }
        }
    }

    public void pauseGameSounds() {
        // pause any instances of game sounds that are currently playing
        for (Sound sound : allGameSounds) {
            sound.pause();
        }
    }

    public void resumeGameSounds() {
        // resume any instances of game sounds that were paused
        for (Sound sound : allGameSounds) {
            sound.resume();
        }
    }

    public void stopGameSounds() {
        // stop any instances of game sounds that are currently playing
        for (Sound sound : allGameSounds) {
            sound.stop();
        }
    }
}
