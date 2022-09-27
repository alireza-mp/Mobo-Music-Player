package com.example.musicapplication.presentation.componnets


import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
    isPlayIng: Boolean,
    enabled: Boolean,
    onItemClick: (music: Music) -> Unit,
) {
    // check current music color
    val color = if (enabled) DarkGray else LightGray

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .listAnim(animation, index) // animation
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
            Icon(
                modifier = Modifier.size(20.dp),
                painter = painterResource(
                    id = if (isPlayIng) // if is playing music
                        R.drawable.ic_pause else R.drawable.ic_play
                ),
                tint = color,
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
                color = color,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            if (isPlayIng) { // if is playing music
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
            color = color,
            maxLines = 1
        )
    }
}