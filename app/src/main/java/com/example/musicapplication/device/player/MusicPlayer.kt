package com.example.musicapplication.device.player

import android.util.Log
import com.example.musicapplication.domain.models.Music
import com.example.musicapplication.util.MusicState
import com.example.musicapplication.util.convertMilliSecondsToSecond
import com.example.musicapplication.util.convertPercentageToMilliSeconds
import com.example.musicapplication.util.convertPositionToPercentage
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
    private var autoNext = false

    //for set last music percentage
    private var lastPercentage = -1f

    private var musicState = MusicState.Complete
    private var currentMusicIndex = 0
    private lateinit var musicList: List<Music>
    private lateinit var currentMusic: Music
    private lateinit var exoPlayer: ExoPlayer
    private var uiListener: MusicPlayerUiListener? = null


    fun initialData(exoPlayer: ExoPlayer, list: List<Music>, lastMusicIndex: Int, lastP: Float) {
        musicList = list
        this.exoPlayer = exoPlayer
        lastPercentage = lastP
        // update current music with last music
        if (musicList.isNotEmpty()) {
            //currentMusic = musicList[lastMusicIndex]
            // currentMusicIndex = lastMusicIndex
            //uiListener?.updateCurrentMusic(currentMusic)
            this.exoPlayer.addMediaItems(musicToMediaList(musicList))
            this.exoPlayer.addListener(object : Player.Listener {

                override fun onIsPlayingChanged(isPlaying: Boolean) {
                    super.onIsPlayingChanged(isPlaying)
                    if (isPlaying) {
                        uiListener?.play()
                        musicState = MusicState.Play
                    } else {
                        musicState = MusicState.Pause
                        uiListener?.pause()
                    }
                }

                override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
                    super.onMediaItemTransition(mediaItem, reason)
                    //exoPlayer.prepare()
                    uiListener?.updateCurrentMusic(mediaItemToMusic(mediaItem))
                }

                override fun onPlaybackStateChanged(state: Int) {
                    if (state == Player.STATE_ENDED) {
                        Log.i("my log", "onPlaybackStateChanged: ended")
                    }
                }
            })
        }
    }

    // play current music && update ui and state
    fun play() {

        exoPlayer.prepare()
        exoPlayer.play()

        //checkLastPercentage()
    }

    // pause current music && update ui and state
    fun pause() {
        if (exoPlayer.isPlaying) {
            exoPlayer.pause()
        }
    }

    // set music player on complete listener
    private fun onComplete() {
        /*mediaPlayer.setOnCompletionListener {
            // remove current music

        }*/
    }

    // enable loop mode
    fun setLoop(enabled: Boolean) {
        exoPlayer.repeatMode = if (enabled)
            Player.REPEAT_MODE_ONE
        else
            Player.REPEAT_MODE_ALL

    }

    // check last percentage state
    private fun checkLastPercentage() {
        if (lastPercentage != -1f) {
            seekTo(lastPercentage)
            removeLastPercentage()
        }
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

    fun setUiListener(MusicPlayerUiListener: MusicPlayerUiListener) {
        this.uiListener = MusicPlayerUiListener
    }

    fun playOrPauseMusic() {
        when (musicState) {
            MusicState.Play -> {
                pause()
            }
            MusicState.Pause -> {
                play()
            }
            MusicState.Complete -> {
                // if completed play again
                play()
            }
        }
    }

    fun updateAutoNext(state: Boolean) {
        autoNext = state
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


    // disable last percentage state
    private fun removeLastPercentage() {
        lastPercentage = -1f
    }
}