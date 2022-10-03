package com.digimoplus.moboplayer.presentation.componnets

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.forEachGesture
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.PointerEvent
import androidx.compose.ui.input.pointer.PointerInputChange
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun CircleSeekBar(
    percentageState: Float,
    onUpSeekBar: () -> Unit,
    alpha: Float,
    onDownSeekBar: () -> Unit,
    updatePercentage: (Float) -> Unit,
) {

    // update percentage with animation
    val percentage by animateFloatAsState(
        targetValue = percentageState,
        animationSpec = tween(
            durationMillis = if (percentageState == 0f) 800 else 200, // change duration
            easing = LinearEasing,
        )
    )

    //  circle sweep angle
    // - 180 / percentage
    val sweepAngle = -(1.8f * percentage)

    //  circle radius
    val radiusState: MutableState<Float> = remember {
        mutableStateOf(0f)
    }

    //draw view
    BoxWithConstraints(modifier = Modifier) {
        // change seek bar offset top in different screen width for space between seek bar and image card
        val seekBarOffsetY = remember { (maxWidth * 0.37f) }

        Canvas(
            modifier = Modifier
                .padding(end = 25.dp, start = 25.dp)
                .offset(x = 0.dp, y = (-seekBarOffsetY))
                .fillMaxWidth()
                .height(200.dp)
                .moveIng(
                    radius = radiusState,
                    updatePercentage = {
                        // update percentage state
                        updatePercentage(it)
                    },
                    onDown = {
                        onDownSeekBar() // when user finger is down
                    },
                    onMove = {
                        // on move
                    },
                    onUp = {
                        onUpSeekBar() // when user finger is up
                    },
                ),
            onDraw = {
                //calculate circle radius
                radiusState.value = size.width / 2
                val radius = radiusState.value

                // draw background arc gray
                drawArc(
                    alpha = alpha,
                    color = Color.LightGray,
                    startAngle = 0f,
                    sweepAngle = 180f,
                    useCenter = false,
                    topLeft = Offset(0f, -radius),
                    size = Size(width = radius * 2,
                        height = radius * 2),
                    style = Stroke(5.dp.toPx(), cap = StrokeCap.Round)
                )

                /// draw dark gray arc
                drawArc(
                    alpha = alpha,
                    color = Color.DarkGray,
                    startAngle = 180f,
                    sweepAngle = sweepAngle,
                    useCenter = false,
                    topLeft = Offset(0f, -radius),
                    size = Size(width = radius * 2,
                        height = radius * 2),
                    style = Stroke(5.dp.toPx(), cap = StrokeCap.Round)
                )

                // angleInDegrees
                val angleInDegrees = (sweepAngle) + 90.0
                // calculate dot x offset
                val x =
                    -(radius * sin(Math.toRadians(angleInDegrees))).toFloat() + (radius)
                // calculate dot y offset
                val y =
                    (radius * cos(Math.toRadians(angleInDegrees))).toFloat() /// + (radius)

                // draw dot
                drawCircle(
                    alpha = alpha,
                    color = Color.DarkGray,
                    center = Offset(x = x, y = y),
                    radius = 25f
                )

                drawCircle(
                    alpha = alpha,
                    color = Color.White,
                    center = Offset(x = x, y = y),
                    radius = 15f
                )
            },
        )
    }

}

private fun Modifier.moveIng(
    radius: MutableState<Float>,
    updatePercentage: (percentage: Float) -> Unit,
    onDown: () -> Unit,
    onMove: () -> Unit,
    onUp: () -> Unit,
): Modifier {
    return pointerInput(Unit) {
        forEachGesture {
            awaitPointerEventScope {

                awaitFirstDown()
                // ACTION_DOWN here
                onDown()

                do {
                    //This PointerEvent contains details including
                    // event, id, position and more
                    val event: PointerEvent = awaitPointerEvent()

                    // Consuming event prevents other gestures or scroll to intercept
                    event.changes.forEach { pointerInputChange: PointerInputChange ->
                        // tap offset
                        val tapOffset = pointerInputChange.position

                        pointerInputChange.consume()

                        // update dot position
                        val theta =
                            Math.toDegrees(
                                atan2((tapOffset.y).toDouble(),
                                    (tapOffset.x - radius.value).toDouble()),
                            )

                        // if angle is is 0..180 range
                        if (theta in 0.0..180.0) {
                            // calculate percentage
                            val percentage = (100 - (theta.toFloat() / 1.8f))

                            // get angle width percentage
                            val angle = -(1.8f * percentage)
                            val angleInDegrees = (angle) + 90.0

                            // calculate dot y offset
                            val dotY =
                                (radius.value * cos(Math.toRadians(angleInDegrees))).toFloat()

                            // calculate user where taped
                            val limitedTapY = dotY - tapOffset.y

                            //if in range 50f around seekbar
                            if (limitedTapY <= 50) {
                                updatePercentage(percentage)
                            }
                        }
                        // ACTION_MOVE loop
                        onMove()

                    }
                } while (event.changes.any { it.pressed })

                // ACTION_UP is here
                onUp()
            }
        }
    }
}