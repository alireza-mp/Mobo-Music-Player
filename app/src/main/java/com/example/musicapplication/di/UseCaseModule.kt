package com.example.musicapplication.di

import com.example.musicapplication.domain.repostiry.MusicsRepository
import com.example.musicapplication.domain.useCase.GetHomeViewStateUseCase
import com.example.musicapplication.domain.useCase.GetLastDataStoreUseCase
import com.example.musicapplication.domain.useCase.GetMusicsUseCase
import com.example.musicapplication.domain.useCase.SaveLastMusicDataUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {

    @Provides
    fun provideGetLastDatastoreUseCase(
        musicsRepository: MusicsRepository,
    ): GetLastDataStoreUseCase {
        return GetLastDataStoreUseCase(musicsRepository)
    }

    @Provides
    fun provideGetMusicsUseCase(
        musicsRepository: MusicsRepository,
    ): GetMusicsUseCase {
        return GetMusicsUseCase(musicsRepository)
    }

    @Provides
    fun provideGetHomeViewState(
        lastDataStoreUseCase: GetLastDataStoreUseCase,
        musicsUseCase: GetMusicsUseCase,
    ): GetHomeViewStateUseCase {
        return GetHomeViewStateUseCase(lastDataStoreUseCase, musicsUseCase)
    }

    @Provides
    fun provideSaveLastMuiscData(
        musicsRepository: MusicsRepository,
    ): SaveLastMusicDataUseCase {
        return SaveLastMusicDataUseCase(musicsRepository)
    }

}