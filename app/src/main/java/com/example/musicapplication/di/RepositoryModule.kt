package com.example.musicapplication.di

import com.example.musicapplication.data.repository.MusicsRepositoryImpl
import com.example.musicapplication.data.repository.dataSource.DataStoreLocalDataSource
import com.example.musicapplication.data.repository.dataSource.MusicsLocalDataSource
import com.example.musicapplication.domain.repostiry.MusicsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideMusicsRepository(
        musicsLocalDataSource: MusicsLocalDataSource,
        dataStoreLocalDataSource: DataStoreLocalDataSource,
    ): MusicsRepository {
        return MusicsRepositoryImpl(
            musicsLocalDataSource,
            dataStoreLocalDataSource,
        )
    }

}