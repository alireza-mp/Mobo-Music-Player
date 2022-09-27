package com.example.musicapplication.presentation.componnets

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.offset
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.unit.dp

// vertical list offset (move item from bottom to top) and alpha(fade in) animation
// state is mutable state // updated in base content with launch view
fun Modifier.listAnim(
    state: Boolean,
    index: Int,
) = composed {
    val anim = animateFloatAsState(
        targetValue = if (state) 0f else 100f, // 100f is a offset start point for move view
        animationSpec = tween(
            durationMillis = 500,
            delayMillis = index * 150, // different delay by index
            easing = LinearEasing,
        )
    )
    this.alpha((100f - anim.value) / 100f) // calculate alpha animation
        .offset(x = 0.dp, y = anim.value.dp) // offset y animation
}