package com.example.musicapplication.domain.useCase

import com.example.musicapplication.domain.models.HomeViewState
import com.example.musicapplication.domain.models.LastDataStore
import com.example.musicapplication.domain.models.Music
import com.example.musicapplication.util.DataState
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class GetHomeViewStateUseCase
@Inject
constructor(
    private val getLastDataStoreUseCase: GetLastDataStoreUseCase,
    private val getMusicsUseCase: GetMusicsUseCase,
) {

    suspend operator fun invoke() = initialData()

    // combine lastDataStore and musics list flows to homeViewState flow
    private suspend fun initialData(): Flow<DataState<HomeViewState>> = callbackFlow {

        var lastDataStore: LastDataStore? = null
        var musicsList: List<Music>? = null

        send(DataState.Loading)

        // get lastDataStore
        launch {
            getLastDataStoreUseCase().collect {
                when (it) {
                    is DataState.Loading -> {}
                    is DataState.Success -> {
                        musicsList?.let { item: List<Music> -> //if lastDataStore not null send data
                            trySend(DataState.Success(HomeViewState(it.data, item)))
                        }
                        lastDataStore = it.data // save data
                    }
                    is DataState.Error -> {
                        trySend(DataState.Error)
                        channel.close()
                    }
                }
            }
        }

        // get music list
        launch {
            getMusicsUseCase().collect {
                when (it) {
                    is DataState.Loading -> {}
                    is DataState.Success -> {
                        lastDataStore?.let { item: LastDataStore -> //if musicLIst not null send data
                            trySend(DataState.Success(HomeViewState(item, it.data)))
                        }
                        musicsList = it.data // sava data
                    }
                    is DataState.Error -> {
                        trySend(DataState.Error)
                        channel.close()
                    }
                }
            }
        }

        awaitClose {
            channel.close()
        }
    }
}













