package com.digimoplus.moboplayer.domain.models

data class HomeViewState(
    val lastDataStore: LastDataStore,
    val musicList: List<Music>,
)