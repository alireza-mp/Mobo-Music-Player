package com.digimoplus.moboplayer.presentation.ui.home

import androidx.compose.foundation.layout.*
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.digimoplus.moboplayer.domain.models.Music
import com.digimoplus.moboplayer.domain.models.PlayListItem
import com.digimoplus.moboplayer.presentation.componnets.CircleSeekBar
import com.digimoplus.moboplayer.presentation.componnets.ControllerButtons
import com.digimoplus.moboplayer.presentation.componnets.MultiStyleText
import com.digimoplus.moboplayer.presentation.theme.DarkGray
import com.digimoplus.moboplayer.presentation.theme.LightGray
import com.digimoplus.moboplayer.util.PlayListState

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun DetailContent(
    currentFraction: Float,
    percentageState: Float,
    durationState: String,
    currentMusic: Music,
    isPlayIng: Boolean,
    playListState: PlayListState,
    playLists: List<PlayListItem>,
    currentPlayListIndex: Int,
    onUpSeekBar: () -> Unit,
    onDownSeekBar: () -> Unit,
    updatePercentage: (Float) -> Unit,
    onPlayPauseClick: () -> Unit,
    onNext: () -> Unit,
    onPrevious: () -> Unit,
    onPlayListChange: (index: Int) -> Unit,
    onPlayListStateChange: (PlayListState) -> Unit,
) {

    Box(modifier = Modifier.fillMaxWidth()) {

        // circle seekbar
        CircleSeekBarContent(
            anim = currentFraction,
            percentageState = percentageState,
            onUpSeekBar = onUpSeekBar,
            onDownSeekBar = onDownSeekBar,
            updatePercentage = updatePercentage,
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .alpha(currentFraction) // set alpha anim
                .padding(top = 80.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(modifier = Modifier.padding(top = 0.dp))

            // music duration text
            MultiStyleTextContent(
                duration = durationState,
                currentMusic = currentMusic,
            )

            Spacer(modifier = Modifier.padding(top = 38.dp))

            // music title text
            Text(
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .fillMaxWidth(),
                text = currentMusic.title,
                color = DarkGray,
                textAlign = TextAlign.Center,
                overflow = TextOverflow.Ellipsis,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                maxLines = 1
            )

            Spacer(modifier = Modifier.padding(top = 16.dp))

            // artist name text
            Text(
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .fillMaxWidth(),
                text = currentMusic.artist,
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
        isPlayIng = isPlayIng,
        alpha = currentFraction, // set alpha anim
        playListState = playListState,
        onPlayPauseClick = onPlayPauseClick,
        onNext = onNext,
        onPrevious = onPrevious,
        onPlayListStateChange = onPlayListStateChange,
        playList = playLists,
        currentPlayListIndex = currentPlayListIndex,
        onPlayListChange = onPlayListChange,
    )
}

@Composable
private fun MultiStyleTextContent(
    duration: String,
    currentMusic: Music,
) {
    MultiStyleText(
        text1 = duration,
        text2 = " ~ ${currentMusic.duration}",
    )
}

@Composable
private fun CircleSeekBarContent(
    anim: Float,
    percentageState: Float,
    onUpSeekBar: () -> Unit,
    onDownSeekBar: () -> Unit,
    updatePercentage: (Float) -> Unit,
) {
    CircleSeekBar(
        alpha = anim, // set alpha anim
        percentageState = percentageState,
        onUpSeekBar = onUpSeekBar,
        onDownSeekBar = onDownSeekBar,
        updatePercentage = updatePercentage,
    )
}