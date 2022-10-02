@file:OptIn(ExperimentalPermissionsApi::class, ExperimentalMaterialApi::class)

package com.example.musicapplication.presentation.ui.home

import android.Manifest
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.material.BottomSheetScaffold
import androidx.compose.material.BottomSheetScaffoldState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.rememberBottomSheetScaffoldState
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
    val scaffoldState = rememberBottomSheetScaffoldState()

    // request for storage permission
    RequestPermission(viewModel, storagePermission)

    // handle back press
    HandleBackPress(coroutineScope, scaffoldState)

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
                // error
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
            if (scaffoldState.bottomSheetState.isCollapsed) {
                DetailContent(viewModel = viewModel, scaffoldState)
            } else {
                MusicListContent(viewModel = viewModel)
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
        ObserveLifecycle(viewModel)
    }
}

@Composable
private fun ObserveLifecycle(
    viewModel: HomeViewModel,
) {
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    DisposableEffect(LocalView.current) {

        // observer for update service when app destroy
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_DESTROY -> {
                    viewModel.updateViewExistListener()
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
    coroutineScope: CoroutineScope,
    scaffoldState: BottomSheetScaffoldState,
) {
    BackHandler(enabled = scaffoldState.drawerState.isOpen || scaffoldState.bottomSheetState.isCollapsed) {
        coroutineScope.launch {
            if (scaffoldState.drawerState.isOpen) {
                //close drawer
                scaffoldState.drawerState.close()
            } else {
                // gone detail content
                // expand bottom sheet
                scaffoldState.bottomSheetState.expand()
            }
        }
    }
}