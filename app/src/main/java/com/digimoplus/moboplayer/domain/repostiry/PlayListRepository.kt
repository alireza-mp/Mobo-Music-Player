package com.digimoplus.moboplayer.domain.repostiry

import com.digimoplus.moboplayer.domain.models.PlayListItem
import com.digimoplus.moboplayer.util.DataState

interface PlayListRepository {

    suspend fun getPlayLists(): DataState<List<PlayListItem>>

    suspend fun addPlayList(playListItem: PlayListItem): Int

    suspend fun deletePlayList(playListItem: PlayListItem)

    suspend fun updatePlayList(playListItem: PlayListItem)

}