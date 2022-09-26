package com.example.musicapplication.domain.models

data class HomeViewState(
    val lastDataStore: LastDataStore,
    val musicList: List<Music>,
)