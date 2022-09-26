package com.example.musicapplication.data.repository.dataSourceImpl

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import com.example.musicapplication.data.repository.dataSource.DataStoreLocalDataSource
import com.example.musicapplication.util.PreferencesKeys
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class DataStoreLocalDataSourceImpl
@Inject
constructor(
    private val dataStore: DataStore<Preferences>,
) : DataStoreLocalDataSource {

    override suspend fun getLastMusicTitle(): String =
        dataStore.data.first()[PreferencesKeys.musicTitleKey] ?: "/+/"

    override suspend fun getLastMusicDuration(): String =
        dataStore.data.first()[PreferencesKeys.musicDurationKey] ?: "0:00"

    override suspend fun getLastMusicPercentage(): Float =
        dataStore.data.first()[PreferencesKeys.musicPercentageKey]?.toFloat() ?: 0.0f

    override suspend fun getIsShuffle(): Boolean =
        dataStore.data.first()[PreferencesKeys.isShuffleKey]?.toBoolean() ?: false

    override suspend fun getIsLoop(): Boolean =
        dataStore.data.first()[PreferencesKeys.isLoopKey]?.toBoolean() ?: false

    override suspend fun saveData(
        duration: String,
        isLoop: Boolean,
        isShuffle: Boolean,
        percentage: Float,
        musicTitle: String,
    ) {
        dataStore.edit {
            it[PreferencesKeys.musicDurationKey] = duration
            it[PreferencesKeys.musicTitleKey] = musicTitle
            it[PreferencesKeys.musicPercentageKey] = percentage.toString()
            it[PreferencesKeys.isLoopKey] = isLoop.toString()
            it[PreferencesKeys.isShuffleKey] = isShuffle.toString()
        }
    }

}