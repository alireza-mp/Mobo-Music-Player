package com.digimoplus.moboplayer.domain.useCase

import com.digimoplus.moboplayer.domain.repostiry.MusicsRepository
import javax.inject.Inject

class SavePlayListUseCase
@Inject
constructor(
    private val musicsRepository: MusicsRepository,
) {
    suspend operator fun invoke(
        isLoop: Boolean,
        isShuffle: Boolean,
    ) = musicsRepository.savePlayListData(isLoop, isShuffle)


}