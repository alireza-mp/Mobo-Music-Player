package com.example.musicapplication.presentation.componnets

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.ehsanmsz.mszprogressindicator.progressindicator.BallPulseProgressIndicator
import com.example.musicapplication.R
import com.example.musicapplication.presentation.theme.DarkGray
import com.example.musicapplication.presentation.theme.LightWhite

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