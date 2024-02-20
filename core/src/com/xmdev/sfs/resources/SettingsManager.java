package com.xmdev.sfs.resources;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

public class SettingsManager {
    // settings
    private static final String IS_MUSIC_ON = "isMusicOn";
    private static final String ARE_SOUNDS_ON = "areSoundsOn";
    private static final String DIFFICULTY_SETTING = "difficulty";
    private static final String IS_BLOOD_ON = "isBloodOn";
    private static final String IS_FULL_SCREEN_ON = "isFullScreenOn";
    private boolean musicSettingOn = true;
    private boolean soundsSettingOn = true;
    private GlobalVariables.Difficulty difficultySetting = GlobalVariables.Difficulty.EASY;
    private boolean bloodSettingOn = true;
    private boolean fullScreenSettingOn = false;

    // preferences
    private final Preferences prefs = Gdx.app.getPreferences("sfs.prefs");

    public void loadSettings() {
        // get all the settings from the preferences
        musicSettingOn = prefs.getBoolean(IS_MUSIC_ON, true);
        soundsSettingOn = prefs.getBoolean(ARE_SOUNDS_ON, true);
        switch (prefs.getInteger(DIFFICULTY_SETTING)) {
            case 0:
                difficultySetting = GlobalVariables.Difficulty.EASY;
                break;
            case 1:
                difficultySetting = GlobalVariables.Difficulty.MEDIUM;
                break;
            default:
                difficultySetting = GlobalVariables.Difficulty.HARD;
        }
        bloodSettingOn = prefs.getBoolean(IS_BLOOD_ON, true);
        fullScreenSettingOn = prefs.getBoolean(IS_FULL_SCREEN_ON, false);
    }

    public void toggleMusicSetting(boolean on) {
        // if the new setting is different update it
        if (musicSettingOn != on) {
            musicSettingOn = on;
            prefs.putBoolean(IS_MUSIC_ON, on).flush();
        }
    }

    public boolean isMusicSettingOn() {
        return musicSettingOn;
    }

    public void toggleSoundsSetting(boolean on) {
        // if the new setting is different update it
        if (soundsSettingOn != on) {
            soundsSettingOn = on;
            prefs.putBoolean(ARE_SOUNDS_ON, on).flush();
        }
    }

    public boolean isSoundsSettingOn() {
        return soundsSettingOn;
    }

    public void setDifficultySetting(GlobalVariables.Difficulty difficulty) {
        // if the new setting is different update it
        if (difficultySetting != difficulty) {
            difficultySetting = difficulty;
            int difficultyInt = 0;
            switch (difficultySetting) {
                case MEDIUM:
                    difficultyInt = 1;
                    break;
                case HARD:
                    difficultyInt = 2;
                    break;
            }
            prefs.putInteger(DIFFICULTY_SETTING, difficultyInt).flush();
        }
    }

    public GlobalVariables.Difficulty getDifficultySetting() {
        return difficultySetting;
    }

    public void toggleBloodSetting(boolean on) {
        // if the new setting is different update it
        if (bloodSettingOn != on) {
            bloodSettingOn = on;
            prefs.putBoolean(IS_BLOOD_ON, on).flush();
        }
    }

    public boolean isBloodSettingOn() {
        return bloodSettingOn;
    }

    public void toggleFullScreenSetting(boolean on) {
        // if the new setting is different update it
        if (fullScreenSettingOn != on) {
            fullScreenSettingOn = on;
            prefs.putBoolean(IS_FULL_SCREEN_ON, on).flush();
        }
    }

    public boolean isFullScreenSettingOn() {
        return fullScreenSettingOn;
    }
}
