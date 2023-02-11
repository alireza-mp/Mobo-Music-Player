package com.digimoplus.moboplayer.device.player

import com.google.android.exoplayer2.MediaItem

interface MusicServiceChangeListener {
    fun onIsPlayingChanged(isPlaying: Boolean)
    fun onMediaItemTransition(mediaItem: MediaItem?)
}