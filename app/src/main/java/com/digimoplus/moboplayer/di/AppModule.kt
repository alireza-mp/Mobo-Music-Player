package com.digimoplus.moboplayer.di

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.CodeBoy.MediaFacer.AudioGet
import com.CodeBoy.MediaFacer.MediaFacer
import com.digimoplus.moboplayer.data.device.player.MusicNotificationManager
import com.digimoplus.moboplayer.data.device.player.MusicPlayer
import com.google.android.exoplayer2.ExoPlayer
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("music_preferences")

    @Provides
    @Singleton
    fun provideDataStore(app: Application): DataStore<Preferences> = app.dataStore

    @Provides
    @Singleton
    fun provideMediaFacer(app: Application): AudioGet {
        return MediaFacer.withAudioContex(app)
    }

    @Provides
    @Singleton
    fun provideMusicPlayer(
        exoPlayer: ExoPlayer,
    ): MusicPlayer {
        return MusicPlayer(
            exoPlayer = exoPlayer,
        )
    }

    @Provides
    @Singleton
    fun provideMusicNotificationManager(
        musicPlayer: MusicPlayer,
        app: Application,
    ): MusicNotificationManager {
        return MusicNotificationManager(
            musicPlayer = musicPlayer,
            context = app
        )
    }

    @Provides
    @Singleton
    fun provideExoPlayer(app: Application): ExoPlayer {
        return ExoPlayer.Builder(app).build()
    }

}