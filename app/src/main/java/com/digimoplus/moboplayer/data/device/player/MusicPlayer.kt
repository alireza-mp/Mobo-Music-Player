package com.digimoplus.moboplayer.data.device.player

import com.digimoplus.moboplayer.data.device.player.model.PlayerPlayList
import com.digimoplus.moboplayer.data.device.player.model.mapToDomainModel
import com.digimoplus.moboplayer.data.device.player.notification.UpdateMusicNotificationListener
import com.digimoplus.moboplayer.domain.models.LastDataStore
import com.digimoplus.moboplayer.domain.models.Music
import com.digimoplus.moboplayer.domain.models.PlayListItem
import com.digimoplus.moboplayer.domain.models.mapToMusicPlayerModel
import com.digimoplus.moboplayer.util.*
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MusicPlayer
@Inject constructor(
    private val exoPlayer: ExoPlayer,
) : MusicServiceChangeListener {

    private var uiListener: MusicPlayerUiListener? = null
    private var updateNotification: UpdateMusicNotificationListener? = null
    private lateinit var currentPlayList: PlayerPlayList
    var playListState: PlayListState = PlayListState.CURRENT
        private set

    //serviceMediaListener
    override fun onIsPlayingChanged(isPlaying: Boolean) {
        if (isPlaying) {
            uiListener?.play()
        } else {
            uiListener?.pause()
        }
    }

    override fun onMediaItemTransition(mediaItem: MediaItem?) {
        uiListener?.updateCurrentMusic(mediaItemToMusic(mediaItem))
    }

    suspend fun initialData(
        playLists: List<PlayListItem>,
        lastData: LastDataStore,
        musicPlayerUiListener: MusicPlayerUiListener,
    ) {
        this.uiListener = musicPlayerUiListener

        if (playLists.isNotEmpty()) {
            // check media player in service is exist or not
            if (exoPlayer.mediaItemCount == 0) { // if media player PlayList is empty

                setMediaPlayerData(playLists, lastData)

            } else { // media player playList not empty

                // update ui with media player state
                updateUiWithPlayerState()
            }
        }
    }

    private suspend fun setMediaPlayerData(playLists: List<PlayListItem>, lastData: LastDataStore) {
        withContext(Dispatchers.IO) {
            val playerList = playLists.map { it.mapToMusicPlayerModel() }
            currentPlayList = try {
                playerList.find { it.id == lastData.lastPlayListId } ?: playerList[0]
            } catch (e: Exception) {
                playerList[0]
            }
        }
        // find playlist index

        val musicList = currentPlayList.musics
        // find music index
        val mIndex = try {
            withContext(Dispatchers.IO) {
                musicList.withIndex().first { it.value.id.toInt() == lastData.lastMusicId }.index
            }
        } catch (e: Exception) {
            -1
        }
        // add music list ot media player playList
        exoPlayer.addMediaItems(musicToMediaList(musicList))

        // update media player with last music in dataStore
        if (mIndex != -1) {
            exoPlayer.seekTo(mIndex, lastData.currentPosition)
            // update ui duration and percentage
            uiListener?.updateUiByPlayerState(
                percentage = convertPositionToPercentageNotSuspend(
                    duration = lastData.duration,
                    currentPosition = lastData.currentPosition,
                ),
                duration = convertMilliSecondsToSecond(lastData.currentPosition),
                playListState = lastData.playListState,
                currentPlayList = currentPlayList.mapToDomainModel(),
            )
        }
        updatePlayListState(lastData.playListState)
    }

    private fun updateUiWithPlayerState() {
        if (exoPlayer.isPlaying) {
            uiListener?.play()
        } else {
            uiListener?.pause()
        }
        uiListener?.updateUiByPlayerState(
            percentage = convertPositionToPercentageNotSuspend(
                duration = exoPlayer.duration,
                currentPosition = exoPlayer.currentPosition,
            ),
            duration = convertMilliSecondsToSecond(exoPlayer.currentPosition),
            playListState = playListState,
            currentPlayList = currentPlayList.mapToDomainModel(),
        )
        uiListener?.updateCurrentMusic(mediaItemToMusic(exoPlayer.currentMediaItem))
    }

    // play current music && update ui and state
    fun play() {
        if (!exoPlayer.isPlaying) {
            exoPlayer.prepare()
            exoPlayer.play()
        }
    }

    // pause current music && update ui and state
    fun pause() {
        if (exoPlayer.isPlaying) {
            exoPlayer.pause()
        }
    }

    fun updatePlayListState(state: PlayListState) {
        playListState = state
        uiListener?.updatePlayListState(state)
        updateNotification?.updatePlayListState(state)
        when (state) {
            PlayListState.CURRENT -> {
                exoPlayer.repeatMode = Player.REPEAT_MODE_ALL
                exoPlayer.shuffleModeEnabled = false
            }
            PlayListState.SHUFFLE -> {
                exoPlayer.repeatMode = Player.REPEAT_MODE_ALL
                exoPlayer.shuffleModeEnabled = true
            }
            PlayListState.LOOP -> {
                exoPlayer.repeatMode = Player.REPEAT_MODE_ONE
                exoPlayer.shuffleModeEnabled = false
            }
        }
    }

    // music play seekTo by percentage
    fun seekTo(percentage: Float) {
        exoPlayer.prepare()
        exoPlayer.seekTo(convertPercentageToMilliSeconds(exoPlayer.duration, percentage))
    }

    // music play seekTo by long position
    fun seekTo(position: Long) {
        exoPlayer.prepare()
        exoPlayer.seekTo(position)
    }

    //update ui duration as flow every 150 millisecond
    suspend fun getDurationFlow(): Flow<String> = callbackFlow {
        while (true) {
            if (exoPlayer.isPlaying) {
                trySend(
                    convertMilliSecondsToSecond(
                        currentPosition = exoPlayer.currentPosition
                    )
                )
            }
            delay(150)
        }
    }.flowOn(Dispatchers.Main)

    //update ui percentage as flow every 150 millisecond
    suspend fun getPercentageFlow(): Flow<Float> = callbackFlow {
        while (true) {
            if (exoPlayer.duration > 0 && exoPlayer.isPlaying) {
                trySend(
                    convertPositionToPercentage(
                        duration = exoPlayer.duration, currentPosition = exoPlayer.currentPosition
                    )
                )
            }
            delay(150)
        }
    }.flowOn(Dispatchers.Main)

    fun getDuration(): Long = exoPlayer.duration

    // update notification by music player current position
    suspend fun getCurrentPositionFlow(): Flow<Long> = callbackFlow {
        while (true) {
            if (exoPlayer.duration > 0 && exoPlayer.isPlaying) {
                trySend(
                    exoPlayer.currentPosition
                )
            }
            delay(150)
        }
    }.flowOn(Dispatchers.Main)


    fun playOrPauseMusic() {
        if (exoPlayer.isPlaying) {
            pause()
        } else {
            play()
        }
    }

    // go to next music
    fun onNext() {
        if (exoPlayer.hasNextMediaItem()) {
            resetDetails()
            exoPlayer.seekToNextMediaItem()
        }
    }

    // go to back
    fun onPrevious() {
        if (exoPlayer.hasPreviousMediaItem()) {
            resetDetails()
            exoPlayer.seekToPreviousMediaItem()
        }
    }

    /* when player is not playing update percentage and duration to zero
      but when player is playing percentage and duration updated by player percentage and duration listener */
    private fun resetDetails() {
        if (!exoPlayer.isPlaying) {
            uiListener?.resetPercentageAndDuration()
        }
    }


    suspend fun seekToIndex(playListItem: PlayListItem, music: Music) {
        val item = playListItem.mapToMusicPlayerModel()
        val musicIndex = withContext(Dispatchers.IO) {
            item.musics.indexOf(music)
        }

        if (item != currentPlayList) {
            changePlayList(playListItem, musicIndex)
        } else {
            seekToItemIndex(item, musicIndex)
        }

    }

    fun removeNotification() {
        pause()
        updateNotification?.removeNotification()
    }

    fun setUpdateNotification(listener: UpdateMusicNotificationListener) {
        this.updateNotification = listener
    }

    fun changePlayList(playListItem: PlayListItem) {
        val item = playListItem.mapToMusicPlayerModel()
        val isPlaying = exoPlayer.isPlaying
        currentPlayList = item
        if (exoPlayer.isPlaying) exoPlayer.pause()
        exoPlayer.clearMediaItems()
        exoPlayer.addMediaItems(musicToMediaList(item.musics))
        exoPlayer.seekTo(0, 0)
        uiListener?.updateCurrentPlayList(item.mapToDomainModel())
        exoPlayer.prepare()
        if (isPlaying) {
            exoPlayer.play()
        }
    }

    private fun seekToItemIndex(playListItem: PlayerPlayList, musicIndex: Int) {
        val currentItemIndex = exoPlayer.currentMediaItem?.mediaId?.toLong()
        if (currentItemIndex != null && currentItemIndex != playListItem.musics[musicIndex].id) {
            exoPlayer.seekTo(musicIndex, 0)
            //play music
            if (!exoPlayer.isPlaying) {
                exoPlayer.prepare()
                exoPlayer.play()
            }
        }
    }

    private fun changePlayList(playListItem: PlayListItem, musicIndex: Int) {
        val item = playListItem.mapToMusicPlayerModel()
        currentPlayList = item
        if (exoPlayer.isPlaying) exoPlayer.pause()
        exoPlayer.clearMediaItems()
        exoPlayer.addMediaItems(musicToMediaList(item.musics))
        exoPlayer.seekTo(musicIndex, 0)
        uiListener?.updateCurrentPlayList(currentPlayList.mapToDomainModel())
        exoPlayer.prepare()
        exoPlayer.play()
    }

    fun getCurrentPlayListId(): Int = currentPlayList.id

}