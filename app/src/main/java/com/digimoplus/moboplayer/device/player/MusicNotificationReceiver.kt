package com.digimoplus.moboplayer.device.player

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.digimoplus.moboplayer.util.PlayListState
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MusicNotificationReceiver : BroadcastReceiver() {

    @Inject
    lateinit var musicPlayer: MusicPlayer

    // receive notification buttons click
    override fun onReceive(context: Context?, intent: Intent?) {

        if (intent != null) {
            when (intent.action) {
                MusicNotificationActions.ACTION_PLAY -> {
                    musicPlayer.play()
                }
                MusicNotificationActions.ACTION_PAUSE -> {
                    musicPlayer.pause()
                }
                MusicNotificationActions.ACTION_NEXT -> {
                    musicPlayer.onNext()
                }
                MusicNotificationActions.ACTION_PREVIOUS -> {
                    musicPlayer.onPrevious()
                }
                MusicNotificationActions.ACTION_CURRENT_PLAYLIST -> {
                    musicPlayer.updatePlayListState(PlayListState.SHUFFLE)
                }
                MusicNotificationActions.ACTION_SHUFFLE -> {
                    musicPlayer.updatePlayListState(PlayListState.LOOP)
                }
                MusicNotificationActions.ACTION_LOOP -> {
                    musicPlayer.updatePlayListState(PlayListState.CURRENT)
                }
                MusicNotificationActions.ACTION_REMOVE -> {
                    musicPlayer.removeNotification()
                }
            }
        }
    }
}