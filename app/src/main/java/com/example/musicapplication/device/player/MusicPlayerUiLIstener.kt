package com.example.musicapplication.device.player

import com.example.musicapplication.domain.models.Music

interface MusicPlayerUiListener {
    fun play()
    fun pause()
    fun updatePAndD(percentage: Float, duration: String)
    fun updateCurrentMusic(music: Music)
    fun updateAutoNext(state: Boolean)
}