package com.example.musicapplication.presentation.ui.home

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.musicapplication.presentation.componnets.MusicItem
import com.example.musicapplication.util.MusicState

@Composable
fun MusicListContent(viewModel: HomeViewModel) {

    // launch animation
    val animation = remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        // call list animation every time view launch
        animation.value = true
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 75.dp, top = 20.dp),
    ) {

        itemsIndexed(viewModel.musicList, key = { _, item -> item.id }) { index, model ->

            MusicItem(
                model = model,
                index = index,
                animation = animation.value,
                isPlayIng = viewModel.musicUIState == MusicState.Play && viewModel.currentMusicUi.id == model.id, // check music is palying
                enabled = viewModel.currentMusicUi.id == model.id,
                onItemClick = { viewModel.onItemClick(index) }
            )

        }
    }
}