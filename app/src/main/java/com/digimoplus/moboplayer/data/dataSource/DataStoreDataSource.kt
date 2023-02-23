package com.digimoplus.moboplayer.data.dataSource

import com.digimoplus.moboplayer.util.PlayListState

interface DataStoreDataSource {

    suspend fun getLastMusicId(): Int
    suspend fun getLastPlayListId(): Int

    suspend fun getLastMusicDuration(): Long

    suspend fun getLastMusicCurrentPosition(): Long

    suspend fun getPlayListState(): PlayListState

    suspend fun saveLastMusicData(
        duration: Long,
        currentPosition: Long,
        currentMusicId: Int,
        currentPlayListId: Int,
    )

    suspend fun savePlayListState(
        state: PlayListState,
    )
}