package com.digimoplus.moboplayer.presentation.componnets

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.digimoplus.moboplayer.BuildConfig
import com.digimoplus.moboplayer.presentation.theme.LightGray

@Composable
fun DrawerContent() {
    //drawer content
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter) {
        Text(
            modifier = Modifier.padding(bottom = 24.dp),
            text = BuildConfig.VERSION_NAME,
            fontSize = 12.sp,
            color = LightGray,
        )
    }
}
