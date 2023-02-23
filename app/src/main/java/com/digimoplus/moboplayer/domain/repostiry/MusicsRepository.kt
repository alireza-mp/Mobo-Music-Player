package com.digimoplus.moboplayer.domain.repostiry

import com.digimoplus.moboplayer.domain.models.LastDataStore
import com.digimoplus.moboplayer.util.DataState
import com.digimoplus.moboplayer.util.PlayListState

interface MusicsRepository {

    suspend fun getLastDataStore(): DataState<LastDataStore>

    suspend fun saveLastData(
        duration: Long,
        currentPosition: Long,
        musicId: Int,
        playListId: Int,
    )

    suspend fun savePlayListState(
        state: PlayListState,
    )

}