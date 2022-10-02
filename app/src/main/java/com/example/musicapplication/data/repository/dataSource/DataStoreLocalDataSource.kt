package com.example.musicapplication.data.repository.dataSource

interface DataStoreLocalDataSource {

    suspend fun getLastMusicTitle(): String

    suspend fun getLastMusicDuration(): Long

    suspend fun getLastMusicCurrentPosition(): Long

    suspend fun getIsShuffle(): Boolean

    suspend fun getIsLoop(): Boolean

    suspend fun saveData(
        duration: Long,
        currentPosition: Long,
        isLoop: Boolean,
        isShuffle: Boolean,
        musicTitle: String,
    )

}