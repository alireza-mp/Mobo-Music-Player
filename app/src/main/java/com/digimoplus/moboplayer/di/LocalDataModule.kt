package com.digimoplus.moboplayer.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.CodeBoy.MediaFacer.AudioGet
import com.digimoplus.moboplayer.data.dataSource.DataStoreDataSource
import com.digimoplus.moboplayer.data.dataSource.MusicsDataSource
import com.digimoplus.moboplayer.data.dataSource.PlayListDataSource
import com.digimoplus.moboplayer.data.dataSourceImpl.DataStoreLocalDataSourceImpl
import com.digimoplus.moboplayer.data.dataSourceImpl.MusicsLocalDataSourceImpl
import com.digimoplus.moboplayer.data.dataSourceImpl.PlayListLocalDataSourceImpl
import com.digimoplus.moboplayer.data.db.dao.PlayListDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LocalDataModule {

    @Singleton
    @Provides
    fun provideMusicsDataSource(
        audioGet: AudioGet,
        dataStoreDataSource: DataStoreDataSource,
    ): MusicsDataSource {
        return MusicsLocalDataSourceImpl(audioGet, dataStoreDataSource)
    }

    @Singleton
    @Provides
    fun provideDataStoreDataSource(
        datastore: DataStore<Preferences>,
    ): DataStoreDataSource {
        return DataStoreLocalDataSourceImpl(datastore)
    }

}