package com.digimoplus.moboplayer.presentation.componnets

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.unit.dp
import com.digimoplus.moboplayer.presentation.componnets.util.toPx
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@Composable
fun MusicLineAnimation(
    isPlaying: Boolean,
) {
    Box(modifier = Modifier.size(200.dp)) {

        // offset Y animations
        val animYOne = remember { Animatable(0f) }
        val animYTwo = remember { Animatable(0f) }
        val animYThree = remember { Animatable(0f) }
        val animYFour = remember { Animatable(0f) }
        val animFive = remember { Animatable(0f) }
        // lines height
        val targetValueOne = 115.dp.toPx()
        val targetValueTwo = 100.dp.toPx()
        val targetValueThree = 90.dp.toPx()
        val targetValueFour = 60.dp.toPx()
        val targetValueFive = 40.dp.toPx()


        // check is playing
        if (isPlaying) {
            //launch anim one
            LaunchedEffect(animYOne) {
                launch {
                    animYOne.animateTo(
                        targetValueOne,
                        animationSpec = infiniteRepeatable( // repeatable animation
                            animation = tween(
                                durationMillis = 600,
                                easing = LinearEasing
                            ),
                            repeatMode = RepeatMode.Reverse
                        )
                    )
                }
            }
            // launch anim two
            LaunchedEffect(animYTwo) {
                delay(300)
                animYTwo.animateTo(
                    targetValueTwo,
                    animationSpec = infiniteRepeatable( // repeatable animation
                        animation = tween(
                            durationMillis = 600,
                            easing = LinearEasing
                        ),
                        repeatMode = RepeatMode.Reverse
                    )
                )
            }
            // launch anim three
            LaunchedEffect(animYThree) {
                delay(180)
                animYThree.animateTo(
                    targetValueThree,
                    animationSpec = infiniteRepeatable( // repeatable animation
                        animation = tween(
                            durationMillis = 500,
                            easing = LinearEasing
                        ),
                        repeatMode = RepeatMode.Reverse
                    )
                )
            }
            // launch anim four
            LaunchedEffect(animYFour) {
                delay(300)
                animYFour.animateTo(
                    targetValueFour,
                    animationSpec = infiniteRepeatable( // repeatable animation
                        animation = tween(
                            durationMillis = 400,
                            easing = LinearEasing
                        ),
                        repeatMode = RepeatMode.Reverse
                    )
                )
            }
            // launch anim five
            LaunchedEffect(animFive) {
                delay(650)
                animFive.animateTo(
                    targetValueFive,
                    animationSpec = infiniteRepeatable( // repeatable animation
                        animation = tween(
                            durationMillis = 400,
                            easing = LinearEasing
                        ),
                        repeatMode = RepeatMode.Reverse
                    )
                )
            }
        } else {
            // not playing  // gone all lines
            LaunchedEffect(Unit) {
                animYOne.snapTo(0f)
                animYTwo.snapTo(0f)
                animYThree.snapTo(0f)
                animYFour.snapTo(0f)
                animFive.snapTo(0f)
            }
        }

        // lines width
        val lineWidth = 10.dp.toPx()
        val spaceBetweenLine = 22.dp.toPx()
        val spaceBetweenLines = spaceBetweenLine * 9

        Canvas(modifier = Modifier
            .fillMaxSize()) {
            // padding for right and left
            val padding = (size.width - spaceBetweenLines) / 2f
            // center Y offset
            val centerY = size.height / 2f
            // lines corner radius
            val cornerRadius = CornerRadius(50f, 50f)
            // color top lines
            val colorTop = Color(0xFFFFFFFF)
            // color bottom lines
            val colorBottom = Color(0x44FFFFFF)

            //draw lines
            // offset x = padding + (total last lines width) + (total spaces between lines)
            drawPath(
                Path().apply {
                    addRoundRect(
                        RoundRect(
                            rect = Rect(
                                offset = Offset(padding + 0f, centerY),
                                size = Size(lineWidth, animYOne.value),
                            ),
                            bottomLeft = cornerRadius,
                            bottomRight = cornerRadius,
                        )
                    )
                },
                color = colorBottom,
            )
            drawPath(
                Path().apply {
                    addRoundRect(
                        RoundRect(
                            rect = Rect(
                                offset = Offset(padding + 0f, centerY),
                                size = Size(lineWidth, -animYOne.value),
                            ),
                            topLeft = cornerRadius,
                            topRight = cornerRadius,
                        )
                    )
                },
                color = colorTop,
            )//1
            drawPath(
                Path().apply {
                    addRoundRect(
                        RoundRect(
                            rect = Rect(
                                offset = Offset(padding + (spaceBetweenLine * 1), centerY),
                                size = Size(lineWidth, animYTwo.value),
                            ),
                            bottomLeft = cornerRadius,
                            bottomRight = cornerRadius,
                        )
                    )
                },
                color = colorBottom,
            )
            drawPath(
                Path().apply {
                    addRoundRect(
                        RoundRect(
                            rect = Rect(
                                offset = Offset(padding + (spaceBetweenLine * 1), centerY),
                                size = Size(lineWidth, -animYTwo.value),
                            ),
                            topLeft = cornerRadius,
                            topRight = cornerRadius,
                        )
                    )
                },
                color = colorTop,
            )//2
            drawPath(
                Path().apply {
                    addRoundRect(
                        RoundRect(
                            rect = Rect(
                                offset = Offset(padding + (spaceBetweenLine * 2), centerY),
                                size = Size(lineWidth, animYOne.value),
                            ),
                            bottomLeft = cornerRadius,
                            bottomRight = cornerRadius,
                        )
                    )
                },
                color = colorBottom,
            )
            drawPath(
                Path().apply {
                    addRoundRect(
                        RoundRect(
                            rect = Rect(
                                offset = Offset(padding + (spaceBetweenLine * 2), centerY),
                                size = Size(lineWidth, -animYOne.value),
                            ),
                            topLeft = cornerRadius,
                            topRight = cornerRadius,
                        )
                    )
                },
                color = colorTop,
            )//3
            drawPath(
                Path().apply {
                    addRoundRect(
                        RoundRect(
                            rect = Rect(
                                offset = Offset(padding + (spaceBetweenLine * 3), centerY),
                                size = Size(lineWidth, animFive.value),
                            ),
                            bottomLeft = cornerRadius,
                            bottomRight = cornerRadius,
                        )
                    )
                },
                color = colorBottom,
            )
            drawPath(
                Path().apply {
                    addRoundRect(
                        RoundRect(
                            rect = Rect(
                                offset = Offset(padding + (spaceBetweenLine * 3), centerY),
                                size = Size(lineWidth, -animFive.value),
                            ),
                            topLeft = cornerRadius,
                            topRight = cornerRadius,
                        )
                    )
                },
                color = colorTop,
            )//4
            drawPath(
                Path().apply {
                    addRoundRect(
                        RoundRect(
                            rect = Rect(
                                offset = Offset(padding + (spaceBetweenLine * 4), centerY),
                                size = Size(lineWidth, animYFour.value),
                            ),
                            bottomLeft = cornerRadius,
                            bottomRight = cornerRadius,
                        )
                    )
                },
                color = colorBottom,
            )
            drawPath(
                Path().apply {
                    addRoundRect(
                        RoundRect(
                            rect = Rect(
                                offset = Offset(padding + (spaceBetweenLine * 4), centerY),
                                size = Size(lineWidth, -animYFour.value),
                            ),
                            topLeft = cornerRadius,
                            topRight = cornerRadius,
                        )
                    )
                },
                color = colorTop,
            )//5
            drawPath(
                Path().apply {
                    addRoundRect(
                        RoundRect(
                            rect = Rect(
                                offset = Offset(padding + (spaceBetweenLine * 5), centerY),
                                size = Size(lineWidth, animFive.value),
                            ),
                            bottomLeft = cornerRadius,
                            bottomRight = cornerRadius,
                        )
                    )
                },
                color = colorBottom,
            )
            drawPath(
                Path().apply {
                    addRoundRect(
                        RoundRect(
                            rect = Rect(
                                offset = Offset(padding + (spaceBetweenLine * 5), centerY),
                                size = Size(lineWidth, -animFive.value),
                            ),
                            topLeft = cornerRadius,
                            topRight = cornerRadius,
                        )
                    )
                },
                color = colorTop,
            )//6
            drawPath(
                Path().apply {
                    addRoundRect(
                        RoundRect(
                            rect = Rect(
                                offset = Offset(padding + (spaceBetweenLine * 6), centerY),
                                size = Size(lineWidth, animYFour.value),
                            ),
                            bottomLeft = cornerRadius,
                            bottomRight = cornerRadius,
                        )
                    )
                },
                color = colorBottom,
            )
            drawPath(
                Path().apply {
                    addRoundRect(
                        RoundRect(
                            rect = Rect(
                                offset = Offset(padding + (spaceBetweenLine * 6), centerY),
                                size = Size(lineWidth, -animYFour.value),
                            ),
                            topLeft = cornerRadius,
                            topRight = cornerRadius,
                        )
                    )
                },
                color = colorTop,
            )//7
            drawPath(
                Path().apply {
                    addRoundRect(
                        RoundRect(
                            rect = Rect(
                                offset = Offset(padding + (spaceBetweenLine * 7), centerY),
                                size = Size(lineWidth, animYTwo.value),
                            ),
                            bottomLeft = cornerRadius,
                            bottomRight = cornerRadius,
                        )
                    )
                },
                color = colorBottom,
            )
            drawPath(
                Path().apply {
                    addRoundRect(
                        RoundRect(
                            rect = Rect(
                                offset = Offset(padding + (spaceBetweenLine * 7), centerY),
                                size = Size(lineWidth, -animYTwo.value),
                            ),
                            topLeft = cornerRadius,
                            topRight = cornerRadius,
                        )
                    )
                },
                color = colorTop,
            )//8
            drawPath(
                Path().apply {
                    addRoundRect(
                        RoundRect(
                            rect = Rect(
                                offset = Offset(padding + (spaceBetweenLine * 8), centerY),
                                size = Size(lineWidth, animYOne.value),
                            ),
                            bottomLeft = cornerRadius,
                            bottomRight = cornerRadius,
                        )
                    )
                },
                color = colorBottom,
            )
            drawPath(
                Path().apply {
                    addRoundRect(
                        RoundRect(
                            rect = Rect(
                                offset = Offset(padding + (spaceBetweenLine * 8), centerY),
                                size = Size(lineWidth, -animYOne.value),
                            ),
                            topLeft = cornerRadius,
                            topRight = cornerRadius,
                        )
                    )
                },
                color = colorTop,
            )//9
            drawPath(
                Path().apply {
                    addRoundRect(
                        RoundRect(
                            rect = Rect(
                                offset = Offset(padding + (spaceBetweenLine * 9), centerY),
                                size = Size(lineWidth, animYFour.value),
                            ),
                            bottomLeft = cornerRadius,
                            bottomRight = cornerRadius,
                        )
                    )
                },
                color = colorBottom,
            )
            drawPath(
                Path().apply {
                    addRoundRect(
                        RoundRect(
                            rect = Rect(
                                offset = Offset(padding + (spaceBetweenLine * 9), centerY),
                                size = Size(lineWidth, -animYFour.value),
                            ),
                            topLeft = cornerRadius,
                            topRight = cornerRadius,
                        )
                    )
                },
                color = colorTop,
            )//10

        }
    }
}