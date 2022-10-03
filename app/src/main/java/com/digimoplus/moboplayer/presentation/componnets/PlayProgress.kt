package com.digimoplus.moboplayer.presentation.componnets

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch


@Composable
fun LineProgress(
    size: Int, // size
    duration: Int, // duration
    strokeWidth: Float, //lines widths
    spaceBetween: Float, //Spacing between lines
    color: Color, // lines color
) {

    Box(modifier = Modifier.size(size.dp)) {
        // animatable for animate lines Offset y
        val animY = remember { Animatable(0f) }
        // padding y lines from top and bottom
        val linePaddingY = size - 1f

        // launch animation
        LaunchedEffect(animY) {
            launch {
                animY.animateTo(
                    linePaddingY,
                    animationSpec = infiniteRepeatable( // repeatable animation
                        animation = tween(
                            durationMillis = duration,
                        ),
                        repeatMode = RepeatMode.Reverse
                    )
                )
            }
        }

        //draw lines
        Canvas(modifier = Modifier.fillMaxSize()) {

            // space between + lines widths
            val totalWidth = (strokeWidth * 3f) + (spaceBetween * 2f)
            //total padding / 2 // padding from right and left
            val paddingX = (this.size.width - totalWidth) / 2f
            // line a x offset
            val lineAX = +paddingX
            // line b x offset
            val lineBX = paddingX + spaceBetween + strokeWidth
            // line c x offset
            val lineCX = paddingX + (spaceBetween * 2) + (strokeWidth * 2)


            // line A
            drawLine(
                color = color,
                start = Offset(x = lineAX, y = animY.value),
                end = Offset(x = lineAX, y = (this.size.height - animY.value)),
                cap = StrokeCap.Round,
                strokeWidth = strokeWidth,
            )
            // line B
            drawLine(
                color = color,
                start = Offset(x = lineBX, y = -(animY.value - linePaddingY)),
                end = Offset(x = lineBX, y = (this.size.height - (-(animY.value - linePaddingY)))),
                cap = StrokeCap.Round,
                strokeWidth = strokeWidth,
            )
            // line C
            drawLine(
                color = color,
                start = Offset(x = lineCX, y = animY.value),
                end = Offset(x = lineCX, y = (this.size.height - animY.value)),
                cap = StrokeCap.Round,
                strokeWidth = strokeWidth,
            )

        }
    }
}