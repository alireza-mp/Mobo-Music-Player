package com.digimoplus.moboplayer.device.player

import com.digimoplus.moboplayer.domain.models.LastDataStore
import com.digimoplus.moboplayer.domain.models.Music
import com.digimoplus.moboplayer.util.*
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class MusicPlayer
@Inject
constructor(
    private val exoPlayer: ExoPlayer,
) : MusicServiceChangeListener {

    private var uiListener: MusicPlayerUiListener? = null
    private var updateNotification: UpdateMusicNotificationListener? = null
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

    fun initialData(
        musicList: List<Music>,
        lastData: LastDataStore,
        musicPlayerUiListener: MusicPlayerUiListener,
    ) {
        this.uiListener = musicPlayerUiListener

        if (musicList.isNotEmpty()) {
            // check media player in service is exist or not
            if (exoPlayer.mediaItemCount == 0) { // if media player PlayList is empty

                setMediaPlayerData(musicList, lastData)

            } else { // media player playList not empty

                // update ui with media player state
                updateUiWithPlayerState()
            }
        }
    }

    private fun setMediaPlayerData(musicList: List<Music>, lastData: LastDataStore) {
        // add music list ot media player playList
        exoPlayer.addMediaItems(musicToMediaList(musicList))

        // update media player with last music in dataStore
        if (lastData.lastMusicIndex != -1) {
            exoPlayer.seekTo(lastData.lastMusicIndex, lastData.currentPosition)
            // update ui duration and percentage
            uiListener?.updateUiByPlayerState(
                percentage = convertPositionToPercentageNotSuspend(
                    duration = lastData.duration,
                    currentPosition = lastData.currentPosition,
                ),
                duration = convertMilliSecondsToSecond(lastData.currentPosition),
                playListState = lastData.playListState,
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
            playListState = playListState
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
                        duration = exoPlayer.duration,
                        currentPosition = exoPlayer.currentPosition
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

    // on music item clicked
    fun onItemClick(index: Int) {
        // check if current music is clicked or not
        if (exoPlayer.currentMediaItemIndex != index) {
            exoPlayer.seekTo(index, 0)
            //play music
            if (!exoPlayer.isPlaying) {
                exoPlayer.prepare()
                exoPlayer.play()
            }
        }
    }

    fun removeNotification() {
        pause()
        updateNotification?.removeNotification()
    }

    fun setUpdateNotification(listener: UpdateMusicNotificationListener) {
        this.updateNotification = listener
    }

}