package com.digimoplus.moboplayer.data.repository

import com.digimoplus.moboplayer.data.dataSource.DataStoreDataSource
import com.digimoplus.moboplayer.data.dataSource.MusicsDataSource
import com.digimoplus.moboplayer.domain.models.LastDataStore
import com.digimoplus.moboplayer.domain.repostiry.MusicsRepository
import com.digimoplus.moboplayer.util.DataState
import com.digimoplus.moboplayer.util.PlayListState
import javax.inject.Inject

class MusicsRepositoryImpl
@Inject
constructor(
    private val musicsDataSource: MusicsDataSource,
    private val dataStoreDataSource: DataStoreDataSource,
) : MusicsRepository {

    override suspend fun getLastDataStore(): DataState<LastDataStore> {

        val lastDataStore = LastDataStore(
            duration = dataStoreDataSource.getLastMusicDuration(),
            currentPosition = dataStoreDataSource.getLastMusicCurrentPosition(),
            lastMusicId = musicsDataSource.getLastMusicId(),
            playListState = dataStoreDataSource.getPlayListState(),
            lastPlayListId = dataStoreDataSource.getLastPlayListId(),
        )
        return DataState.Success(lastDataStore)
    }

    override suspend fun saveLastData(
        duration: Long,
        currentPosition: Long,
        musicId: Int,
        playListId: Int,
    ) {
        dataStoreDataSource.saveLastMusicData(duration, currentPosition, musicId, playListId)
    }

    override suspend fun savePlayListState(state: PlayListState) {
        dataStoreDataSource.savePlayListState(state)
    }

}
