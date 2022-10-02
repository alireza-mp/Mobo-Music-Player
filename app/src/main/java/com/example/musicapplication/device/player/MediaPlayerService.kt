package com.example.musicapplication.device.player

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.graphics.Bitmap
import android.os.Binder
import android.os.IBinder
import androidx.compose.material.ExperimentalMaterialApi
import androidx.core.app.NotificationCompat
import com.example.musicapplication.R
import com.example.musicapplication.presentation.ui.mainactivity.MainActivity
import com.google.android.exoplayer2.C
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.audio.AudioAttributes
import com.google.android.exoplayer2.ui.PlayerNotificationManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@OptIn(ExperimentalMaterialApi::class)
@AndroidEntryPoint
class MediaPlayerService : Service() {


    @Inject
    lateinit var exoPlayer: ExoPlayer


    private val serviceBinder: IBinder = ServiceBinder()

    inner class ServiceBinder : Binder() {
        fun getMediaPlayerService(): MediaPlayerService {
            return this@MediaPlayerService
        }
    }

    override fun onBind(intent: Intent): IBinder {
        return serviceBinder
    }


    val chanelId = "Music Chanel"
    val notificationId = 1111111
    private lateinit var mediaDescription: PlayerNotificationManager.MediaDescriptionAdapter
    private lateinit var notificationListner: PlayerNotificationManager.NotificationListener
    var playerNotificationManager: PlayerNotificationManager? = null

    override fun onCreate() {
        super.onCreate()

        mediaDescription = createMedaiDesc()
        notificationListner = crateListen()

        // ss
        val audioAttributes = AudioAttributes.Builder()
            .setUsage(C.USAGE_MEDIA)
            .setContentType(C.AUDIO_CONTENT_TYPE_MUSIC)
            .build()

        exoPlayer.setAudioAttributes(audioAttributes, true)
        exoPlayer.repeatMode = Player.REPEAT_MODE_ALL

        createPNotif()


    }


    private fun createMedaiDesc(): PlayerNotificationManager.MediaDescriptionAdapter {
        return object : PlayerNotificationManager.MediaDescriptionAdapter {
            override fun getCurrentContentTitle(player: Player): CharSequence {
                return player.currentMediaItem?.mediaMetadata?.title ?: "null"
            }

            override fun createCurrentContentIntent(player: Player): PendingIntent? {
                // intentn to open app
                val intent = Intent(this@MediaPlayerService, MainActivity::class.java)

                return PendingIntent.getActivity(applicationContext,
                    0,
                    intent,
                    PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT)
            }

            override fun getCurrentContentText(player: Player): CharSequence {
                return "hihihi"
            }

            override fun getCurrentLargeIcon(
                player: Player,
                callback: PlayerNotificationManager.BitmapCallback,
            ): Bitmap? {
                /*  val view = ImageView(applicationContext)
                  view.setImageURI(player.currentMediaItem?.mediaMetadata?.artworkUri)
                  val biti: BitmapDrawable = view.drawable as BitmapDrawable
                  *//*biti ?: ContextCompat.getDrawable(applicationContext,
                R.drawable.ic_launcher_background)*//*

            return biti.bitmap*/

                /*val bitmap = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    ImageDecoder.decodeBitmap(ImageDecoder.createSource(requireContext().contentResolver, imageUri))
                } else {
                    MediaStore.Images.Media.getBitmap(requireContext().contentResolver, imageUri)
                }*/

                return null
            }

        }
    }

    private fun crateListen(): PlayerNotificationManager.NotificationListener {
        return object : PlayerNotificationManager.NotificationListener {

            override fun onNotificationPosted(
                notificationId: Int,
                notification: Notification,
                ongoing: Boolean,
            ) {
                super.onNotificationPosted(notificationId, notification, ongoing)
                startForeground(notificationId, notification)
            }

        }
    }


    private fun createPNotif() {

        playerNotificationManager =
            PlayerNotificationManager.Builder(applicationContext, notificationId, chanelId)
                .setNotificationListener(notificationListner)
                .setMediaDescriptionAdapter(mediaDescription)
                .setChannelImportance(NotificationManager.IMPORTANCE_HIGH)
                .setSmallIconResourceId(R.drawable.ic_launcher_foreground)
                .setNextActionIconResourceId(R.drawable.ic_launcher_foreground)
                .setPreviousActionIconResourceId(R.drawable.ic_launcher_foreground)
                .setPlayActionIconResourceId(R.drawable.ic_launcher_foreground)
                .setPauseActionIconResourceId(R.drawable.ic_launcher_foreground)
                .setChannelNameResourceId(R.string.app_name)
                .build()


        playerNotificationManager?.setPlayer(exoPlayer)
        playerNotificationManager?.setPriority(NotificationCompat.PRIORITY_MAX)
        playerNotificationManager?.setUseRewindAction(false)
        playerNotificationManager?.setUseFastForwardAction(false)

    }

    private fun initialPlayerListener() {
        exoPlayer.addListener(object : Player.Listener {

            override fun onIsPlayingChanged(isPlaying: Boolean) {
                super.onIsPlayingChanged(isPlaying)
                serviceMediaListener?.onIsPlayingChanged(isPlaying)
            }

            override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
                super.onMediaItemTransition(mediaItem, reason)
                serviceMediaListener?.onMediaItemTransition(mediaItem)
            }

        })
    }

    override fun onDestroy() {
        //if (exoPlayer.isPlaying) exoPlayer.stop()
        playerNotificationManager?.setPlayer(null)
        exoPlayer.release()
        // player = null
        //stopForeground(true)
        stopSelf()
        super.onDestroy()
    }


}




