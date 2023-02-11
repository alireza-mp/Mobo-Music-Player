package com.digimoplus.moboplayer.domain.repostiry

import com.digimoplus.moboplayer.domain.models.LastDataStore
import com.digimoplus.moboplayer.domain.models.Music
import com.digimoplus.moboplayer.util.DataState
import com.digimoplus.moboplayer.util.PlayListState
import kotlinx.coroutines.flow.Flow

interface MusicsRepository {

    // get all musics in phone
    suspend fun getAllMusicList(): Flow<DataState<List<Music>>>

    //search for last music and return index in music list
    suspend fun getLastDataStore(): Flow<DataState<LastDataStore>>

    suspend fun saveLastMusicData(
        duration: Long,
        currentPosition: Long,
        musicTitle: String,
    )

    suspend fun savePlayListState(
        state: PlayListState,
    )

}