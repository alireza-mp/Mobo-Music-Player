@file:OptIn(ExperimentalMaterialApi::class, ExperimentalMaterialApi::class)

package com.digimoplus.moboplayer.presentation.componnets

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.digimoplus.moboplayer.R
import com.digimoplus.moboplayer.domain.models.PlayListItem
import com.digimoplus.moboplayer.presentation.theme.DarkGray
import com.digimoplus.moboplayer.presentation.theme.LightGray
import com.digimoplus.moboplayer.util.PlayListState

@Composable
fun ControllerButtons(
    isPlayIng: Boolean,
    alpha: Float,
    playListState: PlayListState,
    playList: List<PlayListItem>,
    currentPlayListIndex: Int,
    onPlayPauseClick: () -> Unit,
    onNext: () -> Unit,
    onPrevious: () -> Unit,
    onPlayListChange: (index: Int) -> Unit,
    onPlayListStateChange: (PlayListState) -> Unit,
) {

    val expandedPlayLists = remember {
        mutableStateOf(false)
    }

    Row(
        modifier = Modifier
            .fillMaxSize()
            .alpha(alpha),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier.weight(1f),
            contentAlignment = Alignment.CenterEnd,
        ) {
            // play list state button
            IconButton(
                onClick = {
                    // update state
                    when (playListState) {
                        PlayListState.CURRENT -> {
                            onPlayListStateChange(PlayListState.SHUFFLE)
                        }

                        PlayListState.SHUFFLE -> {
                            onPlayListStateChange(PlayListState.LOOP)
                        }

                        PlayListState.LOOP -> {
                            onPlayListStateChange(PlayListState.CURRENT)
                        }
                    }
                },
            ) {
                Icon(
                    modifier = Modifier.size(24.dp),
                    tint = DarkGray,
                    painter = painterResource(
                        id = when (playListState) {
                            PlayListState.CURRENT -> {
                                R.drawable.ic_current
                            }

                            PlayListState.SHUFFLE -> {
                                R.drawable.ic_shuffle
                            }

                            PlayListState.LOOP -> {
                                R.drawable.ic_repeat
                            }
                        }
                    ),
                    contentDescription = null,
                )
            }
        }
        // previous button
        Box(
            modifier = Modifier.weight(1f),
            contentAlignment = Alignment.CenterEnd,
        ) {
            IconButton(onClick = onPrevious) {
                Image(
                    modifier = Modifier
                        .size(32.dp)
                        .graphicsLayer {
                            rotationZ = 180f
                        },
                    painter = painterResource(id = R.drawable.ic_skip_right),
                    contentDescription = null,
                )
            }
        }
        // center play/pause button
        Box(
            modifier = Modifier.weight(1f),
            contentAlignment = Alignment.Center,
        ) {
            Card(
                backgroundColor = DarkGray,
                shape = RoundedCornerShape(35.dp),
                modifier = Modifier.size(60.dp),
                onClick = onPlayPauseClick,
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        modifier = Modifier.size(24.dp),
                        tint = Color.White,
                        painter = painterResource(
                            id = if (isPlayIng) R.drawable.ic_pause else R.drawable.ic_play
                        ),
                        contentDescription = "null",
                    )
                }

            }
        }
        // next button
        Box(
            modifier = Modifier.weight(1f),
            contentAlignment = Alignment.CenterStart,
        ) {
            IconButton(onClick = onNext) {
                Image(
                    modifier = Modifier.size(32.dp),
                    painter = painterResource(id = R.drawable.ic_skip_right),
                    contentDescription = null,
                )
            }
        }

        // play list button
        Box(
            modifier = Modifier.weight(1f),
            contentAlignment = Alignment.CenterStart,
        ) {

            IconButton(onClick = {
                expandedPlayLists.value = true
            }) {
                Image(
                    modifier = Modifier.size(26.dp),
                    painter = painterResource(id = R.drawable.ic_play_list),
                    contentDescription = null,
                )
            }

            // drop down menu
            DropdownMenu(
                expanded = expandedPlayLists.value,
                onDismissRequest = {
                    expandedPlayLists.value = false
                }
            ) {
                playList.forEachIndexed { itemIndex, itemValue ->
                    if (itemIndex != playList.size - 1) {
                        DropdownMenuItem(
                            onClick = {
                                expandedPlayLists.value = false
                                onPlayListChange(itemIndex)
                            },
                        ) {
                            Text(
                                text = itemValue.title,
                                color = if (currentPlayListIndex == itemIndex) DarkGray else LightGray,
                                fontSize = 14.sp,
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, heightDp = 150)
@Composable
fun DefaultPreview() {
    ControllerButtons(
        isPlayIng = false,
        alpha = 1f,
        playListState = PlayListState.CURRENT,
        playList = listOf(),
        currentPlayListIndex = 0,
        onPlayPauseClick = { /*TODO*/ },
        onNext = { /*TODO*/ },
        onPrevious = { /*TODO*/ },
        onPlayListChange = { },
        onPlayListStateChange = {}
    )
}