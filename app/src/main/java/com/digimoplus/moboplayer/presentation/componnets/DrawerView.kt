package com.digimoplus.moboplayer.presentation.componnets

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.digimoplus.moboplayer.presentation.theme.LightGray

@Composable
fun DrawerContent() {
    //drawer content
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(
            text = "There is nothing here!",
            style = MaterialTheme.typography.body2,
            color = LightGray,
        )
    }
}
