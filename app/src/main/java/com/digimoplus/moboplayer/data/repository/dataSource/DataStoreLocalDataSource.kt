package com.digimoplus.moboplayer.data.repository.dataSource

import com.digimoplus.moboplayer.util.PlayListState

interface DataStoreLocalDataSource {

    suspend fun getLastMusicTitle(): String

    suspend fun getLastMusicDuration(): Long

    suspend fun getLastMusicCurrentPosition(): Long

    suspend fun getPlayListState(): PlayListState

    suspend fun saveLastMusicData(
        duration: Long,
        currentPosition: Long,
        musicTitle: String,
    )

    suspend fun savePlayListState(
        state: PlayListState,
    )
}