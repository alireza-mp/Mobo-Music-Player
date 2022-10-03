package com.digimoplus.moboplayer.domain.useCase

import com.digimoplus.moboplayer.domain.repostiry.MusicsRepository
import javax.inject.Inject

class SaveLastMusicDataUseCase
@Inject
constructor(
    private val musicsRepository: MusicsRepository,
) {

    suspend operator fun invoke(
        duration: Long,
        currentPosition: Long,
        musicTitle: String,
    ) = musicsRepository.saveLastMusicData(duration, currentPosition, musicTitle)

}