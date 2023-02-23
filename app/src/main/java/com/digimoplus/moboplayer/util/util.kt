package com.digimoplus.moboplayer.util

import android.icu.text.SimpleDateFormat
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.digimoplus.moboplayer.domain.models.Music
import com.digimoplus.moboplayer.domain.models.PlayListItem
import java.io.File
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

fun SnapshotStateList<PlayListItem>.sortMusics(sortState: Sort) {
    this.forEach { p ->
        when (sortState) {
            Sort.NAME -> {
                p.musics.sortBy { it.title.lowercase() }
            }
            Sort.Artist -> {
                p.musics.sortBy { it.artist.lowercase() }
            }
            Sort.DATE -> {
                val dateTimeFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
                p.musics.sortByDescending { LocalDate.parse(it.dateModified, dateTimeFormatter) }
            }
        }
    }
}

fun MutableList<Music>.sortByDate() {
    val dateTimeFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
    this.sortByDescending { LocalDate.parse(it.dateModified, dateTimeFormatter) }
}

fun getFileLastModified(filePath: String): String {
    val sdf = SimpleDateFormat("dd-MM-yyyy", Locale.US)
    return try {
        sdf.format(File(filePath).lastModified())
    } catch (e: Exception) {
        "00"
    }
}

fun SnapshotStateList<PlayListItem>.findItemById(item: PlayListItem): Int {
    return this.withIndex().find { it.value.id == item.id }?.index ?: 0
}
