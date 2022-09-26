@file:OptIn(ExperimentalPermissionsApi::class, ExperimentalMaterialApi::class)

package com.example.musicapplication.presentation.ui.home

import android.Manifest
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.example.musicapplication.presentation.componnets.BallProgressView
import com.example.musicapplication.presentation.componnets.BottomSheetView
import com.example.musicapplication.presentation.componnets.DrawerContent
import com.example.musicapplication.presentation.theme.LightWhite
import com.example.musicapplication.presentation.theme.White
import com.example.musicapplication.util.UiState
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


@Composable
fun HomePage() {

    val viewModel: HomeViewModel = hiltViewModel()
    val storagePermission = rememberPermissionState(Manifest.permission.READ_EXTERNAL_STORAGE)
    val coroutineScope = rememberCoroutineScope()
    val scaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = rememberBottomSheetState(initialValue = BottomSheetValue.Expanded)
    )

    // request for storage permission
    RequestPermisision(viewModel, storagePermission)

    // update screen state when scaffold changed
    BottomSheetListener(scaffoldState, viewModel)

    // handle back press
    HandleBackPress(viewModel, coroutineScope, scaffoldState)

    // main screen
    BottomSheetScaffold(
        sheetContent = {
            if (viewModel.uiState == UiState.Success)
                BottomSheetView(viewModel, scaffoldState, coroutineScope)
        }, // set sheet content
        drawerContent = { DrawerContent() },
        scaffoldState = scaffoldState,
        sheetPeekHeight = 36.dp, // sheet height
        backgroundColor = LightWhite,
        sheetBackgroundColor = White,
        sheetElevation = 0.dp,
    ) { innerPadding ->

        // update ui
        when (viewModel.uiState) {
            UiState.Loading -> {
                BallProgressView()
            }
            UiState.Success -> {
                Content(
                    innerPadding = innerPadding,
                    viewModel = viewModel,
                    scaffoldState = scaffoldState,
                    coroutineScope = coroutineScope,
                )
            }
            UiState.Error -> {

            }
        }
    }
}

// screen content
@Composable
fun Content(
    viewModel: HomeViewModel,
    innerPadding: PaddingValues,
    scaffoldState: BottomSheetScaffoldState,
    coroutineScope: CoroutineScope,
) {
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
                viewModel = viewModel,
                scaffoldState = scaffoldState,
                coroutineScope = coroutineScope,
            )

            // Content
            if (viewModel.screenState.value) {
                DetailContent(viewModel = viewModel)
            } else {
                MusicListContent(viewModel = viewModel)
            }

        }
    }
}


// launch storage permission request
@Composable
private fun RequestPermisision(
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
            viewModel.listeningPercentageStateChange()
        }
        ObserveLifesycle(viewModel)
    }
}

@Composable
private fun ObserveLifesycle(
    viewModel: HomeViewModel,
) {
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    DisposableEffect(LocalView.current) {

        // observer for save last music when app is stop
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_STOP -> {
                    viewModel.saveLastMusic()
                }
                else -> {}
            }
        }
        lifecycle.addObserver(observer)
        onDispose {
            lifecycle.removeObserver(observer)
        }
    }
}

@Composable
private fun HandleBackPress(
    viewModel: HomeViewModel,
    coroutineScope: CoroutineScope,
    scaffoldState: BottomSheetScaffoldState,
) {
    BackHandler(enabled = viewModel.backHandler.value) {
        coroutineScope.launch {
            if (viewModel.screenState.value) {
                // gone detail content
                // expand bottom sheet
                scaffoldState.bottomSheetState.expand()
                viewModel.screenState.value = false
            } else {
                //close drawer
                scaffoldState.drawerState.close()
            }
            // disable back handler
            viewModel.backHandler.value = false
        }

    }
}

@Composable
private fun BottomSheetListener(
    scaffoldState: BottomSheetScaffoldState,
    viewModel: HomeViewModel,
) {
    if (scaffoldState.bottomSheetState.isExpanded) {
        viewModel.screenState.value = false
    }
    if (scaffoldState.bottomSheetState.isCollapsed) {
        viewModel.backHandler.value = true
        viewModel.screenState.value = true
    }
}