package com.digimoplus.moboplayer.presentation.componnets

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.ehsanmsz.mszprogressindicator.progressindicator.BallPulseProgressIndicator
import com.digimoplus.moboplayer.presentation.theme.DarkGray
import com.digimoplus.moboplayer.presentation.theme.LightWhite

@Composable
fun BallProgressView() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = LightWhite),
        contentAlignment = Alignment.Center,
    ) {
        BallPulseProgressIndicator(
            color = DarkGray
        )
    }
}