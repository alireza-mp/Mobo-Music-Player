package com.digimoplus.moboplayer.util

import androidx.datastore.preferences.core.stringPreferencesKey

object PreferencesKeys {

    val musicTitleKey = stringPreferencesKey("MUSIC_TITLE_KEY")
    val musicCurrentPositionKey = stringPreferencesKey("MUSIC_CURRENT_POSITION_KEY")
    val musicDurationKey = stringPreferencesKey("MUSIC_DURATION_KEY")
    val musicPlayListStateKey = stringPreferencesKey("MUSIC_PLAY_LIST_STATE_KEY")
    val musicCurrentPlayListKey = stringPreferencesKey("MUSIC_CURRENT_PLAY_LIST_KEY")

}