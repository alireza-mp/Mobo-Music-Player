package com.digimoplus.moboplayer.presentation.componnets

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.sp
import com.digimoplus.moboplayer.presentation.theme.DarkGray
import com.digimoplus.moboplayer.presentation.theme.LightGray
import com.digimoplus.moboplayer.presentation.theme.torus

@Composable
fun MultiStyleText(text1: String, text2: String) {
    Text(buildAnnotatedString {
        withStyle(
            style = SpanStyle(
                color = DarkGray,
                fontFamily = torus,
                fontWeight = FontWeight.Normal,
                fontSize = 16.sp,
            ),
        ) {
            append(text1)
        }
        withStyle(
            style = SpanStyle(
                color = LightGray,
                fontFamily = torus,
                fontWeight = FontWeight.Normal,
                fontSize = 16.sp,
            ),
        ) {
            append(text2)
        }
    })
}