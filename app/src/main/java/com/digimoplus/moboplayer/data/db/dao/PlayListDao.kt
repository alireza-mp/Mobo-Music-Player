package com.digimoplus.moboplayer.data.db.dao

import androidx.room.*
import com.digimoplus.moboplayer.data.db.model.PlayListEntity

@Dao
interface PlayListDao {


    @Insert
    suspend fun addPlayList(playListEntity: PlayListEntity): Long

    @Query("DELETE FROM playlist_tb WHERE id = :id")
    suspend fun deletePlayList(id: Int)

    @Query("UPDATE playlist_tb SET title = :title WHERE id = :playListId")
    suspend fun updatePlayListTitle(playListId: Int, title: String)

    @Query("SELECT * FROM playlist_tb")
    suspend fun getAllPlayLists(): List<PlayListEntity>


}