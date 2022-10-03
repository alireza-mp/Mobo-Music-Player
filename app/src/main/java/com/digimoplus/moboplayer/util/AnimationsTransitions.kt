package com.digimoplus.moboplayer.util

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.ui.Alignment


fun bottomSheetEnterT(): EnterTransition {
    return fadeIn(
        animationSpec = tween()
    ) + expandVertically(
        animationSpec = tween(),
        expandFrom = Alignment.Top,
    )
}

fun bottomSheetExitT(): ExitTransition {
    return fadeOut(
        animationSpec = tween(),
    ) + shrinkVertically(
        animationSpec = tween(),
        shrinkTowards = Alignment.Bottom,
    )
}