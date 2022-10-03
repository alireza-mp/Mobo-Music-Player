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
import com.digimoplus.moboplayer.presentation.theme.LightGray

@ExperimentalMaterialApi
@Composable
fun ControllerButtons(
    isPlayIng: Boolean,
    alpha: Float,
    shuffleState: Boolean,
    loopState: Boolean,
    onPlayPauseClick: () -> Unit,
    onNext: () -> Unit,
    onPrevious: () -> Unit,
    onLoop: (Boolean) -> Unit,
    onShuffle: (Boolean) -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxSize()
            .alpha(alpha),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(modifier = Modifier.weight(1f),
            contentAlignment = Alignment.CenterEnd) {
            // auto next button
            IconButton(
                onClick = {
                    // update auto next state
                    onShuffle(!shuffleState)
                },
            ) {
                Icon(
                    modifier = Modifier.size(24.dp),
                    tint = if (shuffleState) DarkGray else LightGray,
                    painter = painterResource(id = R.drawable.ic_shuffle),
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
                Box(modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center) {
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
            // loop button
            IconButton(
                onClick = {
                    // update loop state
                    onLoop(!loopState)
                },
            ) {
                Icon(
                    modifier = Modifier.size(26.dp),
                    tint = if (loopState) DarkGray else LightGray,
                    painter = painterResource(id = R.drawable.ic_repeat),
                    contentDescription = null,
                )
            }
        }
    }
}