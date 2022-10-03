package com.digimoplus.moboplayer.di

import com.digimoplus.moboplayer.data.repository.MusicsRepositoryImpl
import com.digimoplus.moboplayer.data.repository.dataSource.DataStoreLocalDataSource
import com.digimoplus.moboplayer.data.repository.dataSource.MusicsLocalDataSource
import com.digimoplus.moboplayer.domain.repostiry.MusicsRepository
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