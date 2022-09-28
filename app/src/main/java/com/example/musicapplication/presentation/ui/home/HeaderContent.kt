package com.example.musicapplication.presentation.ui.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun HeaderContent(
    viewModel: HomeViewModel,
    coroutineScope: CoroutineScope,
    scaffoldState: BottomSheetScaffoldState,
) {
    Row(modifier = Modifier.fillMaxWidth()) {
        // back or menu icon
        IconButton(
            modifier = Modifier.padding(vertical = 12.dp),
            onClick = { onBackOrMenuClick(scaffoldState, coroutineScope) },
        ) {
            Icon(
                painter = painterResource(
                    id = if (scaffoldState.bottomSheetState.isCollapsed) R.drawable.ic_back else R.drawable.ic_menu
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

                // music line content animated visibility
                this@Row.AnimatedVisibility(
                    visible = scaffoldState.bottomSheetState.isCollapsed,
                    enter = fadeIn(),
                    exit = fadeOut(),
                ) {
                    Box(modifier = Modifier.fillMaxSize()) {
                        Column(
                            modifier = Modifier
                                .align(Alignment.BottomCenter)
                                .fillMaxWidth()
                                .padding(
                                    bottom = 48.dp,
                                ),
                            horizontalAlignment = Alignment.CenterHorizontally,
                        ) {
                            MusicLineAnimation(
                                isPlaying = viewModel.musicUIState == MusicState.Play
                            )
                        }
                    }
                }

                // music title animated visibility
                this@Row.AnimatedVisibility(
                    visible = scaffoldState.bottomSheetState.isExpanded,
                    enter = fadeIn(),
                    exit = fadeOut(),
                ) {
                    Box(modifier = Modifier.fillMaxSize()) {
                        Column(
                            modifier = Modifier
                                .align(Alignment.BottomCenter)
                                .fillMaxWidth()
                                .padding(
                                    bottom = 35.dp,
                                ),
                            horizontalAlignment = Alignment.CenterHorizontally,
                        ) {
                            // music name
                            ScrollingText(
                                text = viewModel.currentMusicUi.title,
                                color = LightWhite,
                                paddingValues = PaddingValues(horizontal = 42.dp),
                                style = MaterialTheme.typography.body1,
                                textAlign = TextAlign.Center
                            )

                            Spacer(modifier = Modifier.padding(top = 16.dp))

                            // artist name
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

@OptIn(ExperimentalMaterialApi::class)
private fun onBackOrMenuClick(
    scaffoldState: BottomSheetScaffoldState,
    coroutineScope: CoroutineScope,
) {
    coroutineScope.launch {
        if (scaffoldState.bottomSheetState.isCollapsed) {
            // detail content is visible
            // expand bottom sheet
            scaffoldState.bottomSheetState.expand()
        } else {
            // list content is visible
            // open drawer
            if (scaffoldState.drawerState.isClosed) {
                scaffoldState.drawerState.open()
            }
        }
    }
}