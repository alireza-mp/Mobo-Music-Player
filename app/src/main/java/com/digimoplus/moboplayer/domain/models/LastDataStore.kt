package com.digimoplus.moboplayer.domain.models

import com.digimoplus.moboplayer.util.PlayListState

data class LastDataStore(
    val currentPosition: Long,
    val duration: Long,
    val lastMusicId: Int,
    val playListState: PlayListState,
    val lastPlayListId: Int,
)