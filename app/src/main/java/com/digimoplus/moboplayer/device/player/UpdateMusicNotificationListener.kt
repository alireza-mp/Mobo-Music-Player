package com.digimoplus.moboplayer.device.player

import com.digimoplus.moboplayer.util.PlayListState

interface UpdateMusicNotificationListener {

    fun updatePlayListState(state: PlayListState)
    fun removeNotification()

}