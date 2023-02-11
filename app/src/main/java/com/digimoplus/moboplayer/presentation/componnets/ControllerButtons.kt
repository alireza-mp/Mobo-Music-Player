package com.digimoplus.moboplayer.presentation.componnets

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.digimoplus.moboplayer.R
import com.digimoplus.moboplayer.presentation.theme.DarkGray
import com.digimoplus.moboplayer.util.PlayListState

@ExperimentalMaterialApi
@Composable
fun ControllerButtons(
    isPlayIng: Boolean,
    alpha: Float,
    playListState: PlayListState,
    onPlayPauseClick: () -> Unit,
    onNext: () -> Unit,
    onPrevious: () -> Unit,
    onPlayListChange: (PlayListState) -> Unit,
) {
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
                            onPlayListChange(PlayListState.SHUFFLE)
                        }
                        PlayListState.SHUFFLE -> {
                            onPlayListChange(PlayListState.LOOP)
                        }
                        PlayListState.LOOP -> {
                            onPlayListChange(PlayListState.CURRENT)
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
        Box(
            modifier = Modifier.weight(1f),
            contentAlignment = Alignment.CenterStart,
        ) {
            //  button
        }
    }
}