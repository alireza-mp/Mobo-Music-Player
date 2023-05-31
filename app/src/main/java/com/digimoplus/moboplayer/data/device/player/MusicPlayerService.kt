package com.digimoplus.moboplayer.data.device.player

import android.app.*
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import com.digimoplus.moboplayer.data.device.player.notification.MusicNotificationActions
import com.digimoplus.moboplayer.data.device.player.notification.MusicNotificationManager
import com.digimoplus.moboplayer.data.device.player.notification.UpdateMusicNotificationListener
import com.digimoplus.moboplayer.domain.repostiry.MusicsRepository
import com.digimoplus.moboplayer.util.PlayListState
import com.digimoplus.moboplayer.util.stopForeground
import com.google.android.exoplayer2.C
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.audio.AudioAttributes
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import javax.inject.Inject

@AndroidEntryPoint
class MusicPlayerService : Service(), UpdateMusicNotificationListener {

    @Inject
    lateinit var exoPlayer: ExoPlayer

    @Inject
    lateinit var musicsRepository: MusicsRepository

    @Inject
    lateinit var musicNotificationManager: MusicNotificationManager

    @Inject
    lateinit var musicPlayer: MusicPlayer

    var musicServiceChangeListener: MusicServiceChangeListener? = null
    private val serviceBinder: IBinder = ServiceBinder()
    private var removeNotificationJob: Job? = null


    inner class ServiceBinder : Binder() {
        fun getMediaPlayerService(): MusicPlayerService {
            return this@MusicPlayerService
        }
    }

    override fun onBind(intent: Intent): IBinder {
        return serviceBinder
    }


    override fun onCreate() {
        super.onCreate()
        setPlayerAudioAttributes()
        musicNotificationManager.createNotificationChanel(this)
        initialPlayerListener()
        musicPlayer.setUpdateNotification(this)
    }


    private fun setPlayerAudioAttributes() {
        val audioAttributes = AudioAttributes.Builder()
            .setUsage(C.USAGE_MEDIA)
            .setContentType(C.AUDIO_CONTENT_TYPE_MUSIC)
            .build()
        exoPlayer.setAudioAttributes(audioAttributes, true)
        exoPlayer.setHandleAudioBecomingNoisy(true) // Pause music when headphones unplugged

        if (exoPlayer.repeatMode == Player.REPEAT_MODE_OFF)
            exoPlayer.repeatMode = Player.REPEAT_MODE_ALL
    }

    private fun initialPlayerListener() {
        exoPlayer.addListener(object : Player.Listener {

            override fun onIsPlayingChanged(isPlaying: Boolean) {
                super.onIsPlayingChanged(isPlaying)

                musicServiceChangeListener?.onIsPlayingChanged(isPlaying)
                removeNotification(isPlaying)
                updateNotificationPlayPause(isPlaying)

            }

            override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
                super.onMediaItemTransition(mediaItem, reason)

                musicServiceChangeListener?.onMediaItemTransition(mediaItem)
                updateNotificationMusicItem(mediaItem)

            }

        })
    }

    private fun removeNotification(isPlaying: Boolean) {
        removeNotificationJob = if (!isPlaying) {
            val s = SupervisorJob()
            CoroutineScope(Dispatchers.Main + s).launch {
                delay(MusicNotificationActions.NOTIFICATION_REMOVE_TIME) // remove notification after 5 minute
                saveData()
                stopForeground()
            }
        } else {
            removeNotificationJob?.cancel()
            null
        }
    }

    override fun onDestroy() {
        stopForeground()
        super.onDestroy()
    }

    private suspend fun saveData() {
        withContext(Dispatchers.Main) {
            val duration = exoPlayer.duration
            val currentPosition = exoPlayer.currentPosition
            val musicId = exoPlayer.currentMediaItem?.mediaId?.toInt() ?: 0
            val currentPlayListId = musicPlayer.getCurrentPlayListId()
            withContext(Dispatchers.IO) {
                musicsRepository.saveLastData(
                    duration = duration,
                    currentPosition = currentPosition,
                    musicId = musicId,
                    playListId = currentPlayListId,
                )
                musicsRepository.savePlayListState(musicPlayer.playListState)
            }
        }
    }

    private fun updateNotificationPlayPause(isPlaying: Boolean) {
        val builder = musicNotificationManager.updatePlaying(isPlaying)
        startForeground(MusicNotificationActions.NOTIFICATION_ID, builder.build())
    }

    private fun updateNotificationMusicItem(mediaItem: MediaItem?) {
        mediaItem?.let {
            val notification = musicNotificationManager.getNotification(
                context = this@MusicPlayerService,
                mediaItem = it.mediaMetadata
            )
            startForeground(MusicNotificationActions.NOTIFICATION_ID, notification.build())
        }
    }

    override fun updatePlayListState(state: PlayListState) {
        val builder = musicNotificationManager.updatePlayList(state)
        builder?.let {
            startForeground(MusicNotificationActions.NOTIFICATION_ID, builder.build())
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    override fun removeNotification() {
        GlobalScope.launch {
            saveData()
            stopForeground()
            stopSelf()
        }
    }
}


