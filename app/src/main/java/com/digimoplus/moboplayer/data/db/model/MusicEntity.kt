package com.digimoplus.moboplayer.data.db.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "music_tb")
data class MusicEntity(

    @PrimaryKey(autoGenerate = true)
    var id: Int,

    @ColumnInfo(name = "pid")
    var pId: Int,

    @ColumnInfo(name = "title")
    var title: String,

    )