package com.example.musicapplication.device.player

import android.media.MediaPlayer
import com.example.musicapplication.domain.models.Music
import com.example.musicapplication.util.MusicState
import com.example.musicapplication.util.convertDurationToPercentage
import com.example.musicapplication.util.convertMilliSecondsToSecond
import com.example.musicapplication.util.convertPercentageToMilliSeconds
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class MusicPlayer {

    // auto go to next music state
    private var autoNext = false

    //for set last music percentage
    private var lastPercentage = -1f

    private var musicState = MusicState.Complete
    private var currentMusicIndex = 0
    private lateinit var musicList: List<Music>
    private lateinit var currentMusic: Music
    private val mediaPlayer = MediaPlayer()
    private var path = ""
    private var uiListener: MusicPlayerUiListener? = null


    fun initialData(list: List<Music>, lastMusicIndex: Int, lastP: Float) {
        musicList = list
        lastPercentage = lastP
        // update current music with last music
        if (musicList.isNotEmpty()) {
            currentMusic = musicList[lastMusicIndex]
            currentMusicIndex = lastMusicIndex
            uiListener?.updateCurrentMusic(currentMusic)
        }
        // call media player on complete listener
        onComplete()
    }

    // play current music && update ui and state
    fun play(path: String = currentMusic.path) {
        // check music path is changed
        if (path != this.path) {
            mediaPlayer.reset()
            mediaPlayer.setDataSource(path)
            mediaPlayer.prepare()
        }
        mediaPlayer.start()
        this.path = path
        checkLastPercentage()
        uiListener?.play()
        musicState = MusicState.Play
    }

    // pause current music && update ui and state
    fun pause() {
        mediaPlayer.pause()
        uiListener?.pause()
        musicState = MusicState.Pause
    }

    // set music player on complete listener
    private fun onComplete() {
        mediaPlayer.setOnCompletionListener {
            // remove current music
            currentMusic.id = -1
            // go to next music
            autoNext()
        }
    }

    // enable loop mode
    fun setLoop(enabled: Boolean) {
        mediaPlayer.isLooping = enabled
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
        mediaPlayer.seekTo(convertPercentageToMilliSeconds(mediaPlayer.duration, percentage))
    }

    //update ui duration as flow every 150 millisecond
    suspend fun getDurationFlow(): Flow<String> = callbackFlow {
        while (true) {
            if (mediaPlayer.isPlaying) {
                trySend(convertMilliSecondsToSecond(mediaPlayer.currentPosition))
            }
            delay(150)
        }
    }

    //update ui percentage as flow every 150 millisecond
    suspend fun getPercentageFlow(): Flow<Float> = callbackFlow {
        while (true) {
            if (mediaPlayer.isPlaying) {
                trySend(convertDurationToPercentage(mediaPlayer.duration,
                    mediaPlayer.currentPosition))
            }
            delay(150)
        }
    }

    fun getDuration(): Int = mediaPlayer.duration

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

    // auto go to next music
    private fun autoNext() {
        // if auto music enabled
        if (autoNext) {
            // if lastest music
            if (musicList.size > (currentMusicIndex + 1)) {
                // go to next
                onNext()
            } else {
                // disable auto next
                autoNext = false
                uiListener?.updateAutoNext(false)
                uiListener?.onComplete()
                musicState = MusicState.Complete
            }
        } else { // call on complete
            uiListener?.onComplete()
            musicState = MusicState.Complete
        }
    }

    // go to next music
    fun onNext() {
        // check for lastest music
        if (musicList.size > (currentMusicIndex + 1)) {
            removeLastPercentage()
            // update current music
            currentMusicIndex++
            currentMusic = musicList[currentMusicIndex]
            uiListener?.updateCurrentMusic(currentMusic)
            // update music state
            play()
        }
    }

    // go to back
    fun onPrevious() {
        // check for first music
        if ((currentMusicIndex - 1) >= 0) {
            lastPercentage = -1f
            // update current music
            currentMusicIndex--
            currentMusic = musicList[currentMusicIndex]
            uiListener?.updateCurrentMusic(currentMusic)
            // update music state
            play()
        }
    }


    // on music item clicked
    fun onItemClick(music: Music, index: Int) {
        // disable last music details from datastore
        removeLastPercentage()
        // check if current music is clicked or not  || if music ended play again
        if (currentMusic.id != music.id || musicState == MusicState.Complete) {
            //start play "new music" / is not current music
            // update selected current music
            currentMusic = music
            uiListener?.updateCurrentMusic(currentMusic)
            // update currentMusicIndex
            currentMusicIndex = index
            //play music
            play()
        } else {
            // is current music
            // stop or start "current music"
            when (musicState) {
                MusicState.Play -> {
                    pause()
                }
                MusicState.Pause -> {
                    play()
                }
                else -> {}
            }
        }
    }


    // disable last percentage state
    private fun removeLastPercentage() {
        lastPercentage = -1f
    }
}