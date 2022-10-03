package com.digimoplus.moboplayer.device.player

import com.digimoplus.moboplayer.domain.models.Music

interface MusicPlayerUiListener {
    fun play()
    fun pause()
    fun updateUiByPlayerState(
        percentage: Float,
        duration: String,
        isLoop: Boolean,
        isShuffle: Boolean,
    )

    fun updateCurrentMusic(music: Music)
}