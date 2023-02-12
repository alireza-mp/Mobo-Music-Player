package com.digimoplus.moboplayer.data.dataSource

import com.CodeBoy.MediaFacer.mediaHolders.audioContent

interface MusicsDataSource {

    // get all musics list
    suspend fun getAllMusicLists(): List<audioContent>?

    // get last music index in list
    suspend fun getLastMusicIndex(): Int
}