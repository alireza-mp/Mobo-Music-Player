package com.example.musicapplication.device.player

import com.example.musicapplication.domain.models.Music

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