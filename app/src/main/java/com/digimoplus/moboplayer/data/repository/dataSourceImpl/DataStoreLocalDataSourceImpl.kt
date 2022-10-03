package com.digimoplus.moboplayer.data.repository.dataSourceImpl

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import com.digimoplus.moboplayer.data.repository.dataSource.DataStoreLocalDataSource
import com.digimoplus.moboplayer.util.PreferencesKeys
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class DataStoreLocalDataSourceImpl
@Inject
constructor(
    private val dataStore: DataStore<Preferences>,
) : DataStoreLocalDataSource {

    override suspend fun getLastMusicTitle(): String =
        dataStore.data.first()[PreferencesKeys.musicTitleKey] ?: "/+/"

    override suspend fun getLastMusicDuration(): Long =
        dataStore.data.first()[PreferencesKeys.musicDurationKey]?.toLong() ?: 0

    override suspend fun getLastMusicCurrentPosition(): Long =
        dataStore.data.first()[PreferencesKeys.musicCurrentPosition]?.toLong() ?: 0

    override suspend fun getIsShuffle(): Boolean =
        dataStore.data.first()[PreferencesKeys.isShuffleKey]?.toBoolean() ?: false

    override suspend fun getIsLoop(): Boolean =
       dataStore.data.first()[PreferencesKeys.isLoopKey]?.toBoolean() ?: false

    override suspend fun saveLastMusicData(
        duration: Long,
        currentPosition: Long,
        musicTitle: String,
    ) {
        dataStore.edit {
            it[PreferencesKeys.musicDurationKey] = duration.toString()
            it[PreferencesKeys.musicCurrentPosition] = currentPosition.toString()
            it[PreferencesKeys.musicTitleKey] = musicTitle
        }
    }

    override suspend fun savePlayListData(isLoop: Boolean, isShuffle: Boolean) {
        dataStore.edit {
            it[PreferencesKeys.isLoopKey] = isLoop.toString()
            it[PreferencesKeys.isShuffleKey] = isShuffle.toString()
        }
    }

}