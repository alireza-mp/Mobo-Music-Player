package com.digimoplus.moboplayer.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.digimoplus.moboplayer.data.db.model.MusicEntity

@Dao
interface MusicDao {

    @Insert
    suspend fun addMusicList(musics: List<MusicEntity>)

    @Query("DELETE FROM music_tb WHERE pid = :pid")
    suspend fun deleteMusicsByPId(pid: Int)

    @Query("SELECT * FROM music_tb ")
    suspend fun getAllMusics(): List<MusicEntity>

}