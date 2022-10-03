package com.digimoplus.moboplayer.domain.models

data class LastDataStore(
    val currentPosition: Long,
    val duration: Long,
    val lastMusicIndex: Int, // false = index and playing next // true play ing next random
    val isShuffle: Boolean,
    val isLoop: Boolean,
)