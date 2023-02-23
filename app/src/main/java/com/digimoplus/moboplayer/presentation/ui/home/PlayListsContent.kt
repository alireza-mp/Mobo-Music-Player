@file:OptIn(ExperimentalFoundationApi::class)

package com.digimoplus.moboplayer.presentation.ui.home

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.digimoplus.moboplayer.presentation.componnets.AddNewPlayListItem
import com.digimoplus.moboplayer.presentation.componnets.AllPlayListItem
import com.digimoplus.moboplayer.presentation.componnets.EditablePlayListItem
import com.digimoplus.moboplayer.presentation.componnets.TitlePlayListItem
import com.digimoplus.moboplayer.util.ModifyState
import kotlinx.coroutines.launch

@Composable
fun PlayListsContent(
    viewModel: HomeViewModel,
    pagerState: PagerState,
) {

    Column {
        TitleList(
            viewModel = viewModel,
            pagerState = pagerState,
        )

        HorizontalPager(
            state = pagerState,
            userScrollEnabled = viewModel.modifyState == ModifyState.None,
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 64.dp), // padding for bottom sheet height
            pageCount = viewModel.playLists.size,
        ) { playListIndex ->
            when (playListIndex) {

                0 -> {
                    AllPlayListItem(musics = viewModel.playLists[playListIndex].musics,
                        sort = viewModel.sortState,
                        musicUiState = viewModel.musicUIState,
                        currentMusic = viewModel.currentMusicUi,
                        onSortClick = viewModel::onSortChange,
                        onItemClick = { musicIndex ->
                            viewModel.onItemClick(
                                playlistIndex = playListIndex,
                                musicIndex = musicIndex,
                            )
                        })

                }

                viewModel.playLists.size - 1 -> {
                    AddNewPlayListItem(
                        sort = viewModel.sortState,
                        musics = viewModel.playLists[playListIndex].musics,
                        onSortClick = viewModel::onSortChange,
                        onItemClick = { musicIndex ->
                            viewModel.onMusicCheckedChange(
                                playListIndex = viewModel.playLists.size - 1,
                                musicIndex = musicIndex,
                            )
                        },
                    )
                }

                else -> {
                    EditablePlayListItem(
                        musics = viewModel.playLists[playListIndex].musics,
                        sort = viewModel.sortState,
                        musicUiState = viewModel.musicUIState,
                        currentMusic = viewModel.currentMusicUi,
                        onSortClick = viewModel::onSortChange,
                        modifyState = viewModel.modifyState,
                        onRemoveClick = { viewModel.onOpenDialog(playListIndex) },
                        onEditClick = { viewModel.onEditPlayListClick(playListIndex) },
                        onCheckClick = { index ->
                            viewModel.onMusicCheckedChange(
                                playListIndex = playListIndex,
                                musicIndex = index,
                            )
                        },
                        onItemClick = { musicIndex ->
                            viewModel.onItemClick(
                                playlistIndex = playListIndex,
                                musicIndex = musicIndex,
                            )
                        },
                    )
                }

            }
        }

    }
}

@Composable
private fun TitleList(
    viewModel: HomeViewModel,
    pagerState: PagerState,
) {

    val selectedIndex = remember { mutableStateOf(0) }
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        state = listState,
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        itemsIndexed(items = viewModel.playLists, key = { _, item -> item.id }) { index, model ->
            TitlePlayListItem(
                model = model,
                index = index,
                selectedIndex = selectedIndex.value,
                currentPlayList = viewModel.currentPlayListIndex,
                playListsSize = viewModel.playLists.size,
                onItemClick = {
                    coroutineScope.launch {
                        if (viewModel.modifyState == ModifyState.None) { // check add new playlist state
                            pagerState.animateScrollToPage(index)
                        }
                    }
                }
            )
        }
    }

    // observe pager changes
    LaunchedEffect(pagerState.currentPage) {
        selectedIndex.value = pagerState.currentPage
        if (pagerState.currentPage - 1 <= 0) {
            listState.animateScrollToItem(0)
        } else {
            listState.animateScrollToItem(pagerState.currentPage - 2)
        }
    }
}