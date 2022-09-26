package com.example.musicapplication.domain.useCase

import com.example.musicapplication.domain.repostiry.MusicsRepository
import javax.inject.Inject

class GetLastDataStoreUseCase
@Inject
constructor(
    private val repository: MusicsRepository,
) {
    suspend operator fun invoke() = repository.getLastDataStore()
}