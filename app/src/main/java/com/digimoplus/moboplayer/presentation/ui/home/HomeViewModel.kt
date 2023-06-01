package com.digimoplus.moboplayer.presentation.ui.home

import android.app.Application
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.digimoplus.moboplayer.data.device.player.MusicPlayer
import com.digimoplus.moboplayer.data.device.player.MusicPlayerService
import com.digimoplus.moboplayer.data.device.player.MusicPlayerUiListener
import com.digimoplus.moboplayer.domain.models.LastDataStore
import com.digimoplus.moboplayer.domain.models.Music
import com.digimoplus.moboplayer.domain.models.PlayListItem
import com.digimoplus.moboplayer.domain.useCase.GetLastDataStoreUseCase
import com.digimoplus.moboplayer.domain.useCase.GetPlayListsUseCase
import com.digimoplus.moboplayer.domain.useCase.ModifyPlayListUseCase
import com.digimoplus.moboplayer.util.DataState
import com.digimoplus.moboplayer.util.ModifyState
import com.digimoplus.moboplayer.util.MusicState
import com.digimoplus.moboplayer.util.PlayListState
import com.digimoplus.moboplayer.util.Sort
import com.digimoplus.moboplayer.util.UiState
import com.digimoplus.moboplayer.util.convertPercentageToSecond
import com.digimoplus.moboplayer.util.findItemById
import com.digimoplus.moboplayer.util.sortMusics
import com.digimoplus.moboplayer.util.startMediaPlayerService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class HomeViewModel
@Inject
constructor(
    private val getPlayListsUseCase: GetPlayListsUseCase,
    private val getLastDataStoreUseCase: GetLastDataStoreUseCase,
    private val modifyPlayListUseCase: ModifyPlayListUseCase,
    private val musicPlayer: MusicPlayer,
    private val app: Application,
) : ViewModel() {


    private val _playLists = mutableStateListOf<PlayListItem>()
    val playLists: List<PlayListItem> = _playLists

    var currentPlayListIndex by mutableStateOf(0)
        private set

    private lateinit var currentPlayList: PlayListItem

    var sortState by mutableStateOf(Sort.DATE)
        private set
    var modifyState by mutableStateOf(ModifyState.None)
        private set
    var musicUIState by mutableStateOf(MusicState.Pause)
        private set
    var playListState by mutableStateOf(PlayListState.CURRENT)
        private set
    var uiState by mutableStateOf(UiState.Loading)
        private set
    var currentMusicUi by mutableStateOf(Music())
        private set
    var duration by mutableStateOf("0:00")
        private set
    var percentage by mutableStateOf(0f)
        private set
    var openRemoveDialog by mutableStateOf(false)
        private set

    // bottom sheet animation progress fraction
    var currentFraction by mutableStateOf(0f)
        private set

    // save button enabled state in bottomSheet
    var saveModifyEnabled by mutableStateOf(true)
        private set

    private lateinit var serviceConnection: ServiceConnection

    // index of playlist is show dialog for deleting
    private var deletingPlayListIndex = -1

    // index of playlist is modifying
    private var modifyingPlayListIndex = -1

    // save edit list for cancel editing
    private var saveEditedList = listOf<Music>()

    // get all playlists
    fun getAllMusics() {
        if (_playLists.isEmpty()) {
            viewModelScope.launch(Dispatchers.IO) {
                val result = getPlayListsUseCase()
                val lastData = getLastDataStoreUseCase()
                withContext(Dispatchers.Main) {
                    if (result is DataState.Success && lastData is DataState.Success) {
                        _playLists.addAll(result.data)
                        // update player data
                        // call ui success in service connection
                        getService(lastDataStore = lastData.data)
                    } else {
                        uiState = UiState.Error
                    }
                }
            }
        }
    }

    // on music item clicked
    fun onItemClick(playlistIndex: Int, musicIndex: Int) {
        viewModelScope.launch {
            musicPlayer.seekToIndex(
                _playLists[playlistIndex], music = _playLists[playlistIndex].musics[musicIndex]
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

    // update play list state
    fun onPlayListStateChange(playListState: PlayListState) {
        musicPlayer.updatePlayListState(playListState)
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
                playListState: PlayListState,
                currentPlayList: PlayListItem,
            ) {
                this@HomeViewModel.playListState = playListState
                this@HomeViewModel.percentage = percentage
                this@HomeViewModel.duration = duration
                this@HomeViewModel.currentPlayListIndex = _playLists.findItemById(currentPlayList)
                this@HomeViewModel.currentPlayList = currentPlayList
            }

            override fun updatePlayListState(playListState: PlayListState) {
                this@HomeViewModel.playListState = playListState
            }

            override fun updateCurrentPlayList(item: PlayListItem) {
                currentPlayListIndex = _playLists.findItemById(item)
                currentPlayList = item
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
                viewModelScope.launch {
                    val binder: MusicPlayerService.ServiceBinder =
                        p1 as MusicPlayerService.ServiceBinder
                    val service = binder.getMediaPlayerService()
                    // set service media player listener from music player
                    service.musicServiceChangeListener = musicPlayer
                    // initial music player data
                    musicPlayer.initialData(
                        playLists = _playLists.toList(),
                        lastData = lastDataStore,
                        musicPlayerUiListener = initialMusicPlayerUIListener(), // set music player ui listener
                    )
                    setMusicPlayerDurationListener()
                    setMusicPlayerPercentageListener()
                    listeningPercentageStateChange()
                    // update ui state
                    uiState = UiState.Success
                }
            }

            override fun onServiceDisconnected(p0: ComponentName?) {
                // service disconnected
            }
        }
    }

    private fun getService(lastDataStore: LastDataStore) {
        // uiState success call in service connection
        serviceConnection = initialServiceConnection(lastDataStore)
        val intent = Intent(app, MusicPlayerService::class.java)
        app.bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE)
    }

    override fun onCleared() {
        super.onCleared()
        app.unbindService(serviceConnection)
    }

    fun updateCurrentFraction(fraction: Float) {
        currentFraction = fraction
    }

    // change current playlist
    fun onPlayListChange(index: Int) {
        musicPlayer.changePlayList(_playLists[index])
    }

    suspend fun saveModify(name: String): Int {
        val pagerIndex = when (modifyState) {
            ModifyState.Add -> {
                saveAddNewPlayList(name)
                _playLists.lastIndex
            }

            ModifyState.Edit -> {
                saveEditedPlayList(name)
                modifyingPlayListIndex
            }

            else -> {
                cancelModifying()
                0
            }
        }
        modifyState = ModifyState.None
        return pagerIndex
    }

    private suspend fun saveAddNewPlayList(name: String) {
        withContext(Dispatchers.Main) {
            val newMusicList = _playLists[_playLists.lastIndex].musics.filter { it.isChecked }
            val newPlayListItem = PlayListItem(
                id = 0,
                title = name,
                musics = newMusicList.toMutableStateList(),
            )
            val newPlayListId = withContext(Dispatchers.IO) {
                modifyPlayListUseCase.addNewPlayListUseCase(newPlayListItem)
            }
            newPlayListItem.id = newPlayListId
            _playLists.add(_playLists.lastIndex, newPlayListItem)
            _playLists[_playLists.lastIndex].musics.forEach {
                it.isChecked = false
            }
        }
    }

    // save edited list
    private suspend fun saveEditedPlayList(name: String) {
        var modifiedMusicList = _playLists[modifyingPlayListIndex].musics.toList()
        modifiedMusicList = withContext(Dispatchers.IO) {
            modifiedMusicList.filter { it.isChecked }
        }
        // update list
        _playLists[modifyingPlayListIndex].musics.clear()
        _playLists[modifyingPlayListIndex].musics.addAll(modifiedMusicList)
        _playLists[modifyingPlayListIndex].title = name
        _playLists.sortMusics(sortState)
        // save to database
        val editedPlayList = _playLists[modifyingPlayListIndex]
        viewModelScope.launch(Dispatchers.IO) {
            modifyPlayListUseCase.updatePlayListUseCase(editedPlayList)
        }
        // update music player
        currentPlayList = playLists[modifyingPlayListIndex]
        if (modifyingPlayListIndex == currentPlayListIndex) {
            musicPlayer.changePlayList(currentPlayList)
        }
    }

    fun onMusicCheckedChange(playListIndex: Int, musicIndex: Int) {
        viewModelScope.launch {
            _playLists[playListIndex].musics[musicIndex] =
                _playLists[playListIndex].musics[musicIndex]
                    .copy(isChecked = !_playLists[playListIndex].musics[musicIndex].isChecked)

            if (modifyState == ModifyState.Edit) {
                saveModifyEnabled = isItemsChecked(modifyingPlayListIndex)
            } else {
                if (!isItemsChecked(playListIndex = _playLists.lastIndex)) {
                    modifyState = ModifyState.None
                } else if (modifyState == ModifyState.None) {
                    modifyState = ModifyState.Add
                }
            }
        }
    }

    private suspend fun isItemsChecked(playListIndex: Int): Boolean {
        return withContext(Dispatchers.IO) {
            for (i in _playLists[playListIndex].musics) {
                if (i.isChecked) {
                    return@withContext true
                }
            }
            return@withContext false
        }
    }

    fun onSortChange() {
        viewModelScope.launch {
            sortState = when (sortState) {
                Sort.DATE -> {
                    Sort.NAME
                }

                Sort.NAME -> {
                    Sort.Artist
                }

                Sort.Artist -> {
                    Sort.DATE
                }
            }
            _playLists.sortMusics(sortState)
        }
    }

    fun cancelModifying() {
        when (modifyState) {
            ModifyState.Add -> {
                onCancelAddNew()
            }

            ModifyState.Edit -> {
                onCancelEdit()
            }

            else -> {}
        }
    }


    fun onOpenDialog(index: Int) {
        if (currentPlayListIndex != index && modifyState == ModifyState.None) {
            deletingPlayListIndex = index
            openRemoveDialog = true
        }
    }

    fun dialogOnCancel() {
        openRemoveDialog = false
    }

    fun dialogOnDelete() {
        onDeletePlayList()
        openRemoveDialog = false
    }

    private fun onDeletePlayList() {
        val removedPlayList = _playLists[deletingPlayListIndex]
        _playLists.remove(removedPlayList)
        viewModelScope.launch(Dispatchers.IO) {
            modifyPlayListUseCase.deletePlayListUseCase(removedPlayList)
        }
    }

    fun onEditPlayListClick(playListIndex: Int) {
        // enable editing
        if (modifyState == ModifyState.None) {
            var allMusics = _playLists[0].musics.toList()
            modifyingPlayListIndex = playListIndex
            saveEditedList = _playLists[playListIndex].musics.toList()
            val currentMusicList = _playLists[playListIndex].musics.toList()
            allMusics = allMusics.filter { f -> currentMusicList.find { f.id == it.id } == null }
            for (i in 0 until _playLists[playListIndex].musics.size) {
                _playLists[playListIndex].musics[i] =
                    _playLists[playListIndex].musics[i].copy(isChecked = true)
            }
            _playLists[playListIndex].musics.addAll(allMusics)
            _playLists.sortMusics(sortState)
            modifyState = ModifyState.Edit
        }
    }

    private fun onCancelAddNew() {
        val addNewMusicList = _playLists[_playLists.lastIndex].musics
        for (i in 0 until addNewMusicList.size) {
            addNewMusicList[i] = addNewMusicList[i].copy(isChecked = false)
        }
        modifyState = ModifyState.None
    }

    private fun onCancelEdit() {
        saveModifyEnabled = true
        _playLists[modifyingPlayListIndex].musics.clear()
        _playLists[modifyingPlayListIndex].musics.addAll(saveEditedList)
        _playLists.sortMusics(sortState)
        modifyState = ModifyState.None
    }

    fun getUpdateListDefaultTitle(): String {
        return _playLists[modifyingPlayListIndex].title
    }

}

