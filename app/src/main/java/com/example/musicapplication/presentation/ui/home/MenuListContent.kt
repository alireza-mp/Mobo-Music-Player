package com.example.musicapplication.presentation.ui.home

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.musicapplication.presentation.componnets.MusicItem
import com.example.musicapplication.util.MusicState

@Composable
fun MusicListContent(viewModel: HomeViewModel) {
    // list state
    val listState = rememberLazyListState()
    // music list
    LazyColumn(modifier = Modifier
        .fillMaxSize()
        .padding(bottom = 75.dp, top = 20.dp),
        state = listState
    ) {
        // get first item visible
        val firstVisibleItem = listState.firstVisibleItemIndex
        // is list scrolled
        var isScrolled = false

        itemsIndexed(viewModel.musicList) { index, model ->
            if (firstVisibleItem > 0) {
                isScrolled = true
            }
            MusicItem(
                model = model,
                // first visible for check is first of the list
                /*is scrolled for check when user scrolling to bottom and back to
                 top animation not run again*/
                animation = firstVisibleItem == 0 && !isScrolled,
                index = index,
                currentMusic = viewModel.currentMusicUi,
                isPlayIng = viewModel.musicUIState == MusicState.Play, // check music is palying
                onItemClick = { music -> viewModel.onItemClick(music, index) }
            )
        }
    }
}