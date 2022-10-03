package com.digimoplus.moboplayer.presentation.componnets

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.digimoplus.moboplayer.R
import com.digimoplus.moboplayer.presentation.theme.DarkGray
import com.digimoplus.moboplayer.presentation.theme.LightWhite

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CircleButton(
    percentage: Float,
    isPlayIng: Boolean,
    onclick: () -> Unit,
) {
    Card(
        modifier = Modifier
            .size(55.dp),
        elevation = 0.dp,
        shape = RoundedCornerShape(26.dp),
        backgroundColor = Color.White,
        onClick = onclick,
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(3.dp),
            contentAlignment = Alignment.Center,
        ) {
            // center icon
            Icon(
                modifier = Modifier.size(26.dp),
                painter = painterResource(
                    id = if (isPlayIng) R.drawable.ic_pause else R.drawable.ic_play
                ),
                tint = DarkGray,
                contentDescription = null,
            )

            Canvas(modifier = Modifier.fillMaxSize(), onDraw = {
                // draw background arc
                drawArc(
                    color = LightWhite,
                    startAngle = 0f,
                    sweepAngle = 360f,
                    useCenter = false,
                    style = Stroke(5.dp.toPx(), cap = StrokeCap.Round),
                )
                // draw day gary arc
                drawArc(
                    color = DarkGray,
                    startAngle = 270f,
                    sweepAngle = (percentage * 3.6f), // calculate angle
                    useCenter = false,
                    style = Stroke(5.dp.toPx(), cap = StrokeCap.Round),
                )
            })
        }
    }
}