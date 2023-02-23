package com.digimoplus.moboplayer.presentation.componnets


import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.digimoplus.moboplayer.R
import com.digimoplus.moboplayer.domain.models.Music
import com.digimoplus.moboplayer.presentation.componnets.util.onClick
import com.digimoplus.moboplayer.presentation.theme.DarkGray
import com.digimoplus.moboplayer.presentation.theme.LightGray
import com.digimoplus.moboplayer.util.Sort

// music item
@Composable
fun MusicItem(
    model: Music,
    index: Int,
    animation: Boolean,
    isPlayIng: Boolean,
    enabled: Boolean,
    onItemClick: () -> Unit,
) {
    // check current music color
    val color = if (enabled) DarkGray else LightGray

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .listAnim(animation, index) // animation
            .padding(top = 4.dp, bottom = 4.dp, start = 8.dp, end = 32.dp)
            .onClick {
                onItemClick()
            },
        verticalAlignment = Alignment.CenterVertically,
    ) {
        IconButton(
            onClick = {
                onItemClick()
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
                    strokeWidthDp = 2.dp,
                    spaceBetweenDp = 4.dp,
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


// add new play list music item
@Composable
fun MusicAddNewPlayListItem(
    model: Music,
    onItemClick: () -> Unit,
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 4.dp, bottom = 4.dp, start = 28.dp, end = 24.dp)
            .onClick {
                onItemClick()
            },
        verticalAlignment = Alignment.CenterVertically,
    ) {

        Row(
            modifier = Modifier
                .weight(1f),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            // music title text
            Text(
                modifier = Modifier.padding(end = 12.dp),
                text = model.title,
                style = MaterialTheme.typography.body2,
                color = DarkGray,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
        // check box
        IconButton(
            onClick = {
                onItemClick()
            },
        ) {
            Image(
                modifier = Modifier.size(if (model.isChecked) 24.dp else 34.dp),
                painter = painterResource(
                    id = if (model.isChecked) R.drawable.ic_checkbox_minus else R.drawable.ic_checkbox_plus
                ),
                contentDescription = null
            )
        }
    }
}

//properties item
@Composable
fun PropertiesItem(
    sort: Sort,
    controllerButtonsEnabled: Boolean,
    onSortClick: () -> Unit,
    onRemoveClick: (() -> Unit)? = null,
    onEditClick: (() -> Unit)? = null,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Spacer(
            modifier = Modifier
                .width(24.dp)
                .height(50.dp)
        )

        Icon(
            modifier = Modifier
                .size(18.dp)
                .onClick { onSortClick() },
            painter = painterResource(id = R.drawable.ic_sort),
            contentDescription = null,
            tint = Color.DarkGray,
        )
        Spacer(modifier = Modifier.width(10.dp))
        Text(
            modifier = Modifier
                .onClick { onSortClick() },
            text = "sorted by ${sort.text}",
            fontSize = 14.sp,
            color = Color.DarkGray,
        )

        Spacer(modifier = Modifier.weight(1f))
        if (controllerButtonsEnabled) {
            IconButton(onClick = { onEditClick?.invoke() }) {
                Icon(
                    modifier = Modifier.size(16.dp),
                    painter = painterResource(id = R.drawable.ic_edit),
                    contentDescription = null,
                    tint = Color.DarkGray,
                )
            }

            IconButton(onClick = { onRemoveClick?.invoke() }) {
                Icon(
                    modifier = Modifier.size(16.dp),
                    painter = painterResource(id = R.drawable.ic_delete),
                    contentDescription = null,
                    tint = Color.DarkGray,
                )
            }
        }
        Spacer(modifier = Modifier.width(15.dp))
    }
}