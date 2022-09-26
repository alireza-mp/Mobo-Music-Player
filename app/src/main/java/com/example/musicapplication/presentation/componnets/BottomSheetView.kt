package com.example.musicapplication.presentation.componnets

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.musicapplication.R
import com.example.musicapplication.presentation.theme.DarkGray
import com.example.musicapplication.presentation.theme.LightGray
import com.example.musicapplication.presentation.ui.home.HomeViewModel
import com.example.musicapplication.util.MusicState
import com.example.musicapplication.util.bottomSheetEnterT
import com.example.musicapplication.util.bottomSheetExitT
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
    Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
        // animate arrow icon visibility
        AnimatedVisibility(
            visible = viewModel.screenState.value,
            enter = bottomSheetEnterT(),
            exit = bottomSheetExitT(),
        ) {
            // on arrow icon click
            IconButton(onClick = {
                if (viewModel.screenState.value) {
                    coroutineScope.launch {
                        // expand bottom sheet
                        scaffoldState.bottomSheetState.expand()
                        viewModel.screenState.value = false
                    }
                }
            }) {
                // arrow icon
                Icon(
                    modifier = Modifier.graphicsLayer {
                        rotationZ = 90f
                    },
                    painter = painterResource(id = R.drawable.ic_back), contentDescription = null,
                )
            }
        }

        // sheet content
        Row(
            modifier = Modifier
                .height(100.dp)
                .padding(end = 12.dp, start = 12.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            IconButton(onClick = { // bottom sheet button
                coroutineScope.launch {
                    scaffoldState.bottomSheetState.collapse()
                }
            }) {
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
            CircleButton(
                percentage = viewModel.percentage,
                isPlayIng = viewModel.musicUIState == MusicState.Play,
                onclick = viewModel::playOrPauseMusic
            )
            Spacer(modifier = Modifier.padding(end = 8.dp))
        }
    }
}