package com.digimoplus.moboplayer.presentation.componnets

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.digimoplus.moboplayer.R
import com.digimoplus.moboplayer.presentation.theme.LightGray

@Composable
fun LogoView() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color(0xFF000000)),
        contentAlignment = Alignment.Center,
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Image(
                modifier = Modifier.size(300.dp),
                painter = painterResource(R.drawable.logo),
                contentDescription = null
            )
            Spacer(Modifier.padding(top = 20.dp))
            Text(
                text = "Mobo Player",
                color = LightGray,
                style = MaterialTheme.typography.body1,
                fontSize = 30.sp,
            )
        }
    }
}