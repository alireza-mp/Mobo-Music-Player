package com.example.musicapplication.domain.repostiry

import com.example.musicapplication.domain.models.LastDataStore
import com.example.musicapplication.domain.models.Music
import com.example.musicapplication.util.DataState
import kotlinx.coroutines.flow.Flow

interface MusicsRepository {

    // get all musics in phone
    suspend fun getAllMusicList(): Flow<DataState<List<Music>>>

    //search for last music and return index in music list
    suspend fun getLastDataStore(): Flow<DataState<LastDataStore>>

    suspend fun saveData(
        duration: Long,
        currentPosition: Long,
        isLoop: Boolean,
        isShuffle: Boolean,
        musicTitle: String,
    )

}