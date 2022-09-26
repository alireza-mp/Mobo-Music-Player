package com.example.musicapplication.domain.models

data class LastDataStore(
    val duration: String,
    val percentage: Float,
    val lastMusicIndex: Int, // false = index and playing next // true play ing next random
    val isShuffle: Boolean,
    val isLoop: Boolean,
)