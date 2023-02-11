package com.digimoplus.moboplayer.data.repository.dataSourceImpl

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import com.digimoplus.moboplayer.data.repository.dataSource.DataStoreLocalDataSource
import com.digimoplus.moboplayer.util.PlayListState
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
        musicTitle: String,
    ) {
        dataStore.edit {
            it[PreferencesKeys.musicDurationKey] = duration.toString()
            it[PreferencesKeys.musicCurrentPosition] = currentPosition.toString()
            it[PreferencesKeys.musicTitleKey] = musicTitle
        }
    }

    override suspend fun savePlayListState(state: PlayListState) {
        dataStore.edit {
            it[PreferencesKeys.musicPlayListStateKey] = state.state
        }
    }

}