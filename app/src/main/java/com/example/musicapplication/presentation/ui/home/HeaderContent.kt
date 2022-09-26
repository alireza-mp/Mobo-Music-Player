@file:OptIn(ExperimentalMaterialApi::class)

package com.example.musicapplication.presentation.ui.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.musicapplication.R
import com.example.musicapplication.presentation.componnets.FadeInImage
import com.example.musicapplication.presentation.componnets.MusicLineAnimation
import com.example.musicapplication.presentation.componnets.ScrollingText
import com.example.musicapplication.presentation.theme.DarkGray
import com.example.musicapplication.presentation.theme.LightWhite
import com.example.musicapplication.util.MusicState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun HeaderContent(
    viewModel: HomeViewModel,
    coroutineScope: CoroutineScope,
    scaffoldState: BottomSheetScaffoldState,
) {
    Row(modifier = Modifier.fillMaxWidth()) {
        // back/menu icon
        IconButton(
            modifier = Modifier.padding(vertical = 12.dp),
            onClick = { onBackOrMenuClick(viewModel, scaffoldState, coroutineScope) },
        ) {
            Icon(
                painter = painterResource(
                    id = if (viewModel.screenState.value) R.drawable.ic_back else R.drawable.ic_menu
                ),
                tint = DarkGray,
                contentDescription = null,
            )
        }
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight(0.5f)
        ) {
            // rounded card

            Card(
                modifier = Modifier
                    .fillMaxSize(),
                shape = RoundedCornerShape(bottomEnd = 150.dp,
                    bottomStart = 150.dp),
                backgroundColor = LightWhite,
            ) {
                // card background image
                FadeInImage(viewModel)

                Box(modifier = Modifier.fillMaxSize()) {
                    Column(
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .fillMaxWidth()
                            .padding(
                                bottom = if (viewModel.screenState.value) 35.dp else 48.dp,
                            ),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        // check screen state
                        if (viewModel.screenState.value) {
                            // show music line anim
                            MusicLineAnimation(
                                isPlaying = viewModel.musicUIState == MusicState.Play
                            )
                        } else {
                            // show music title
                            ScrollingText(
                                text = viewModel.currentMusicUi.title,
                                color = LightWhite,
                                paddingValues = PaddingValues(horizontal = 42.dp),
                                style = MaterialTheme.typography.body1,
                                textAlign = TextAlign.Center
                            )
                            Spacer(modifier = Modifier.padding(top = 16.dp))
                            // show artist name
                            ScrollingText(
                                text = viewModel.currentMusicUi.artist,
                                color = LightWhite,
                                style = MaterialTheme.typography.caption,
                                paddingValues = PaddingValues(horizontal = 68.dp),
                                textAlign = TextAlign.Center,
                            )
                        }
                    }
                }
            }
        }
        // more icon
        IconButton(
            modifier = Modifier.padding(vertical = 12.dp),
            onClick = { /*TODO*/ },
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_more),
                contentDescription = null,
                tint = DarkGray,
            )
        }
    }

}

private fun onBackOrMenuClick(
    viewModel: HomeViewModel,
    scaffoldState: BottomSheetScaffoldState,
    coroutineScope: CoroutineScope,
) {
    coroutineScope.launch {
        if (viewModel.screenState.value) {
            // detail content is visible
            // go to list content
            viewModel.screenState.value = false
            viewModel.backHandler.value = false // disable back handler
            scaffoldState.bottomSheetState.expand()
        } else {
            // list content is visible
            // open drawer
            if (scaffoldState.drawerState.isClosed) {
                scaffoldState.drawerState.open()
                viewModel.backHandler.value = true // enable back handler for close it
            }
        }
    }
}