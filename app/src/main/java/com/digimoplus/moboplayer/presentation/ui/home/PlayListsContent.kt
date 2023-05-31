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
import com.digimoplus.moboplayer.domain.models.Music
import com.digimoplus.moboplayer.domain.models.PlayListItem
import com.digimoplus.moboplayer.presentation.componnets.AddNewPlayListItem
import com.digimoplus.moboplayer.presentation.componnets.AllPlayListItem
import com.digimoplus.moboplayer.presentation.componnets.EditablePlayListItem
import com.digimoplus.moboplayer.presentation.componnets.TitlePlayListItem
import com.digimoplus.moboplayer.util.ModifyState
import com.digimoplus.moboplayer.util.MusicState
import com.digimoplus.moboplayer.util.Sort
import kotlinx.coroutines.launch

@Composable
fun PlayListsContent(
    playLists: List<PlayListItem>,
    currentPlayListIndex: Int,
    modifyState: ModifyState,
    pagerState: PagerState,
    sortState: Sort,
    musicUiState: MusicState,
    currentMusic: Music,
    onSortClick: () -> Unit,
    onItemClick: (playListIndex: Int, musicIndex: Int) -> Unit,
    onMusicCheckedChange: (playListIndex: Int, musicIndex: Int) -> Unit,
    onOpenDialog: (playListIndex: Int) -> Unit,
    onEditPlayListClick: (playListIndex: Int) -> Unit,
) {

    Column {
        TitleList(
            playLists = playLists,
            currentPlayListIndex = currentPlayListIndex,
            modifyState = modifyState,
            pagerState = pagerState,
        )

        HorizontalPager(
            state = pagerState,
            userScrollEnabled = modifyState == ModifyState.None,
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 64.dp), // padding for bottom sheet height
            pageCount = playLists.size,
        ) { playListIndex ->
            when (playListIndex) {

                0 -> {
                    AllPlayListItem(
                        musics = playLists[playListIndex].musics,
                        sort = sortState,
                        musicUiState = musicUiState,
                        currentMusic = currentMusic,
                        onSortClick = onSortClick,
                        onItemClick = { musicIndex ->
                            onItemClick(
                                playListIndex,
                                musicIndex,
                            )
                        },
                    )

                }

                playLists.size - 1 -> {
                    AddNewPlayListItem(
                        sort = sortState,
                        musics = playLists[playListIndex].musics,
                        onSortClick = onSortClick,
                        onItemClick = { musicIndex ->
                            onMusicCheckedChange(
                                playLists.size - 1,
                                musicIndex,
                            )
                        },
                    )
                }

                else -> {
                    EditablePlayListItem(
                        musics = playLists[playListIndex].musics,
                        sort = sortState,
                        musicUiState = musicUiState,
                        currentMusic = currentMusic,
                        onSortClick = onSortClick,
                        modifyState = modifyState,
                        onRemoveClick = { onOpenDialog(playListIndex) },
                        onEditClick = { onEditPlayListClick(playListIndex) },
                        onCheckClick = { musicIndex ->
                            onMusicCheckedChange(
                                playListIndex,
                                musicIndex,
                            )
                        },
                        onItemClick = { musicIndex ->
                            onItemClick(
                                playListIndex,
                                musicIndex,
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
    playLists: List<PlayListItem>,
    currentPlayListIndex: Int,
    modifyState: ModifyState,
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
        itemsIndexed(items = playLists, key = { _, item -> item.id }) { index, model ->
            TitlePlayListItem(
                model = model,
                index = index,
                selectedIndex = selectedIndex.value,
                currentPlayList = currentPlayListIndex,
                playListsSize = playLists.size,
                onItemClick = {
                    coroutineScope.launch {
                        if (modifyState == ModifyState.None) { // check add new playlist state
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