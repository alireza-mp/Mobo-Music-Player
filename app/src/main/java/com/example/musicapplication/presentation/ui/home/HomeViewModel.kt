package com.example.musicapplication.presentation.ui.home

import android.app.Application
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.musicapplication.device.player.MediaPlayerService
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
    private val app: Application,
) : ViewModel() {

    var musicUIState by mutableStateOf(MusicState.Pause)
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

    private lateinit var lastData: LastDataStore


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
                        lastData = result.data.lastDataStore
                        inLastPAndD()
                        getService()
                        UiState.Success
                    }
                    is DataState.Error -> {
                        UiState.Error
                    }
                }
            }.launchIn(viewModelScope)
        }
    }


    private fun inLastPAndD() {

        // update duration state
        duration = lastData.duration
        // update percentage state
        percentage = lastData.percentage
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

    // listening to percentage change for update duration when user change seek bar and player is pause
    fun listeningPercentageStateChange() {
        viewModelScope.launch {
            snapshotFlow { percentage }.collect { percentage ->
                if (musicUIState == MusicState.Pause && musicPlayer.getDuration() >= 0) {
                    // update duration when percentage change in seekbar
                    duration = convertPercentageToSecond(musicPlayer.getDuration(), percentage)
                }
            }
        }
    }

    // update duration state from music player every 150 millisecond
    private fun setMusicPlayerDurationListener() {
        viewModelScope.launch(Dispatchers.IO) {
            musicPlayer.getDurationFlow().onEach {
                // update duration
                duration = it
            }.launchIn(viewModelScope)
        }
    }

    // update percentage state from music player every 150 millisecond
    private fun setMusicPlayerPercentageListener() {
        viewModelScope.launch(Dispatchers.IO) {
            musicPlayer.getPercentageFlow().onEach {
                // update percentage
                percentage = it
            }.launchIn(viewModelScope)
        }
    }

    // update ui with music player changes listener
    private fun initialMusicPlayerUIListener(): MusicPlayerUiListener {
        return object : MusicPlayerUiListener {

            override fun play() {
                musicUIState = MusicState.Play
            }

            override fun pause() {
                musicUIState = MusicState.Pause
            }

            override fun updatePAndD(percentage: Float, duration: String) {
                this@HomeViewModel.percentage = percentage
                this@HomeViewModel.duration = duration
            }

            override fun updateCurrentMusic(music: Music) {
                currentMusicUi = music
            }

            override fun updateAutoNext(state: Boolean) {
                shuffleState = state
            }
        }
    }

    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(p0: ComponentName?, p1: IBinder?) {
            val binder: MediaPlayerService.ServiceBinder = p1 as MediaPlayerService.ServiceBinder
            val exoPlayer = binder.getMediaPlayerService().exoPlayer
            musicPlayer.initialData(
                exoPlayer,
                musicList,
                lastData.lastMusicIndex,
                lastData.percentage
            )
            musicPlayerUiListener()
            setMusicPlayerDurationListener()
            setMusicPlayerPercentageListener()
            app.startService(Intent(app, MediaPlayerService::class.java))

        }

        override fun onServiceDisconnected(p0: ComponentName?) {

        }

    }

    private fun getService() {
        val intent = Intent(app, MediaPlayerService::class.java)
        app.bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE)
    }


}