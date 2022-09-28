package com.example.musicapplication.presentation.ui.home

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.musicapplication.device.player.MusicPlayer
import com.example.musicapplication.device.player.MusicPlayerUiListener
import com.example.musicapplication.domain.models.LastDataStore
import com.example.musicapplication.domain.models.Music
import com.example.musicapplication.domain.useCase.GetHomeViewStateUseCase
import com.example.musicapplication.domain.useCase.SaveLastMusicDataUseCase
import com.example.musicapplication.util.DataState
import com.example.musicapplication.util.MusicState
import com.example.musicapplication.util.UiState
import com.example.musicapplication.util.convertPercentageToSecond
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel
@Inject
constructor(
    private val homeViewUseCase: GetHomeViewStateUseCase,
    private val saveLastMusicUseCase: SaveLastMusicDataUseCase,
    private val musicPlayer: MusicPlayer,
) : ViewModel() {

    init {
        musicPlayerUiListener()
        setMusicPlayerDurationListener()
        setMusicPlayerPercentageListener()
    }

    var musicUIState by mutableStateOf(MusicState.Complete)
    var loopState by mutableStateOf(false)
    var shuffleState by mutableStateOf(false)
    val musicList = mutableStateListOf<Music>()
    var uiState by mutableStateOf(UiState.Loading)
        private set
    var currentMusicUi by mutableStateOf(Music())
        private set
    var duration by mutableStateOf("0:00")
        private set
    var percentage by mutableStateOf(0f)
        private set


    // get all music list
    fun getAllMusics() {
        viewModelScope.launch(Dispatchers.IO) {
            homeViewUseCase().onEach { result ->
                uiState = when (result) {
                    is DataState.Loading -> {
                        UiState.Loading
                    }
                    is DataState.Success -> {
                        musicList.addAll(result.data.musicList)
                        // update music player data
                        initialMusicPlayerData(result.data.lastDataStore, result.data.musicList)
                        UiState.Success
                    }
                    is DataState.Error -> {
                        UiState.Error
                    }
                }
            }.launchIn(viewModelScope)
        }
    }


    private fun initialMusicPlayerData(data: LastDataStore, musicList: List<Music>) {
        // set last music details from datastore && set music list
        musicPlayer.initialData(musicList, data.lastMusicIndex, data.percentage)
        // update duration state
        duration = data.duration
        // update percentage state
        percentage = data.percentage
    }

    // on music item clicked
    fun onItemClick(music: Music, index: Int) {
        musicPlayer.onItemClick(music, index)
    }

    // listening for percentage changing
    fun listeningPercentageStateChange() {
        viewModelScope.launch {
            snapshotFlow { percentage }.collect { percentage ->
                if (musicUIState == MusicState.Pause) {
                    // update duration when percentage change in seekbar
                    duration = convertPercentageToSecond(musicPlayer.getDuration(), percentage)
                }
            }
        }
    }

    // user finger up from seek bar
    fun onUpSeekBar() {
        viewModelScope.launch {
            // update player
            musicPlayer.seekTo(percentage)
            // play music
            musicPlayer.play()
        }
    }

    // user finger down from seek bar
    fun onDownSeekBar() {
        // pause music
        musicPlayer.pause()
    }

    // play or pause music
    fun playOrPauseMusic() {
        musicPlayer.playOrPauseMusic()
    }

    //save last music details to data store
    fun saveLastMusic() {
        viewModelScope.launch {
            if (musicUIState == MusicState.Play) { // if music is playing pause it
                musicPlayer.pause()
            }
            saveLastMusicUseCase.invoke(
                duration = duration,
                isLoop = loopState,
                isShuffle = shuffleState,
                percentage = percentage,
                musicTitle = currentMusicUi.title,
            )
        }
    }

    // update percentage state
    fun updatePercentage(percentage: Float) {
        this.percentage = percentage
    }

    // on previous click
    fun onPrevious() {
        musicPlayer.onPrevious()
    }

    // on next click
    fun onNext() {
        musicPlayer.onNext()
    }

    // enable music player shuffle auto next
    fun onShuffle(state: Boolean) {
        shuffleState = state
        musicPlayer.updateAutoNext(state)
    }


    // enable music player looping
    fun onLoop(state: Boolean) {
        loopState = state
        musicPlayer.setLoop(state)
    }

    // listening to music player ui change
    private fun musicPlayerUiListener() {

        musicPlayer.setUiListener(object : MusicPlayerUiListener {

            override fun play() {
                musicUIState = MusicState.Play
            }

            override fun pause() {
                musicUIState = MusicState.Pause
            }

            override fun onComplete() {
                musicUIState = MusicState.Complete
                percentage = 0f
                duration = "0:00"
            }

            override fun updateCurrentMusic(music: Music) {
                currentMusicUi = music
            }

            override fun updateAutoNext(state: Boolean) {
                shuffleState = state
            }
        })
    }

    // update duration state from music player
    private fun setMusicPlayerDurationListener() {
        viewModelScope.launch(Dispatchers.IO) {
            musicPlayer.getDurationFlow().onEach {
                // update duration
                duration = it
            }.launchIn(viewModelScope)
        }
    }

    // update percentage state from music player
    private fun setMusicPlayerPercentageListener() {
        viewModelScope.launch(Dispatchers.IO) {
            musicPlayer.getPercentageFlow().onEach {
                // update percentage
                percentage = it
            }.launchIn(viewModelScope)
        }
    }
}