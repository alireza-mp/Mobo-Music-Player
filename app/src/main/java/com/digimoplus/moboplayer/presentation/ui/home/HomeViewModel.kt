package com.digimoplus.moboplayer.presentation.ui.home

import android.app.Application
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.digimoplus.moboplayer.device.player.MediaPlayerService
import com.digimoplus.moboplayer.device.player.MusicPlayer
import com.digimoplus.moboplayer.device.player.MusicPlayerUiListener
import com.digimoplus.moboplayer.device.player.ServiceUiChangeListener
import com.digimoplus.moboplayer.domain.models.LastDataStore
import com.digimoplus.moboplayer.domain.models.Music
import com.digimoplus.moboplayer.domain.useCase.GetHomeViewStateUseCase
import com.digimoplus.moboplayer.domain.useCase.SavePlayListUseCase
import com.digimoplus.moboplayer.util.*
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
    private val savePlayListUseCase: SavePlayListUseCase,
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
    private var serviceUiChangeListener: ServiceUiChangeListener? = null
    private lateinit var serviceConnection: ServiceConnection

    // bottom sheet animation progress fraction
    var currentFraction by mutableStateOf(0f)


    // get all music list
    fun getAllMusics() {
        viewModelScope.launch(Dispatchers.IO) {
            homeViewUseCase().onEach { result ->
                when (result) {
                    is DataState.Loading -> {
                        uiState = UiState.Loading
                    }
                    is DataState.Success -> {
                        musicList.addAll(result.data.musicList)
                        // update player data
                        // call ui success in service connection
                        getService(result.data.lastDataStore)
                    }
                    is DataState.Error -> {
                        uiState = UiState.Error
                    }
                }
            }.launchIn(viewModelScope)
        }
    }

    fun updateViewExistListener() {
        serviceUiChangeListener?.onViewExist(false)
    }

    // on music item clicked
    fun onItemClick(index: Int) {
        musicPlayer.onItemClick(index)
    }

    fun savePlayListState() {
        viewModelScope.launch {
            savePlayListUseCase.invoke(
                isLoop = loopState,
                isShuffle = shuffleState,
            )
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
    fun onPlayListChange(isShuffle: Boolean) {
        shuffleState = isShuffle
        musicPlayer.setShuffle(isShuffle)
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
                app.startMediaPlayerService()
            }

            override fun pause() {
                musicUIState = MusicState.Pause
            }

            override fun updateUiByPlayerState(
                percentage: Float,
                duration: String,
                isLoop: Boolean,
                isShuffle: Boolean,
            ) {
                loopState = isLoop
                shuffleState = isShuffle
                this@HomeViewModel.percentage = percentage
                this@HomeViewModel.duration = duration
            }

            override fun updateCurrentMusic(music: Music) {
                currentMusicUi = music
            }

            override fun resetPercentageAndDuration() {
                percentage = 0f
                duration = "0:00"
            }
        }
    }

    // service connection
    private fun initialServiceConnection(lastDataStore: LastDataStore): ServiceConnection {
        return object : ServiceConnection {
            override fun onServiceConnected(p0: ComponentName?, p1: IBinder?) {

                val binder: MediaPlayerService.ServiceBinder =
                    p1 as MediaPlayerService.ServiceBinder
                val service = binder.getMediaPlayerService()
                // set service media player listener from music player
                service.serviceMediaListener = musicPlayer
                // set view exist listener from service
                serviceUiChangeListener = service
                // set view exist true
                serviceUiChangeListener?.onViewExist(true)
                // initial music player data
                musicPlayer.initialData(
                    exoPlayer = service.exoPlayer,
                    musicList = musicList,
                    lastData = lastDataStore,
                    musicPlayerUiListener = initialMusicPlayerUIListener(), // set music player ui listener
                )
                setMusicPlayerDurationListener()
                setMusicPlayerPercentageListener()
                listeningPercentageStateChange()
                // update ui state
                uiState = UiState.Success
            }

            override fun onServiceDisconnected(p0: ComponentName?) {
                // service disconnected
            }
        }
    }

    private fun getService(lastDataStore: LastDataStore) {
        // uiState success call in service connection
        serviceConnection = initialServiceConnection(lastDataStore)
        val intent = Intent(app, MediaPlayerService::class.java)
        app.bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE)
    }

    override fun onCleared() {
        super.onCleared()
        app.unbindService(serviceConnection)
    }

}