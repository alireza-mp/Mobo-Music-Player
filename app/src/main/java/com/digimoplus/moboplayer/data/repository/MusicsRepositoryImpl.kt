package com.digimoplus.moboplayer.data.repository

import com.digimoplus.moboplayer.data.repository.dataSource.DataStoreLocalDataSource
import com.digimoplus.moboplayer.data.repository.dataSource.MusicsLocalDataSource
import com.digimoplus.moboplayer.domain.models.LastDataStore
import com.digimoplus.moboplayer.domain.models.Music
import com.digimoplus.moboplayer.domain.models.mapToDomainModel
import com.digimoplus.moboplayer.domain.repostiry.MusicsRepository
import com.digimoplus.moboplayer.util.DataState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class MusicsRepositoryImpl
@Inject
constructor(
    private val musicsLocalDataSource: MusicsLocalDataSource,
    private val dataStoreLocalDataSource: DataStoreLocalDataSource,
) : MusicsRepository {

    // get all musics
    override suspend fun getAllMusicList(): Flow<DataState<List<Music>>> = flow {
        emit(DataState.Loading)
        val result = musicsLocalDataSource.getAllMusicLists()
        result ?: emit(DataState.Error)
        result?.let {
            emit(DataState.Success(result.map { mapToDomainModel(it) })) //add mapper
        }
    }

    // search for last music and return index in music list and get local lastDataStore
    override suspend fun getLastDataStore(): Flow<DataState<LastDataStore>> = flow {
        emit(DataState.Loading)
        val lastDataStore = LastDataStore(
            duration = dataStoreLocalDataSource.getLastMusicDuration(),
            currentPosition = dataStoreLocalDataSource.getLastMusicCurrentPosition(),
            lastMusicIndex = musicsLocalDataSource.getLastMusicIndex(),
            isShuffle = dataStoreLocalDataSource.getIsShuffle(),
            isLoop = dataStoreLocalDataSource.getIsLoop(),
        )
        emit(DataState.Success(lastDataStore))
    }

    override suspend fun saveLastMusicData(
        duration: Long,
        currentPosition: Long,
        musicTitle: String
    ) {
        dataStoreLocalDataSource.saveLastMusicData(duration, currentPosition,musicTitle)
    }

    override suspend fun savePlayListData(isLoop: Boolean, isShuffle: Boolean) {
        dataStoreLocalDataSource.savePlayListData(isLoop, isShuffle)
    }

}
