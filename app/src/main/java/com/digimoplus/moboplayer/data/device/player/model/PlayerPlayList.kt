package com.digimoplus.moboplayer.data.device.player.model

import androidx.compose.runtime.toMutableStateList
import com.digimoplus.moboplayer.domain.models.Music
import com.digimoplus.moboplayer.domain.models.PlayListItem

data class PlayerPlayList(
    var id: Int,
    val title: String,
    val musics: List<Music>,
)

fun PlayerPlayList.mapToDomainModel(): PlayListItem {
    return PlayListItem(
        this.id,
        this.title,
        this.musics.toMutableStateList()
    )
}
