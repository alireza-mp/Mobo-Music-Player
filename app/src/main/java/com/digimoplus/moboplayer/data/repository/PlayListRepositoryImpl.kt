package com.digimoplus.moboplayer.data.repository

import androidx.compose.runtime.toMutableStateList
import com.digimoplus.moboplayer.data.dataSource.MusicsDataSource
import com.digimoplus.moboplayer.data.dataSource.PlayListDataSource
import com.digimoplus.moboplayer.data.db.model.MusicEntity
import com.digimoplus.moboplayer.data.db.model.mapToDomainModel
import com.digimoplus.moboplayer.domain.models.Music
import com.digimoplus.moboplayer.domain.models.PlayListItem
import com.digimoplus.moboplayer.domain.models.mapToDomainModel
import com.digimoplus.moboplayer.domain.models.mapToEntityModel
import com.digimoplus.moboplayer.domain.repostiry.PlayListRepository
import com.digimoplus.moboplayer.util.DataState
import com.digimoplus.moboplayer.util.sortByDate
import javax.inject.Inject

class PlayListRepositoryImpl
@Inject
constructor(
    private val playListDataSource: PlayListDataSource,
    private val musicDataSource: MusicsDataSource,
) : PlayListRepository {


    override suspend fun getPlayLists(): DataState<List<PlayListItem>> {
        val playListEntityList = playListDataSource.getAllPlayLists()
        val musicEntityList = musicDataSource.getPlayListMusics()
        val audioList = musicDataSource.getAllMusicList()
        val playListItemList = mutableListOf<PlayListItem>()

        return if (playListEntityList != null && audioList != null && musicEntityList != null) {

            val musicList = audioList.map { it.mapToDomainModel() }.toMutableList()

            musicList.sortByDate()

            playListItemList.add(
                PlayListItem(
                    id = -1,
                    title = "All",
                    musics = musicList.toMutableStateList()
                )
            ) // add allPlayListItem to list

            val dataBaseList = playListEntityList.map { item ->
                item.mapToDomainModel(
                    musicList = filterMusics(
                        pid = item.id,
                        musicList = musicList.toList(),
                        entityList = musicEntityList,
                    )
                )
            }
            playListItemList.addAll(dataBaseList) // add playlists from database

            playListItemList.add(
                PlayListItem(
                    id = -2,
                    title = "+ Add New PlayList",
                    musics = musicList.toMutableStateList(),
                )
            ) // add addNewPlayListItem to list


            DataState.Success(playListItemList)

        } else DataState.Error
    }

    override suspend fun addPlayList(playListItem: PlayListItem): Int {
        val pid = playListDataSource.addPlayList(playListItem.mapToEntityModel())
        val musicEntityList = playListItem.musics.map { it.mapToEntityModel(pid) }
        musicDataSource.addMusics(musicEntityList)
        return pid
    }

    override suspend fun deletePlayList(playListItem: PlayListItem) {
        val playlist = playListItem.mapToEntityModel()
        playListDataSource.deletePlayList(playlist.id)
        musicDataSource.deleteMusicsByPId(playlist.id)
    }

    override suspend fun updatePlayList(playListItem: PlayListItem) {
        val playlist = playListItem.mapToEntityModel()
        val musics = playListItem.musics.map { it.mapToEntityModel(playListItem.id) }
        playListDataSource.updatePlayListTitle(playlist.id, playlist.title)
        musicDataSource.deleteMusicsByPId(playlist.id)
        musicDataSource.addMusics(musics)
    }


    private fun filterMusics(
        pid: Int, // play list id
        musicList: List<Music>, // all music list
        entityList: List<MusicEntity>, // entity all music list
    ): List<Music> {
        val list = mutableListOf<Music>()
        entityList.forEach { e ->
            if (pid == e.pId) { // filter entityList by pid
                musicList.forEach { m ->
                    if (m.title == e.title) { // find and add music from musicList
                        list.add(m)
                    }
                }
            }
        }
        return list
    }

}







