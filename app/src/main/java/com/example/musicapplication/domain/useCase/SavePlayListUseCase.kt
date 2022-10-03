package com.example.musicapplication.domain.useCase

import com.example.musicapplication.domain.repostiry.MusicsRepository
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