package com.example.musicapplication.domain.useCase

import com.example.musicapplication.domain.repostiry.MusicsRepository
import javax.inject.Inject

class SaveLastMusicDataUseCase
@Inject
constructor(
    private val musicsRepository: MusicsRepository,
) {

    suspend operator fun invoke(
        duration: String,
        isLoop: Boolean,
        isShuffle: Boolean,
        percentage: Float,
        musicTitle: String,
    ) = musicsRepository.saveData(duration, isLoop, isShuffle, percentage, musicTitle)

}