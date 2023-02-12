package com.digimoplus.moboplayer.di

import com.digimoplus.moboplayer.data.dataSource.DataStoreDataSource
import com.digimoplus.moboplayer.data.dataSource.MusicsDataSource
import com.digimoplus.moboplayer.data.dataSource.PlayListDataSource
import com.digimoplus.moboplayer.data.repository.MusicsRepositoryImpl
import com.digimoplus.moboplayer.data.repository.PlayListRepositoryImpl
import com.digimoplus.moboplayer.domain.repostiry.MusicsRepository
import com.digimoplus.moboplayer.domain.repostiry.PlayListRepository
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
        musicsDataSource: MusicsDataSource,
        dataStoreDataSource: DataStoreDataSource,
    ): MusicsRepository {
        return MusicsRepositoryImpl(
            musicsDataSource,
            dataStoreDataSource,
        )
    }

}