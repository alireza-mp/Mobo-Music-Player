package com.example.musicapplication.util

import androidx.datastore.preferences.core.stringPreferencesKey

object PreferencesKeys {

    val musicTitleKey = stringPreferencesKey("MUSIC_TITLE_KEY")
    val musicPercentageKey = stringPreferencesKey("MUSIC_PERCENTAGE_KEY")
    val musicDurationKey = stringPreferencesKey("MUSIC_DURATION_KEY")
    val isShuffleKey = stringPreferencesKey("IS_SHUFFLE_KEY")
    val isLoopKey = stringPreferencesKey("IS_LOOP_KEY")

}