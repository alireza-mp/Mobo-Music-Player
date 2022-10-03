package com.digimoplus.moboplayer.data.repository.dataSource

interface DataStoreLocalDataSource {

    suspend fun getLastMusicTitle(): String

    suspend fun getLastMusicDuration(): Long

    suspend fun getLastMusicCurrentPosition(): Long

    suspend fun getIsShuffle(): Boolean

    suspend fun getIsLoop(): Boolean

    suspend fun saveLastMusicData(
        duration: Long,
        currentPosition: Long,
        musicTitle: String,
    )

    suspend fun savePlayListData(
        isLoop: Boolean,
        isShuffle: Boolean,
    )
}