package com.digimoplus.moboplayer.presentation.ui.home

import androidx.compose.foundation.layout.*
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.digimoplus.moboplayer.presentation.componnets.CircleSeekBar
import com.digimoplus.moboplayer.presentation.componnets.ControllerButtons
import com.digimoplus.moboplayer.presentation.componnets.MultiStyleText
import com.digimoplus.moboplayer.presentation.theme.DarkGray
import com.digimoplus.moboplayer.presentation.theme.LightGray
import com.digimoplus.moboplayer.util.MusicState

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun DetailContent(viewModel: HomeViewModel) {

    Box(modifier = Modifier.fillMaxWidth()) {

        // circle seekbar
        CircleSeekBarContent(viewModel.currentFraction, viewModel)

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .alpha(viewModel.currentFraction) // set alpha anim
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
        alpha = viewModel.currentFraction, // set alpha anim
        playListState = viewModel.playListState,
        onPlayPauseClick = viewModel::playOrPauseMusic,
        onNext = viewModel::onNext,
        onPrevious = viewModel::onPrevious,
        onPlayListChange = { viewModel.onPlayListChange(it) },
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
private fun CircleSeekBarContent(
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