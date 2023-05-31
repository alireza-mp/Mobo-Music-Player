@file:OptIn(
    ExperimentalPermissionsApi::class,
    ExperimentalMaterialApi::class,
    ExperimentalFoundationApi::class
)

package com.digimoplus.moboplayer.presentation.ui.home

import android.Manifest
import android.os.Build
import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.digimoplus.moboplayer.presentation.componnets.BottomSheetView
import com.digimoplus.moboplayer.presentation.componnets.CustomAlertDialog
import com.digimoplus.moboplayer.presentation.componnets.DrawerContent
import com.digimoplus.moboplayer.presentation.componnets.LogoView
import com.digimoplus.moboplayer.presentation.theme.LightWhite
import com.digimoplus.moboplayer.presentation.theme.White
import com.digimoplus.moboplayer.util.ModifyState
import com.digimoplus.moboplayer.util.MusicState
import com.digimoplus.moboplayer.util.UiState
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


@Composable
fun HomeRoute() {

    val viewModel: HomeViewModel = hiltViewModel()
    val storagePermission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        rememberPermissionState(Manifest.permission.READ_MEDIA_AUDIO)
    } else {
        rememberPermissionState(Manifest.permission.READ_EXTERNAL_STORAGE)
    }
    val coroutineScope = rememberCoroutineScope()
    val pageState = rememberPagerState()
    val scaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = rememberBottomSheetState(
            initialValue = BottomSheetValue.Collapsed,
            animationSpec = tween(durationMillis = 300, easing = LinearEasing),
        )
    )


    // request for storage permission
    RequestPermission(viewModel, storagePermission)

    // handle back press
    HandleBackPress(viewModel, coroutineScope, scaffoldState)

    HomeScreen(
        viewModel = viewModel,
        scaffoldState = scaffoldState,
        coroutineScope = coroutineScope,
        pagerState = pageState,
    )
}


// screen content
@Composable
private fun HomeScreen(
    viewModel: HomeViewModel,
    scaffoldState: BottomSheetScaffoldState,
    coroutineScope: CoroutineScope,
    pagerState: PagerState,
) {

    // main screen
    BottomSheetScaffold(
        modifier = Modifier.fillMaxSize(),
        sheetContent = {
            if (viewModel.uiState == UiState.Success)
                BottomSheetView(
                    viewModel = viewModel,
                    scaffoldState = scaffoldState,
                    pagerState = pagerState
                )
        },
        drawerContent = { DrawerContent() },
        scaffoldState = scaffoldState,
        sheetPeekHeight = if (viewModel.uiState == UiState.Loading) 0.dp else 36.dp,
        sheetGesturesEnabled = viewModel.modifyState == ModifyState.None,
        backgroundColor = LightWhite,
        sheetBackgroundColor = White,
        sheetElevation = 0.dp,
    ) { innerPadding ->

        when (viewModel.uiState) {
            UiState.Loading -> {
                LogoView()
            }

            UiState.Success -> {

                // update ui
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(innerPadding),
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {

                        // Header
                        HeaderContent(
                            currentMusic = viewModel.currentMusicUi,
                            musicUiState = viewModel.musicUIState,
                            currentFraction = viewModel.currentFraction,
                            coroutineScope = coroutineScope,
                            scaffoldState = scaffoldState,
                        )

                        // Content
                        if (scaffoldState.bottomSheetState.isCollapsed) {

                            DetailContent(
                                currentFraction = viewModel.currentFraction,
                                percentageState = viewModel.percentage,
                                durationState = viewModel.duration,
                                currentMusic = viewModel.currentMusicUi,
                                isPlayIng = viewModel.musicUIState == MusicState.Play,
                                playListState = viewModel.playListState,
                                playLists = viewModel.playLists,
                                currentPlayListIndex = viewModel.currentPlayListIndex,
                                onUpSeekBar = viewModel::onUpSeekBar,
                                onDownSeekBar = viewModel::onDownSeekBar,
                                updatePercentage = { viewModel.updatePercentage(it) },
                                onPlayPauseClick = viewModel::playOrPauseMusic,
                                onNext = viewModel::onNext,
                                onPrevious = viewModel::onPrevious,
                                onPlayListChange = { viewModel.onPlayListChange(it) },
                                onPlayListStateChange = { viewModel.onPlayListStateChange(it) }
                            )

                        } else {

                            PlayListsContent(
                                playLists = viewModel.playLists,
                                currentPlayListIndex = viewModel.currentPlayListIndex,
                                modifyState = viewModel.modifyState,
                                pagerState = pagerState,
                                sortState = viewModel.sortState,
                                musicUiState = viewModel.musicUIState,
                                currentMusic = viewModel.currentMusicUi,
                                onSortClick = viewModel::onSortChange,
                                onItemClick = { playListIndex, musicIndex ->
                                    viewModel.onItemClick(playListIndex, musicIndex)
                                },
                                onMusicCheckedChange = { playListIndex, musicIndex ->
                                    viewModel.onMusicCheckedChange(playListIndex, musicIndex)
                                },
                                onOpenDialog = { playListIndex ->
                                    viewModel.onOpenDialog(playListIndex)
                                },
                                onEditPlayListClick = { playListIndex ->
                                    viewModel.onEditPlayListClick(playListIndex)
                                }
                            )
                        }

                    }
                }

                CustomAlertDialog(
                    openDialog = viewModel.openRemoveDialog,
                    onCancel = { viewModel.dialogOnCancel() },
                    onDelete = { viewModel.dialogOnDelete() },
                )
            }

            UiState.Error -> {
                // error
            }
        }

    }
}

// launch storage permission request
@Composable
private fun RequestPermission(
    viewModel: HomeViewModel,
    storagePermission: PermissionState,
) {
    LaunchedEffect(Unit) {
        storagePermission.launchPermissionRequest()
    }
    // check permission request state
    if (storagePermission.status.isGranted) {
        LaunchedEffect(Unit) {
            // get all musics
            viewModel.getAllMusics()
        }
    }
}

@Composable
private fun HandleBackPress(
    viewModel: HomeViewModel,
    coroutineScope: CoroutineScope,
    scaffoldState: BottomSheetScaffoldState,
) {
    BackHandler(
        enabled = scaffoldState.drawerState.isOpen || scaffoldState.bottomSheetState.isCollapsed || viewModel.modifyState != ModifyState.None
    ) {
        coroutineScope.launch {
            when {
                scaffoldState.drawerState.isOpen -> {
                    //close drawer
                    scaffoldState.drawerState.close()
                }

                viewModel.modifyState != ModifyState.None -> {
                    viewModel.cancelModifying()
                }

                else -> {
                    // gone detail content
                    // expand bottom sheet
                    scaffoldState.bottomSheetState.expand()
                }
            }
        }
    }
}