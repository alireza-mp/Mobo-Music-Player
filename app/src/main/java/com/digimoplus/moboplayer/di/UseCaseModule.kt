package com.digimoplus.moboplayer.di

import com.digimoplus.moboplayer.domain.repostiry.MusicsRepository
import com.digimoplus.moboplayer.domain.repostiry.PlayListRepository
import com.digimoplus.moboplayer.domain.useCase.*
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
    fun provideGetPlayListUseCase(
        playListRepository: PlayListRepository,
    ): GetPlayListsUseCase {
        return GetPlayListsUseCase(playListRepository)
    }

    @Provides
    fun provideDeleteUseCase(
        playListRepository: PlayListRepository,
    ): DeletePlayListUseCase {
        return DeletePlayListUseCase(playListRepository)
    }

    @Provides
    fun provideAddNewPlayListUseCase(
        playListRepository: PlayListRepository,
    ): AddNewPlayListUseCase {
        return AddNewPlayListUseCase(playListRepository)
    }

    @Provides
    fun provideUpdateUseCase(
        playListRepository: PlayListRepository,
    ): UpdatePlayListUseCase {
        return UpdatePlayListUseCase(playListRepository)
    }

    @Provides
    fun provideModifyPlayListUseCase(
        addNewPlayListUseCase: AddNewPlayListUseCase,
        updatePlayListUseCase: UpdatePlayListUseCase,
        deletePlayListUseCase: DeletePlayListUseCase,
    ): ModifyPlayListUseCase {
        return ModifyPlayListUseCase(
            addNewPlayListUseCase = addNewPlayListUseCase,
            updatePlayListUseCase = updatePlayListUseCase,
            deletePlayListUseCase = deletePlayListUseCase,
        )
    }

}