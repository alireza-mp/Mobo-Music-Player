package com.digimoplus.moboplayer.data.dataSource

import com.CodeBoy.MediaFacer.mediaHolders.audioContent
import com.digimoplus.moboplayer.data.db.model.MusicEntity

interface MusicsDataSource {

    suspend fun getAllMusicList(): List<audioContent>?

    suspend fun getLastMusicId(): Int

    suspend fun addMusics(musicList: List<MusicEntity>)

    suspend fun getPlayListMusics(): List<MusicEntity>?

    suspend fun deleteMusicsByPId(pid: Int)
}