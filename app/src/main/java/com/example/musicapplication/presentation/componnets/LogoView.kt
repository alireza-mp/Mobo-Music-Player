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
import com.example.musicapplication.R

@Composable
fun LogoView() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color(0xFF000000)),
        contentAlignment = Alignment.Center,
    ) {
        Image(
            modifier = Modifier.size(300.dp),
            painter = painterResource(R.drawable.logo),
            contentDescription = null
        )
    }
}