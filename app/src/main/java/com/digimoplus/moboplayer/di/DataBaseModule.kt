package com.digimoplus.moboplayer.di

import android.app.Application
import androidx.room.Room
import com.digimoplus.moboplayer.data.db.AppDatabase
import com.digimoplus.moboplayer.data.db.dao.MusicDao
import com.digimoplus.moboplayer.data.db.dao.PlayListDao
import com.digimoplus.moboplayer.util.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object DataBaseModule {


    @Provides
    @Singleton
    fun provideAppDatabase(app: Application): AppDatabase {
        return Room.databaseBuilder(app, AppDatabase::class.java, Constants.DATABASE_NAME).build()
    }

    @Singleton
    @Provides
    fun provideMusicDao(db: AppDatabase): MusicDao {
        return db.getMusicDao()
    }

    @Singleton
    @Provides
    fun providePlayListDao(db: AppDatabase): PlayListDao {
        return db.getPlayListDao()
    }

}