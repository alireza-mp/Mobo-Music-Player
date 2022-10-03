package com.digimoplus.moboplayer.data.repository.dataSource

import com.CodeBoy.MediaFacer.mediaHolders.audioContent

interface MusicsLocalDataSource {

    // get all musics list
    suspend fun getAllMusicLists(): List<audioContent>?

    // get last music index in list
    suspend fun getLastMusicIndex(): Int
}