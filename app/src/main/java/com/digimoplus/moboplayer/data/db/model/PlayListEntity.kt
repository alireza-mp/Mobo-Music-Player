package com.digimoplus.moboplayer.data.db.model

import androidx.compose.runtime.toMutableStateList
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.digimoplus.moboplayer.domain.models.Music
import com.digimoplus.moboplayer.domain.models.PlayListItem

@Entity(tableName = "playlist_tb")
data class PlayListEntity(

    @PrimaryKey(autoGenerate = true)
    var id: Int,

    @ColumnInfo(name = "title")
    var title: String,

    )


fun PlayListEntity.mapToDomainModel(musicList: List<Music>): PlayListItem {
    return PlayListItem(
        id = this.id,
        title = this.title,
        musics = musicList.toMutableStateList(),
    )
}