package com.digimoplus.moboplayer.data.dataSourceImpl

import com.digimoplus.moboplayer.data.dataSource.DataStoreDataSource
import com.digimoplus.moboplayer.data.dataSource.PlayListDataSource
import com.digimoplus.moboplayer.data.db.dao.PlayListDao
import com.digimoplus.moboplayer.data.db.model.PlayListEntity
import javax.inject.Inject

class PlayListLocalDataSourceImpl
@Inject
constructor(
    private val playListDao: PlayListDao,
    private val datastore: DataStoreDataSource,
) : PlayListDataSource {

    override suspend fun getAllPlayLists(): List<PlayListEntity>? {
        return try {
            playListDao.getAllPlayLists()
        } catch (e: Exception) {
            null
        }
    }

    override suspend fun addPlayList(playListEntity: PlayListEntity): Int {
        return playListDao.addPlayList(playListEntity).toInt()
    }

    override suspend fun getLastPlayListId(): Int {
        return datastore.getLastPlayListId()
    }

    override suspend fun deletePlayList(id: Int) {
        playListDao.deletePlayList(id)
    }

    override suspend fun updatePlayListTitle(playListId: Int, title: String) {
        playListDao.updatePlayListTitle(playListId, title)
    }


}