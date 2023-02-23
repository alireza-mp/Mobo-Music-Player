package com.digimoplus.moboplayer.domain.useCase

import com.digimoplus.moboplayer.domain.models.PlayListItem
import com.digimoplus.moboplayer.domain.repostiry.PlayListRepository
import javax.inject.Inject

class AddNewPlayListUseCase
@Inject
constructor(
    private val playListRepository: PlayListRepository,
) {
    suspend operator fun invoke(playListItem: PlayListItem): Int =
        playListRepository.addPlayList(playListItem)

}