package com.example.musicapplication.data.repository

import com.example.musicapplication.data.repository.dataSource.DataStoreLocalDataSource
import com.example.musicapplication.data.repository.dataSource.MusicsLocalDataSource
import com.example.musicapplication.domain.models.LastDataStore
import com.example.musicapplication.domain.models.Music
import com.example.musicapplication.domain.models.mapToDomainModel
import com.example.musicapplication.domain.repostiry.MusicsRepository
import com.example.musicapplication.util.DataState
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
            percentage = dataStoreLocalDataSource.getLastMusicPercentage(),
            lastMusicIndex = musicsLocalDataSource.getLastMusicIndex(),
            isShuffle = dataStoreLocalDataSource.getIsShuffle(),
            isLoop = dataStoreLocalDataSource.getIsLoop(),
        )
        emit(DataState.Success(lastDataStore))
    }

    override suspend fun saveData(
        duration: String,
        isLoop: Boolean,
        isShuffle: Boolean,
        percentage: Float,
        musicTitle: String,
    ) {
        dataStoreLocalDataSource.saveData(duration, isLoop, isShuffle, percentage, musicTitle)
    }
}
