@file:OptIn(ExperimentalMaterialApi::class, ExperimentalFoundationApi::class)

package com.digimoplus.moboplayer.presentation.componnets

import androidx.compose.animation.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.PagerState
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.digimoplus.moboplayer.R
import com.digimoplus.moboplayer.presentation.theme.DarkGray
import com.digimoplus.moboplayer.presentation.theme.LightGray
import com.digimoplus.moboplayer.presentation.theme.White
import com.digimoplus.moboplayer.presentation.ui.home.HomeViewModel
import com.digimoplus.moboplayer.util.ModifyState
import com.digimoplus.moboplayer.util.MusicState
import kotlinx.coroutines.launch

// bottom sheet content
@Composable
fun BottomSheetView(
    viewModel: HomeViewModel,
    scaffoldState: BottomSheetScaffoldState,
    pagerState: PagerState,
) {

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp),
        contentAlignment = Alignment.TopCenter,
    ) {
        if (viewModel.modifyState == ModifyState.None) {
            BottomSheetMusicContent(viewModel, scaffoldState)
        } else {
            BottomSheetModifyPlayListContent(viewModel, pagerState)
        }
    }

}

@Composable
private fun BottomSheetModifyPlayListContent(viewModel: HomeViewModel, pagerState: PagerState) {
    val defaultName = if (viewModel.modifyState == ModifyState.Edit)
        viewModel.getUpdateListDefaultTitle()
    else "playlist${viewModel.playLists.size - 1}"

    val text = remember {
        mutableStateOf(defaultName)
    }
    val coroutineScope = rememberCoroutineScope()
    Row(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        TextField(
            modifier = Modifier.weight(1f),
            value = text.value,
            onValueChange = { text.value = it },
            singleLine = true,
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = Color.Transparent,
                cursorColor = DarkGray,
                disabledLabelColor = Color.Transparent,
                focusedIndicatorColor = DarkGray,
                unfocusedIndicatorColor = LightGray
            ),
        )
        Spacer(modifier = Modifier.width(20.dp))
        Column(
            modifier = Modifier.width(90.dp)
        ) {
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .defaultMinSize(minWidth = 1.dp, minHeight = 1.dp),
                enabled = viewModel.saveModifyEnabled,
                onClick = {
                    coroutineScope.launch {
                        val index = viewModel.saveModify(text.value)
                        pagerState.animateScrollToPage(index)
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = if (viewModel.saveModifyEnabled) DarkGray else DarkGray.copy(
                        alpha = 0.5f
                    ),
                )
            ) {
                Text(
                    text = "Save",
                    fontSize = 14.sp,
                    color = White
                )
            }
            OutlinedButton(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    text.value = defaultName
                    viewModel.cancelModifying()
                },
                border = BorderStroke(1.dp, DarkGray),
            ) {
                Text(
                    text = "Cancel",
                    fontSize = 14.sp,
                    color = DarkGray,
                )
            }
        }
    }

}

@Composable
private fun BottomSheetMusicContent(
    viewModel: HomeViewModel,
    scaffoldState: BottomSheetScaffoldState,
) {
    val coroutineScope = rememberCoroutineScope()

    ObserveBottomSheetFraction(scaffoldState = scaffoldState, viewModel = viewModel)

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
                viewModel.updateCurrentFraction(0f)
            } // gone
            currentValue == BottomSheetValue.Collapsed && fraction == 1f && targetValue == currentValue -> {
                viewModel.updateCurrentFraction(1f)
            }// visible
            currentValue == BottomSheetValue.Expanded -> {
                viewModel.updateCurrentFraction(fraction)
            } // fade in alpha
            /*BottomSheetValue.Collapsed*/ else -> {
            viewModel.updateCurrentFraction(1f - fraction)
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