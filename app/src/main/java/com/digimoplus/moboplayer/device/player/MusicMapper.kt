package com.digimoplus.moboplayer.device.player

import com.digimoplus.moboplayer.domain.models.Music
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.MediaMetadata

fun musicToMediaList(musicList: List<Music>): List<MediaItem> {

    val mediaList = mutableListOf<MediaItem>()
    musicList.forEach {
        mediaList.add(MediaItem.Builder()
            .setMediaId(it.id.toString())
            .setUri(it.musicUri)
            .setMediaMetadata(getMetadata(it))
            .build())
    }
    return mediaList
}

private fun getMetadata(music: Music): MediaMetadata {
    return MediaMetadata.Builder()
        .setTitle(music.title)
        .setDescription(music.duration)
        .setArtist(music.artist)
        .setArtworkUri(music.imageUri)
        .build()
}

fun mediaItemToMusic(model: MediaItem?): Music {
    return Music(
        id = model?.mediaId?.toLong() ?: -1,
        artist = model?.mediaMetadata?.artist.toString(),
        title = model?.mediaMetadata?.title.toString(),
        duration = model?.mediaMetadata?.description.toString(),
        imageUri = model?.mediaMetadata?.artworkUri,
    )
}