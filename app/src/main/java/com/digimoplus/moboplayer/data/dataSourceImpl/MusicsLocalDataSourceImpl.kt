package com.digimoplus.moboplayer.data.dataSourceImpl

import com.CodeBoy.MediaFacer.AudioGet
import com.CodeBoy.MediaFacer.mediaHolders.audioContent
import com.digimoplus.moboplayer.data.dataSource.DataStoreDataSource
import com.digimoplus.moboplayer.data.dataSource.MusicsDataSource
import com.digimoplus.moboplayer.data.db.dao.MusicDao
import com.digimoplus.moboplayer.data.db.model.MusicEntity
import javax.inject.Inject

class MusicsLocalDataSourceImpl
@Inject constructor(
    private val audioGet: AudioGet,
    private val dataStoreDataSource: DataStoreDataSource,
    private val musicDao: MusicDao,
) : MusicsDataSource {

    override suspend fun getAllMusicList(): List<audioContent>? {
        return audioGet.getAllAudioContent(AudioGet.externalContentUri)
    }

    override suspend fun getLastMusicId(): Int {
        return dataStoreDataSource.getLastMusicId()
    }

    override suspend fun addMusics(musicList: List<MusicEntity>) {
        musicDao.addMusicList(musicList)

    }

    override suspend fun getPlayListMusics(): List<MusicEntity>? {
        return try {
            musicDao.getAllMusics()
        } catch (e: Exception) {
            null
        }
    }

    override suspend fun deleteMusicsByPId(pid: Int) {
        musicDao.deleteMusicsByPId(pid)
    }

}