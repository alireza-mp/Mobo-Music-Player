package com.digimoplus.moboplayer.presentation.componnets

import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.digimoplus.moboplayer.R
import com.digimoplus.moboplayer.presentation.theme.DarkGray
import com.digimoplus.moboplayer.presentation.theme.LightGray
import com.digimoplus.moboplayer.presentation.ui.home.HomeViewModel
import com.digimoplus.moboplayer.util.MusicState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

// bottom sheet content
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun BottomSheetView(
    viewModel: HomeViewModel,
    scaffoldState: BottomSheetScaffoldState,
    coroutineScope: CoroutineScope,
) {

    ObserveBottomSheetFraction(scaffoldState = scaffoldState, viewModel = viewModel)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp),
        contentAlignment = Alignment.TopCenter,
    ) {
        //if (viewModel.uiState == UiState.Success) {
        IconButton(
            modifier = Modifier
                .height(36.dp)
                .offset(y = ((viewModel.currentFraction * 8f) - 8f).dp) // animate to top
                .alpha(viewModel.currentFraction), // animate alpha
            onClick = {
                coroutineScope.launch {
                    // expand bottom sheet
                    scaffoldState.bottomSheetState.expand()
                }
            },
        ) {
            // arrow icon
            Icon(
                modifier = Modifier.graphicsLayer { rotationZ = 90f },
                painter = painterResource(id = R.drawable.ic_back), contentDescription = null,
            )
        }

        Row(
            modifier = Modifier
                .padding(end = 12.dp, start = 12.dp)
                .alpha(1f - viewModel.currentFraction) // animate alpha
                .fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            // bottom sheet button
            IconButton(
                onClick = {
                    coroutineScope.launch {
                        scaffoldState.bottomSheetState.collapse()
                    }
                },
            ) {
                Icon(
                    modifier = Modifier.graphicsLayer {
                        rotationZ = 270f
                    },
                    painter = painterResource(id = R.drawable.ic_back),
                    tint = LightGray,
                    contentDescription = "",
                )
            }

            Spacer(modifier = Modifier.padding(end = 8.dp))

            Column(modifier = Modifier.weight(1f)) {
                // text music title
                Text(
                    text = viewModel.currentMusicUi.title,
                    color = DarkGray,
                    style = MaterialTheme.typography.body2,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )

                Spacer(modifier = Modifier.padding(top = 10.dp))
                // text artist name
                Text(
                    text = viewModel.currentMusicUi.artist,
                    color = LightGray,
                    style = MaterialTheme.typography.caption,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }

            Spacer(modifier = Modifier.padding(end = 8.dp))

            // circle button
            CircleButtonContent(viewModel)

            Spacer(modifier = Modifier.padding(end = 8.dp))
        }
    }

}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun ObserveBottomSheetFraction(
    scaffoldState: BottomSheetScaffoldState,
    viewModel: HomeViewModel,
) {

    LaunchedEffect(scaffoldState.bottomSheetState.progress.fraction) {
        val targetValue = scaffoldState.bottomSheetState.targetValue
        val currentValue = scaffoldState.bottomSheetState.currentValue
        val fraction = scaffoldState.bottomSheetState.progress.fraction

        when {
            currentValue == BottomSheetValue.Expanded && fraction == 1f && targetValue == currentValue -> {
                viewModel.currentFraction = 0f
            } // gone
            currentValue == BottomSheetValue.Collapsed && fraction == 1f && targetValue == currentValue -> {
                viewModel.currentFraction = 1f
            }// visible
            currentValue == BottomSheetValue.Expanded -> {
                viewModel.currentFraction = fraction
            } // fade in alpha
            /*BottomSheetValue.Collapsed*/ else -> {
            viewModel.currentFraction = 1f - fraction
        }  // fade out alpha
        }
    }
}

@Composable
private fun CircleButtonContent(
    viewModel: HomeViewModel,
) {
    CircleButton(
        percentage = viewModel.percentage,
        isPlayIng = viewModel.musicUIState == MusicState.Play,
        onclick = viewModel::playOrPauseMusic
    )
}