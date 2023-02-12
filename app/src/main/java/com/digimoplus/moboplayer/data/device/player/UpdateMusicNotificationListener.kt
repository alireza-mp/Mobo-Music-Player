package com.digimoplus.moboplayer.data.device.player

import com.digimoplus.moboplayer.util.PlayListState

interface UpdateMusicNotificationListener {

    fun updatePlayListState(state: PlayListState)
    fun removeNotification()

}