package com.example.musicapplication.device.player

import com.example.musicapplication.domain.models.LastDataStore
import com.example.musicapplication.domain.models.Music
import com.example.musicapplication.util.convertMilliSecondsToSecond
import com.example.musicapplication.util.convertPercentageToMilliSeconds
import com.example.musicapplication.util.convertPositionToPercentage
import com.example.musicapplication.util.convertPositionToPercentageNotSuspend
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flowOn

class MusicPlayer {

    // auto go to next music state
    //private var autoNext = false

    private lateinit var exoPlayer: ExoPlayer
    private var uiListener: MusicPlayerUiListener? = null
    val serviceMediaListener: ServiceMediaListener = object : ServiceMediaListener {

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

    }

    fun initialData(
        exoPlayer: ExoPlayer,
        musicList: List<Music>,
        lastData: LastDataStore,
        musicPlayerUiListener: MusicPlayerUiListener,
    ) {

        this.exoPlayer = exoPlayer
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
            uiListener?.updatePAndD(
                percentage = convertPositionToPercentageNotSuspend(
                    duration = lastData.duration,
                    currentPosition = lastData.currentPosition,
                ),
                duration = convertMilliSecondsToSecond(lastData.currentPosition)
            )
        }
    }

    private fun updateUiWithPlayerState() {
        if (exoPlayer.isPlaying) {
            uiListener?.play()
        } else {
            uiListener?.pause()
        }
        uiListener?.updateCurrentMusic(mediaItemToMusic(exoPlayer.currentMediaItem))
        uiListener?.updatePAndD(
            percentage = convertPositionToPercentageNotSuspend(
                duration = exoPlayer.duration,
                currentPosition = exoPlayer.currentPosition,
            ),
            duration = convertMilliSecondsToSecond(exoPlayer.currentPosition)
        )
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

    // enable loop mode
    fun setLoop(enabled: Boolean) {
        exoPlayer.repeatMode = if (enabled)
            Player.REPEAT_MODE_ONE
        else
            Player.REPEAT_MODE_ALL

    }

    // music play seekTo by percentage
    fun seekTo(percentage: Float) {
        exoPlayer.seekTo(convertPercentageToMilliSeconds(exoPlayer.duration, percentage))
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

    fun playOrPauseMusic() {
        if (exoPlayer.isPlaying) {
            pause()
        } else {
            play()
        }
    }

    fun updateAutoNext(state: Boolean) {
        //autoNext = state
    }


    // go to next music
    fun onNext() {
        if (exoPlayer.hasNextMediaItem()) {
            exoPlayer.seekToNextMediaItem()
        }
    }

    // go to back
    fun onPrevious() {
        if (exoPlayer.hasPreviousMediaItem()) {
            exoPlayer.seekToPreviousMediaItem()
        }
    }


    // on music item clicked
    fun onItemClick(music: Music, index: Int) {
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

}