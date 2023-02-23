package com.digimoplus.moboplayer.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.digimoplus.moboplayer.data.db.dao.MusicDao
import com.digimoplus.moboplayer.data.db.dao.PlayListDao
import com.digimoplus.moboplayer.data.db.model.MusicEntity
import com.digimoplus.moboplayer.data.db.model.PlayListEntity


@Database(entities = [PlayListEntity::class, MusicEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun getPlayListDao(): PlayListDao

    abstract fun getMusicDao(): MusicDao

}