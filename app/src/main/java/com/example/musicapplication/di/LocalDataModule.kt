package com.example.musicapplication.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.CodeBoy.MediaFacer.AudioGet
import com.example.musicapplication.data.repository.dataSource.DataStoreLocalDataSource
import com.example.musicapplication.data.repository.dataSource.MusicsLocalDataSource
import com.example.musicapplication.data.repository.dataSourceImpl.DataStoreLocalDataSourceImpl
import com.example.musicapplication.data.repository.dataSourceImpl.MusicsLocalDataSourceImpl
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
    fun provideMusicsLocalDataSource(
        audioGet: AudioGet,
        dataStoreLocalDataSource: DataStoreLocalDataSource,
    ): MusicsLocalDataSource {
        return MusicsLocalDataSourceImpl(audioGet, dataStoreLocalDataSource)
    }

    @Singleton
    @Provides
    fun provideDataStoreLocalDataSource(
        datastore: DataStore<Preferences>,
    ): DataStoreLocalDataSource {
        return DataStoreLocalDataSourceImpl(datastore)
    }

}