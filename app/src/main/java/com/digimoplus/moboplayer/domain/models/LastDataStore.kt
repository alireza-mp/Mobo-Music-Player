package com.digimoplus.moboplayer.domain.models

import com.digimoplus.moboplayer.util.PlayListState

data class LastDataStore(
    val currentPosition: Long,
    val duration: Long,
    val lastMusicIndex: Int, // false = index and playing next // true play ing next random
    val playListState: PlayListState,
)