package com.digimoplus.moboplayer.domain.useCase

import com.digimoplus.moboplayer.domain.repostiry.MusicsRepository
import javax.inject.Inject

class GetMusicsUseCase
@Inject
constructor(
    private val repository: MusicsRepository,
) {
    // get all musics list
    suspend operator fun invoke() = repository.getAllMusicList()
}