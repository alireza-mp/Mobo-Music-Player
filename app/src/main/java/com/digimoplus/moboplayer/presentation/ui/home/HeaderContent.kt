@file:OptIn(ExperimentalMaterialApi::class, ExperimentalMaterialApi::class)

package com.digimoplus.moboplayer.presentation.ui.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.digimoplus.moboplayer.R
import com.digimoplus.moboplayer.domain.models.Music
import com.digimoplus.moboplayer.presentation.componnets.ControllerButtons
import com.digimoplus.moboplayer.presentation.componnets.FadeInImage
import com.digimoplus.moboplayer.presentation.componnets.MusicLineAnimation
import com.digimoplus.moboplayer.presentation.componnets.ScrollingText
import com.digimoplus.moboplayer.presentation.theme.DarkGray
import com.digimoplus.moboplayer.presentation.theme.LightWhite
import com.digimoplus.moboplayer.util.MusicState
import com.digimoplus.moboplayer.util.PlayListState
import com.digimoplus.moboplayer.util.UiState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun HeaderContent(
    currentMusic: Music,
    musicUiState: MusicState,
    currentFraction: Float,
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
                modifier = Modifier.fillMaxSize(),
                shape = RoundedCornerShape(
                    bottomEnd = 150.dp, bottomStart = 150.dp
                ),
                backgroundColor = LightWhite,
            ) {
                FadeInImage(currentMusic)
                CardContent(
                    musicUiState = musicUiState,
                    currentMusic = currentMusic,
                    currentFraction = currentFraction
                )
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

@Composable
private fun CardContent(
    musicUiState: MusicState,
    currentMusic: Music,
    currentFraction: Float,
) {

    Box(
        modifier = Modifier
            .fillMaxSize()
            .alpha(currentFraction)
    ) {
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
                isPlaying = musicUiState == MusicState.Play
            )
        }
    }


    Box(
        modifier = Modifier
            .fillMaxSize()
            .alpha(1f - currentFraction)
    ) {


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
                text = currentMusic.title,
                color = LightWhite,
                paddingValues = PaddingValues(horizontal = 42.dp),
                style = MaterialTheme.typography.body1,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.padding(top = 16.dp))

            // artist name
            ScrollingText(
                text = currentMusic.artist,
                color = LightWhite,
                style = MaterialTheme.typography.caption,
                paddingValues = PaddingValues(horizontal = 68.dp),
                textAlign = TextAlign.Center,
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