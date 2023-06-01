package com.digimoplus.moboplayer.data.device.player.notification


import com.digimoplus.moboplayer.util.PlayListState

data class MusicNotificationStateHolder(
    val isPlaying: Boolean = false,
    val position: Long = 0L,
    val playListState: PlayListState = PlayListState.CURRENT,
)
