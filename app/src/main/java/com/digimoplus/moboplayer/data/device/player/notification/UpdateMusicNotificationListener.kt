package com.digimoplus.moboplayer.data.device.player.notification

import com.digimoplus.moboplayer.util.PlayListState

interface UpdateMusicNotificationListener {

    fun updatePlayListState(state: PlayListState)
    fun removeNotification()

}