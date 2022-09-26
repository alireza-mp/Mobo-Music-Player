package com.example.musicapplication.data.repository.dataSource

interface DataStoreLocalDataSource {

    suspend fun getLastMusicTitle(): String

    suspend fun getLastMusicDuration(): String

    suspend fun getLastMusicPercentage(): Float

    suspend fun getIsShuffle(): Boolean

    suspend fun getIsLoop(): Boolean

    suspend fun saveData(
        duration: String,
        isLoop: Boolean,
        isShuffle: Boolean,
        percentage: Float,
        musicTitle: String,
    )

}