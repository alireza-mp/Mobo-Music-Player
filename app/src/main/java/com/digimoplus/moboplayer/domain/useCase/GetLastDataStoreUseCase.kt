package com.digimoplus.moboplayer.domain.useCase

import com.digimoplus.moboplayer.domain.repostiry.MusicsRepository
import javax.inject.Inject

class GetLastDataStoreUseCase
@Inject
constructor(
    private val repository: MusicsRepository,
) {
    suspend operator fun invoke() = repository.getLastDataStore()
}