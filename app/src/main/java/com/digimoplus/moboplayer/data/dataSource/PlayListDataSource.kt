package com.digimoplus.moboplayer.data.dataSource

import com.digimoplus.moboplayer.data.db.model.PlayListEntity

interface PlayListDataSource {

    suspend fun getAllPlayLists(): List<PlayListEntity>?

    suspend fun addPlayList(playListEntity: PlayListEntity): Int

    suspend fun getLastPlayListId(): Int

    suspend fun deletePlayList(id: Int)

    suspend fun updatePlayListTitle(playListId: Int, title: String)

}