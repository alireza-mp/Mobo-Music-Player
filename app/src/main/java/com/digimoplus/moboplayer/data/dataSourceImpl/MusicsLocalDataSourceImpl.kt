package com.digimoplus.moboplayer.data.dataSourceImpl

import com.CodeBoy.MediaFacer.AudioGet
import com.CodeBoy.MediaFacer.mediaHolders.audioContent
import com.digimoplus.moboplayer.data.dataSource.DataStoreDataSource
import com.digimoplus.moboplayer.data.dataSource.MusicsDataSource
import javax.inject.Inject

class MusicsLocalDataSourceImpl
@Inject constructor(
    private val audioGet: AudioGet,
    private val dataStoreDataSource: DataStoreDataSource,
) : MusicsDataSource {

    override suspend fun getAllMusicLists(): List<audioContent>? {
        return audioGet.getAllAudioContent(AudioGet.externalContentUri)
    }

    override suspend fun getLastMusicIndex(): Int {
        // search for last music
        val result = audioGet.searchMusic(dataStoreDataSource.getLastMusicTitle())
        // if founded
        return if (result.size != 0) {
            var index = -1
            // found music index in list
            val list = audioGet.getAllAudioContent(AudioGet.externalContentUri)
            for (i in 0 until list.size) {
                if (list[i].musicID == result[0].musicID) {
                    index = i
                    break
                }
            }
            index
        } else -1 // not founded  // set as default index 0
    }

}