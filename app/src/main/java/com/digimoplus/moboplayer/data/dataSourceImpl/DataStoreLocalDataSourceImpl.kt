package com.digimoplus.moboplayer.data.dataSourceImpl

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import com.digimoplus.moboplayer.data.dataSource.DataStoreDataSource
import com.digimoplus.moboplayer.util.PlayListState
import com.digimoplus.moboplayer.util.PreferencesKeys
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class DataStoreLocalDataSourceImpl
@Inject
constructor(
    private val dataStore: DataStore<Preferences>,
) : DataStoreDataSource {

    override suspend fun getLastMusicId(): Int =
        dataStore.data.first()[PreferencesKeys.musicTitleKey]?.toInt() ?: 0

    override suspend fun getLastPlayListId(): Int {
        return dataStore.data.first()[PreferencesKeys.musicCurrentPlayListKey]?.toInt() ?: 0
    }

    override suspend fun getLastMusicDuration(): Long =
        dataStore.data.first()[PreferencesKeys.musicDurationKey]?.toLong() ?: 0

    override suspend fun getLastMusicCurrentPosition(): Long =
        dataStore.data.first()[PreferencesKeys.musicCurrentPositionKey]?.toLong() ?: 0

    override suspend fun getPlayListState(): PlayListState {
        return when (dataStore.data.first()[PreferencesKeys.musicPlayListStateKey]?.toString()) {
            PlayListState.LOOP.state -> PlayListState.LOOP
            PlayListState.SHUFFLE.state -> PlayListState.SHUFFLE
            PlayListState.CURRENT.state -> PlayListState.CURRENT
            else -> PlayListState.CURRENT
        }
    }


    override suspend fun saveLastMusicData(
        duration: Long,
        currentPosition: Long,
        currentMusicId: Int,
        currentPlayListId: Int,
    ) {
        dataStore.edit {
            it[PreferencesKeys.musicDurationKey] = duration.toString()
            it[PreferencesKeys.musicCurrentPositionKey] = currentPosition.toString()
            it[PreferencesKeys.musicTitleKey] = currentMusicId.toString()
            it[PreferencesKeys.musicCurrentPlayListKey] = currentPlayListId.toString()
        }
    }

    override suspend fun savePlayListState(state: PlayListState) {
        dataStore.edit {
            it[PreferencesKeys.musicPlayListStateKey] = state.state
        }
    }

}