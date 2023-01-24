package com.digimoplus.moboplayer.presentation.ui.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.digimoplus.moboplayer.R
import com.digimoplus.moboplayer.presentation.componnets.FadeInImage
import com.digimoplus.moboplayer.presentation.componnets.MusicLineAnimation
import com.digimoplus.moboplayer.presentation.componnets.ScrollingText
import com.digimoplus.moboplayer.presentation.theme.DarkGray
import com.digimoplus.moboplayer.presentation.theme.LightWhite
import com.digimoplus.moboplayer.util.MusicState
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
                FadeInImage(viewModel)
                CardContent(viewModel)
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
private fun CardContent(viewModel: HomeViewModel) {

    Box(
        modifier = Modifier
            .fillMaxSize()
            .alpha(viewModel.currentFraction)
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
                isPlaying = viewModel.musicUIState == MusicState.Play
            )
        }
    }


    Box(
        modifier = Modifier
            .fillMaxSize()
            .alpha(1f - viewModel.currentFraction)
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