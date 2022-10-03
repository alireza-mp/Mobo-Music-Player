package com.example.musicapplication.presentation.ui.home

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.material.BottomSheetScaffoldState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.musicapplication.presentation.componnets.CircleSeekBar
import com.example.musicapplication.presentation.componnets.ControllerButtons
import com.example.musicapplication.presentation.componnets.MultiStyleText
import com.example.musicapplication.presentation.theme.DarkGray
import com.example.musicapplication.presentation.theme.LightGray
import com.example.musicapplication.util.MusicState

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun DetailContent(viewModel: HomeViewModel, scaffoldState: BottomSheetScaffoldState) {

    // alpha animation for detail content
    val anim = remember { Animatable(initialValue = 0f) }
    LaunchedEffect(scaffoldState.bottomSheetState.isExpanded) {
        anim.snapTo(0f)
        anim.animateTo(
            targetValue = 1f,
            animationSpec = tween(
                durationMillis = 500,
                easing = LinearEasing,
            ),
        )
    }

    Box(modifier = Modifier.fillMaxWidth()) {

        // circle seekbar
        CricleSeekBarContent(anim.value, viewModel)

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .alpha(anim.value) // set alpha anim
                .padding(top = 80.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(modifier = Modifier.padding(top = 0.dp))

            // music duration text
            MultiStyleTextContent(viewModel)

            Spacer(modifier = Modifier.padding(top = 38.dp))

            // music title text
            Text(
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .fillMaxWidth(),
                text = viewModel.currentMusicUi.title,
                color = DarkGray,
                style = MaterialTheme.typography.body1,
                textAlign = TextAlign.Center,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1
            )

            Spacer(modifier = Modifier.padding(top = 16.dp))

            // artist name text
            Text(
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .fillMaxWidth(),
                text = viewModel.currentMusicUi.artist,
                color = LightGray,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.caption,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
            )
        }
    }
    // controller buttons
    ControllerButtons(
        isPlayIng = viewModel.musicUIState == MusicState.Play,
        alpha = anim.value, // set alpha anim
        shuffleState = viewModel.shuffleState,
        loopState = viewModel.loopState,
        onPlayPauseClick = viewModel::playOrPauseMusic,
        onNext = viewModel::onNext,
        onPrevious = viewModel::onPrevious,
        onShuffle = { viewModel.onPlayListChange(it) },
        onLoop = { viewModel.onLoop(it) }
    )
}

@Composable
private fun MultiStyleTextContent(viewModel: HomeViewModel) {
    MultiStyleText(
        text1 = viewModel.duration,
        text2 = " ~ ${viewModel.currentMusicUi.duration}",
    )
}

@Composable
private fun CricleSeekBarContent(
    anim: Float,
    viewModel: HomeViewModel,
) {
    CircleSeekBar(
        alpha = anim, // set alpha anim
        percentageState = viewModel.percentage,
        onUpSeekBar = viewModel::onUpSeekBar,
        onDownSeekBar = viewModel::onDownSeekBar,
        updatePercentage = { viewModel.updatePercentage(it) }
    )
}