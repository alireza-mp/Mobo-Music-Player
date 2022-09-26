package com.example.musicapplication.domain.useCase

import com.example.musicapplication.domain.repostiry.MusicsRepository
import javax.inject.Inject

class GetMusicsUseCase
@Inject
constructor(
    private val repository: MusicsRepository,
) {
    // get all musics list
    suspend operator fun invoke() = repository.getAllMusicList()
}