package com.example.musicapplication.presentation.componnets


import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.musicapplication.R
import com.example.musicapplication.domain.models.Music
import com.example.musicapplication.presentation.theme.DarkGray
import com.example.musicapplication.presentation.theme.LightGray


// music item
@Composable
fun MusicItem(
    model: Music,
    index: Int,
    animation: Boolean,
    currentMusic: Music,
    isPlayIng: Boolean,
    onItemClick: (music: Music) -> Unit,
) {
    // check is current music set darkGray color
    val color = remember { mutableStateOf(LightGray) }
    color.value = if (currentMusic.id == model.id) DarkGray else LightGray

    // offset y move to top animation
    val targetYOffset = 100f
    val anim = remember { Animatable(targetYOffset) }
    LaunchedEffect(key1 = animation) {
        if (animation) {
            anim.animateTo(
                targetValue = 0f,
                animationSpec = tween(
                    durationMillis = 500,
                    delayMillis = index * 150,
                    easing = LinearEasing,
                )
            )
        } else {
            anim.snapTo(0f) // no anim
        }
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .alpha((targetYOffset - anim.value) / targetYOffset) // calculate alpha animation
            .offset(x = 0.dp, y = anim.value.dp) // offset y animation
            .padding(top = 4.dp, bottom = 4.dp, start = 8.dp, end = 32.dp)
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }, // This is mandatory
                onClick = {
                    onItemClick(model)
                }
            ),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        IconButton(
            onClick = {
                onItemClick(model)
            },
        ) {
            // play/pause icon
            Icon(
                modifier = Modifier.size(20.dp),
                painter = painterResource(
                    id = if (currentMusic.id == model.id && isPlayIng) // if is playing music
                        R.drawable.ic_pause else R.drawable.ic_play
                ),
                tint = color.value,
                contentDescription = null,
            )
        }

        Row(
            modifier = Modifier
                .weight(1f)
                .padding(end = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            // music title text
            Text(
                modifier = Modifier.padding(end = 12.dp),
                text = model.title,
                style = MaterialTheme.typography.body2,
                color = color.value,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            if (currentMusic.id == model.id && isPlayIng) { // if is playing music
                // line progress
                LineProgress(
                    size = 10,
                    duration = 200,
                    strokeWidth = 5f,
                    spaceBetween = 10f,
                    color = DarkGray
                )
            }
        }
        // music duration text
        Text(
            modifier = Modifier
                .padding(end = 1.dp),
            text = model.duration,
            style = MaterialTheme.typography.body2,
            color = color.value,
            maxLines = 1
        )
    }
}