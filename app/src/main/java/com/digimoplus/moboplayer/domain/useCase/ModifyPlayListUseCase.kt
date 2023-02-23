package com.digimoplus.moboplayer.domain.useCase

data class ModifyPlayListUseCase(
    val addNewPlayListUseCase: AddNewPlayListUseCase,
    val updatePlayListUseCase: UpdatePlayListUseCase,
    val deletePlayListUseCase: DeletePlayListUseCase,
)
