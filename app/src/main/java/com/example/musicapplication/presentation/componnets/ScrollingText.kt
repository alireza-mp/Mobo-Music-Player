package com.example.musicapplication.presentation.componnets

import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntSize
import kotlinx.coroutines.delay

//If the size of textview width is not enough start scrolling
@Composable
fun ScrollingText(
    paddingValues: PaddingValues,
    text: String,
    color: Color,
    style: TextStyle,
    textAlign: TextAlign? = null,
) {
    // scroll state
    val scrollState = rememberScrollState()
    // scrolling state animation
    var shouldAnimate by remember { mutableStateOf(true) }
    // text size state
    var textSize by remember { mutableStateOf<IntSize?>(null) }
    // scroll enabled state
    val enableScrolling = remember { mutableStateOf(false) }
    // text size int (not dp)
    val size = textSize?.let { maxOf(it.width, it.height) }
    // density
    val density = LocalDensity.current

    // max text size state
    val maxDimensionDp = remember(textSize) {
        textSize?.let { textSize ->
            // convert to dp
            with(density) {
                maxOf(textSize.width, textSize.height).toDp()
            }
        }
    }

    // text view composable
    val textComposable = @Composable {
        Text(
            color = color,
            maxLines = 1,
            style = style,
            text = text,
            onTextLayout = {
                textSize = it.size // set textview size
            },
            textAlign = textAlign,
            modifier = Modifier
                .horizontalScroll(scrollState, false) // set scroll state
                .drawWithContent {
                    if (textSize != null) {
                        drawContent()
                    }
                }
        )
    }

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxWidth()
            .padding(paddingValues), // set padding
        contentAlignment = Alignment.Center,
    ) {
        when {
            maxDimensionDp == null -> {
                // calculating size.
                // because of drawWithContent it's not gonna be drawn
                textComposable()
            }
            maxDimensionDp < maxWidth -> { // size is okay / disable scrolling
                enableScrolling.value = false // disable scrolling
                Box(
                    modifier = Modifier
                        .width(maxWidth),
                    contentAlignment = Alignment.Center,
                ) {
                    textComposable()
                }
            }
            else -> { // size is not okay / enable scrolling
                enableScrolling.value = true // enable scrolling
                textComposable()
            }
        }

        if (textSize != null) {
            LaunchedEffect(key1 = shouldAnimate) {
                scrollState.animateScrollTo(
                    scrollState.maxValue,
                    animationSpec = tween(
                        //get duration with text size
                        // 2.9f is speed with words
                        durationMillis = ((size ?: 0) * 2.9f).toInt(),
                        delayMillis = 0,
                        easing = CubicBezierEasing(0f, 0f, 0f, 0f),
                    ),
                )
                delay(1000) // 1 second wait after scrolling ended
                scrollState.scrollTo(0) // back to first
                delay(3000) // 3 second wait to start scrolling
                shouldAnimate = !shouldAnimate // scrolling again
            }
        }

    }
}