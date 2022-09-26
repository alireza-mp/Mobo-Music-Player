package com.example.musicapplication.presentation.theme

import androidx.compose.material.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.musicapplication.R

// Set of Material typography styles to start with
val torus = FontFamily(
    Font(R.font.torus_pro, FontWeight.Normal)
)

val Typography = Typography(
    body1 = TextStyle(
        fontFamily = torus,
        fontWeight = FontWeight.Normal,
        fontSize = 20.sp
    ),
    body2 = TextStyle(
        fontFamily = torus,
        fontWeight = FontWeight.Normal,
        fontSize = 17.sp
    ),
    caption = TextStyle(
        fontFamily = torus,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp
    )

)