@file:OptIn(ExperimentalMaterialApi::class, DelicateCoroutinesApi::class)

package com.digimoplus.moboplayer.device.player


import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.media.MediaMetadata
import android.os.Build
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import androidx.annotation.RequiresApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.core.app.NotificationCompat
import androidx.media.session.MediaButtonReceiver
import com.digimoplus.moboplayer.R
import com.digimoplus.moboplayer.presentation.ui.mainactivity.MainActivity
import com.digimoplus.moboplayer.util.PlayListState
import com.digimoplus.moboplayer.util.convertMinuteToMilliSeconds
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject


class MusicNotificationManager
@Inject
constructor(
    private val musicPlayer: MusicPlayer,
    private val context: Context,
) {


    private var notificationBuilder: NotificationCompat.Builder? = null
    private lateinit var mediaSession: MediaSessionCompat


    private val playIntent: Intent = Intent(context, MusicNotificationReceiver::class.java)
        .setAction(MusicNotificationActions.ACTION_PLAY)

    private val playPendingIntent = PendingIntent.getBroadcast(
        context,
        MusicNotificationActions.NOTIFICATION_ID,
        playIntent,
        PendingIntent.FLAG_IMMUTABLE
    )

    private val pauseIntent: Intent = Intent(context, MusicNotificationReceiver::class.java)
        .setAction(MusicNotificationActions.ACTION_PAUSE)

    private val pausePendingIntent = PendingIntent.getBroadcast(
        context,
        MusicNotificationActions.NOTIFICATION_ID,
        pauseIntent,
        PendingIntent.FLAG_IMMUTABLE
    )

    private val previousIntent: Intent = Intent(context, MusicNotificationReceiver::class.java)
        .setAction(MusicNotificationActions.ACTION_PREVIOUS)

    private val previousPendingIntent = PendingIntent.getBroadcast(
        context,
        MusicNotificationActions.NOTIFICATION_ID,
        previousIntent,
        PendingIntent.FLAG_IMMUTABLE
    )

    private val nextIntent: Intent = Intent(context, MusicNotificationReceiver::class.java)
        .setAction(MusicNotificationActions.ACTION_NEXT)

    private val nextPendingIntent = PendingIntent.getBroadcast(
        context,
        MusicNotificationActions.NOTIFICATION_ID,
        nextIntent,
        PendingIntent.FLAG_IMMUTABLE
    )

    // playlist state
    private val shuffleIntent: Intent = Intent(context, MusicNotificationReceiver::class.java)
        .setAction(MusicNotificationActions.ACTION_SHUFFLE)
    private val shufflePendingIntent = PendingIntent.getBroadcast(
        context,
        MusicNotificationActions.NOTIFICATION_ID,
        shuffleIntent,
        PendingIntent.FLAG_IMMUTABLE
    )

    // playlist state
    private val loopIntent: Intent = Intent(context, MusicNotificationReceiver::class.java)
        .setAction(MusicNotificationActions.ACTION_LOOP)
    private val loopPendingIntent = PendingIntent.getBroadcast(
        context,
        MusicNotificationActions.NOTIFICATION_ID,
        loopIntent,
        PendingIntent.FLAG_IMMUTABLE
    )

    // playlist state
    private val currentListIntent: Intent = Intent(context, MusicNotificationReceiver::class.java)
        .setAction(MusicNotificationActions.ACTION_CURRENT_PLAYLIST)
    private val currentListPendingIntent = PendingIntent.getBroadcast(
        context,
        MusicNotificationActions.NOTIFICATION_ID,
        currentListIntent,
        PendingIntent.FLAG_IMMUTABLE
    )

    private val removeIntent: Intent = Intent(context, MusicNotificationReceiver::class.java)
        .setAction(MusicNotificationActions.ACTION_REMOVE)

    private val removePendingIntent = PendingIntent.getBroadcast(
        context,
        MusicNotificationActions.NOTIFICATION_ID,
        removeIntent,
        PendingIntent.FLAG_IMMUTABLE
    )

    private val contentIntent = Intent(context, MainActivity::class.java)
    private val contentPendingIntent =
        PendingIntent.getActivity(context, 1, contentIntent, PendingIntent.FLAG_IMMUTABLE)

    private val pauseAction =
        NotificationCompat.Action.Builder(R.drawable.ic_pause, "pause", pausePendingIntent).build()
    private val playAction =
        NotificationCompat.Action.Builder(R.drawable.ic_play, "play", playPendingIntent).build()
    private val loopAction =
        NotificationCompat.Action.Builder(
            R.drawable.ic_repeat_notification,
            "loop",
            loopPendingIntent
        ).build()
    private val shuffleAction =
        NotificationCompat.Action.Builder(
            R.drawable.ic_shuffle_notification,
            "shuffle",
            shufflePendingIntent
        )
            .build()
    private val currentListAction = NotificationCompat.Action.Builder(
        R.drawable.ic_current_notification,
        "current",
        currentListPendingIntent
    ).build()

    fun getNotification(
        context: Context,
        mediaItem: com.google.android.exoplayer2.MediaMetadata,
    ): NotificationCompat.Builder {

        if (notificationBuilder == null) {
            initNotification(context, mediaItem)
        } else {
            updateNotificationItem(mediaItem)
        }

        return notificationBuilder!!
    }


    private fun initNotification(
        context: Context,
        mediaItem: com.google.android.exoplayer2.MediaMetadata,
    ) {
        initMediaSession(context)
        updateSeekBarPosition()
        mediaSession.setMetadata(createNotificationMetadata(mediaItem))
        mediaSession.setPlaybackState(
            PlaybackStateCompat.Builder()
                .setState(
                    PlaybackStateCompat.STATE_PAUSED,
                    0,
                    0f
                )
                .setActions(PlaybackStateCompat.ACTION_SEEK_TO)
                .build()
        )

        mediaSession.setCallback(object : MediaSessionCompat.Callback() {
            override fun onSeekTo(pos: Long) {
                super.onSeekTo(pos)
                musicPlayer.seekTo(pos)
            }
        })

        val mediaStyle =
            androidx.media.app.NotificationCompat.MediaStyle()
                .setMediaSession(mediaSession.sessionToken)

        notificationBuilder = NotificationCompat.Builder(
            context,
            MusicNotificationActions.NOTIFICATION_CHANEL_ID
        )
            .setStyle(mediaStyle)
            .setSmallIcon(R.drawable.ic_logo)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setOnlyAlertOnce(true)
            .setContentIntent(contentPendingIntent)
            .addAction(currentListAction)
            .addAction(R.drawable.ic_notification_previous, "previous", previousPendingIntent)
            .addAction(playAction)
            .addAction(R.drawable.ic_notification_next, "next", nextPendingIntent)
            .addAction(
                com.ehsanmsz.mszprogressindicator.R.drawable.ic_m3_chip_close,
                "close",
                removePendingIntent
            )
    }

    @SuppressLint("RestrictedApi")
    fun updatePlaying(isPlay: Boolean): NotificationCompat.Builder {
        notificationBuilder?.mActions?.set(2, if (isPlay) pauseAction else playAction)
        return notificationBuilder!!
    }

    @SuppressLint("RestrictedApi")
    fun updatePlayList(playListState: PlayListState): NotificationCompat.Builder {
        when (playListState) {
            PlayListState.CURRENT -> {
                notificationBuilder?.mActions?.set(0, currentListAction)
            }
            PlayListState.SHUFFLE -> {
                notificationBuilder?.mActions?.set(0, shuffleAction)
            }
            PlayListState.LOOP -> {
                notificationBuilder?.mActions?.set(0, loopAction)
            }
        }
        return notificationBuilder!!
    }

    private fun updateNotificationItem(
        mediaItem: com.google.android.exoplayer2.MediaMetadata,
    ) {

        mediaSession.setMetadata(createNotificationMetadata(mediaItem))

        mediaSession.setPlaybackState(
            PlaybackStateCompat.Builder()
                .setState(
                    PlaybackStateCompat.STATE_PAUSED,
                    0,
                    0f
                )
                .setActions(PlaybackStateCompat.ACTION_SEEK_TO)
                .build()
        )


    }

    private fun updateSeekBar(
        position: Long,
    ) {
        mediaSession.setPlaybackState(
            PlaybackStateCompat.Builder()
                .setState(
                    PlaybackStateCompat.STATE_PLAYING,
                    position,
                    0f
                )
                .setActions(PlaybackStateCompat.ACTION_SEEK_TO)
                .build()
        )
    }


    private fun initMediaSession(
        context: Context,
    ) {
        val mediaButtonReceiver = ComponentName(
            context,
            MediaButtonReceiver::class.java
        )
        mediaSession = MediaSessionCompat(
            context,
            "MyMediaSession",
            mediaButtonReceiver,
            null,
        )
    }

    fun createNotificationChanel(context: Context) {
        // Create the NotificationChannel
        val mChannel = NotificationChannel(
            MusicNotificationActions.NOTIFICATION_CHANEL_ID,
            MusicNotificationActions.NOTIFICATION_CHANEL_NAME,
            NotificationManager.IMPORTANCE_HIGH
        )
        val notificationManager =
            context.getSystemService(Service.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(mChannel)
    }


    private fun updateSeekBarPosition() {
        GlobalScope.launch(Dispatchers.Main) {
            musicPlayer.getCurrentPositionFlow().collect { currentPosition ->
                updateSeekBar(currentPosition)
            }
        }
    }

    private fun createNotificationMetadata(
        mediaItem: com.google.android.exoplayer2.MediaMetadata,
    ): MediaMetadataCompat {
        return MediaMetadataCompat.Builder()
            .putString(MediaMetadata.METADATA_KEY_TITLE, mediaItem.title.toString())
            .putString(MediaMetadata.METADATA_KEY_ARTIST, mediaItem.artist.toString())
            .putString(
                MediaMetadata.METADATA_KEY_ALBUM_ART_URI,
                mediaItem.artworkUri.toString()
            )
            .putLong(
                MediaMetadata.METADATA_KEY_DURATION,
                convertMinuteToMilliSeconds(mediaItem.description.toString())
            )
            .build()
    }

}