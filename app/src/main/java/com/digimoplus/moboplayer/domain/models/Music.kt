package com.digimoplus.moboplayer.domain.models

import android.net.Uri
import com.CodeBoy.MediaFacer.mediaHolders.audioContent
import com.digimoplus.moboplayer.data.db.model.MusicEntity
import com.digimoplus.moboplayer.util.convertMilliSecondsToSecond
import com.digimoplus.moboplayer.util.getFileLastModified

data class Music(
    var id: Long = -1,
    val artist: String = "null",
    val title: String = "null",
    val path: String = "null",
    var duration: String = "null",
    val imageUri: Uri? = null,
    val musicUri: String? = null,
    val dateModified: String = "",
    var isChecked: Boolean = false,
)

fun audioContent.mapToDomainModel(): Music {
    return Music(
        id = this.musicID,
        artist = this.artist,
        title = this.title,
        path = this.filePath,
        duration = convertMilliSecondsToSecond(this.duration),
        musicUri = this.assetFileStringUri,
        imageUri = this.art_uri,
        dateModified = getFileLastModified(this.filePath),
    )
}

fun Music.mapToEntityModel(pId: Int): MusicEntity {
    return MusicEntity(
        id = 0,
        pId = pId,
        title = this.title,
    )
}
