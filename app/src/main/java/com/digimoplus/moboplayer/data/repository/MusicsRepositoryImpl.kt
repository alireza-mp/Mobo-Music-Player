package com.digimoplus.moboplayer.data.repository

import com.digimoplus.moboplayer.data.dataSource.DataStoreDataSource
import com.digimoplus.moboplayer.data.dataSource.MusicsDataSource
import com.digimoplus.moboplayer.domain.models.LastDataStore
import com.digimoplus.moboplayer.domain.models.Music
import com.digimoplus.moboplayer.domain.models.mapToDomainModel
import com.digimoplus.moboplayer.domain.repostiry.MusicsRepository
import com.digimoplus.moboplayer.util.DataState
import com.digimoplus.moboplayer.util.PlayListState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class MusicsRepositoryImpl
@Inject
constructor(
    private val musicsDataSource: MusicsDataSource,
    private val dataStoreDataSource: DataStoreDataSource,
) : MusicsRepository {

    // get all musics
    override suspend fun getAllMusicList(): Flow<DataState<List<Music>>> = flow {
        emit(DataState.Loading)
        val result = musicsDataSource.getAllMusicLists()
        result ?: emit(DataState.Error)
        result?.let {
            emit(DataState.Success(result.map { it.mapToDomainModel() })) //add mapper
        }
    }

    // search for last music and return index in music list and get local lastDataStore
    override suspend fun getLastDataStore(): Flow<DataState<LastDataStore>> = flow {
        emit(DataState.Loading)
        val lastDataStore = LastDataStore(
            duration = dataStoreDataSource.getLastMusicDuration(),
            currentPosition = dataStoreDataSource.getLastMusicCurrentPosition(),
            lastMusicIndex = musicsDataSource.getLastMusicIndex(),
            playListState = dataStoreDataSource.getPlayListState()
        )
        emit(DataState.Success(lastDataStore))
    }

    override suspend fun saveLastMusicData(
        duration: Long,
        currentPosition: Long,
        musicTitle: String,
    ) {
        dataStoreDataSource.saveLastMusicData(duration, currentPosition, musicTitle)
    }

    override suspend fun savePlayListState(state: PlayListState) {
        dataStoreDataSource.savePlayListState(state)
    }

}
