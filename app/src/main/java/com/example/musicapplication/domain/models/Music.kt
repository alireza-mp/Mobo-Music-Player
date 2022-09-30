package com.example.musicapplication.domain.models

import android.net.Uri
import com.CodeBoy.MediaFacer.mediaHolders.audioContent
import com.example.musicapplication.util.convertMilliSecondsToSecond

data class Music(
    var id: Long = -1,
    val artist: String = "null",
    val title: String = "null",
    val path: String = "null",
    var duration: String = "null",
    val imageUri: Uri? = null,
    val musicUri: String? = null,
    val isPlayed: Boolean = false,
)

suspend fun mapToDomainModel(model: audioContent): Music {
    return Music(
        id = model.musicID,
        artist = model.artist,
        title = model.title,
        path = model.filePath,
        duration = convertMilliSecondsToSecond(model.duration),
        musicUri = model.assetFileStringUri,
        imageUri = model.art_uri
    )
}



