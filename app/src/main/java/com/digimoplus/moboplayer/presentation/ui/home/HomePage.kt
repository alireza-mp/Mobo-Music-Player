@file:OptIn(
    ExperimentalPermissionsApi::class,
    ExperimentalMaterialApi::class,
    ExperimentalFoundationApi::class
)

package com.digimoplus.moboplayer.presentation.ui.home

import android.Manifest
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
import com.digimoplus.moboplayer.util.UiState
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

    // main screen
    BottomSheetScaffold(
        modifier = Modifier.fillMaxSize(),
        sheetContent = {
            if (viewModel.uiState == UiState.Success) BottomSheetView(
                viewModel,
                scaffoldState,
                pageState
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
                Content(
                    innerPadding = innerPadding,
                    viewModel = viewModel,
                    scaffoldState = scaffoldState,
                    coroutineScope = coroutineScope,
                    pagerState = pageState,
                )

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

// screen content
@Composable
private fun Content(
    viewModel: HomeViewModel,
    innerPadding: PaddingValues,
    scaffoldState: BottomSheetScaffoldState,
    coroutineScope: CoroutineScope,
    pagerState: PagerState,
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
                DetailContent(viewModel = viewModel)
            } else {
                PlayListsContent(viewModel = viewModel, pagerState = pagerState)
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