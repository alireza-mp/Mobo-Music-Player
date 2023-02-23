package com.digimoplus.moboplayer.domain.models

import androidx.compose.runtime.snapshots.SnapshotStateList
import com.digimoplus.moboplayer.data.db.model.PlayListEntity
import com.digimoplus.moboplayer.data.device.player.model.PlayerPlayList

data class PlayListItem(
    var id: Int,
    var title: String,
    val musics: SnapshotStateList<Music>,
)


fun PlayListItem.mapToEntityModel(): PlayListEntity {
    return PlayListEntity(
        id = this.id,
        title = this.title,
    )
}

fun PlayListItem.mapToMusicPlayerModel(): PlayerPlayList {
    return PlayerPlayList(
        this.id,
        this.title,
        this.musics.toList()
    )
}

