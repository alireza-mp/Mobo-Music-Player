package com.digimoplus.moboplayer.domain.useCase

import com.digimoplus.moboplayer.domain.repostiry.PlayListRepository
import javax.inject.Inject

class GetPlayListsUseCase
@Inject
constructor(
    private val playListRepository: PlayListRepository,
) {

    suspend operator fun invoke() = playListRepository.getPlayLists()

}













