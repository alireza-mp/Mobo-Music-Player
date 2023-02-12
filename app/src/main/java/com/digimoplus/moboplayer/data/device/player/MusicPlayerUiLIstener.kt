package com.digimoplus.moboplayer.data.device.player

import com.digimoplus.moboplayer.domain.models.Music
import com.digimoplus.moboplayer.util.PlayListState

interface MusicPlayerUiListener {
    fun play()
    fun pause()
    fun updateUiByPlayerState(
        percentage: Float,
        duration: String,
        playListState: PlayListState,
    )

    fun updatePlayListState(playListState: PlayListState)
    fun updateCurrentMusic(music: Music)
    fun resetPercentageAndDuration()
}