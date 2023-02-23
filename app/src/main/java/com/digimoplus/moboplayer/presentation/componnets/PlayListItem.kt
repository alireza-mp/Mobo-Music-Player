package com.digimoplus.moboplayer.presentation.componnets

import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.digimoplus.moboplayer.R
import com.digimoplus.moboplayer.domain.models.Music
import com.digimoplus.moboplayer.domain.models.PlayListItem
import com.digimoplus.moboplayer.presentation.componnets.util.onClick
import com.digimoplus.moboplayer.presentation.theme.LightGray
import com.digimoplus.moboplayer.util.ModifyState
import com.digimoplus.moboplayer.util.MusicState
import com.digimoplus.moboplayer.util.Sort

@Composable
fun AllPlayListItem(
    musics: List<Music>,
    sort: Sort,
    musicUiState: MusicState,
    currentMusic: Music,
    onSortClick: () -> Unit,
    onItemClick: (index: Int) -> Unit,
) {

    // launch animation
    val animation = remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        // call list animation every time view launch
        animation.value = true
    }

    CustomLazyColumn(
        musics = musics,
        firstIndexContent = {
            PropertiesItem(
                sort = sort,
                controllerButtonsEnabled = false,
                onSortClick = onSortClick,
            )
        },
        itemsContent = { index ->
            val model = musics[index]
            MusicItem(
                model = model,
                index = index,
                animation = animation.value,
                isPlayIng = musicUiState == MusicState.Play && currentMusic.id == model.id,
                enabled = currentMusic.id == model.id,
                onItemClick = {
                    onItemClick(index)
                }
            )
        },
    )
}

@Composable
fun EditablePlayListItem(
    musics: List<Music>,
    sort: Sort,
    musicUiState: MusicState,
    currentMusic: Music,
    modifyState: ModifyState,
    onSortClick: () -> Unit,
    onRemoveClick: () -> Unit,
    onEditClick: () -> Unit,
    onCheckClick: (index: Int) -> Unit,
    onItemClick: (index: Int) -> Unit,
) {

    CustomLazyColumn(
        musics = musics,
        firstIndexContent = {
            PropertiesItem(
                sort = sort,
                controllerButtonsEnabled = true,
                onSortClick = onSortClick,
                onRemoveClick = onRemoveClick,
                onEditClick = onEditClick,
            )
        },
        itemsContent = { index ->
            val model = musics[index]
            if (modifyState == ModifyState.Edit) {
                MusicAddNewPlayListItem(
                    model = musics[index],
                    onItemClick = { onCheckClick(index) }
                )
            } else {
                MusicItem(
                    model = model,
                    index = index,
                    animation = true,
                    isPlayIng = musicUiState == MusicState.Play && currentMusic.id == model.id,
                    enabled = currentMusic.id == model.id,
                    onItemClick = {
                        onItemClick(index)
                    }
                )
            }
        },
    )
}


@Composable
fun AddNewPlayListItem(
    sort: Sort,
    musics: List<Music>,
    onSortClick: () -> Unit,
    onItemClick: (index: Int) -> Unit,
) {
    CustomLazyColumn(
        musics = musics,
        firstIndexContent = {
            PropertiesItem(
                sort = sort,
                controllerButtonsEnabled = false,
                onSortClick = onSortClick,
            )
        },
        itemsContent = { index ->
            MusicAddNewPlayListItem(
                model = musics[index],
                onItemClick = { onItemClick(index) }
            )
        },
    )
}

@Composable
fun TitlePlayListItem(
    model: PlayListItem,
    index: Int,
    selectedIndex: Int,
    currentPlayList: Int,
    playListsSize: Int,
    onItemClick: () -> Unit,
) {
    val color = if (index == selectedIndex) Color.DarkGray else LightGray
    Box(modifier = Modifier
        .padding(
            top = 24.dp,
            bottom = 0.dp,
            end = if (index != playListsSize - 1) 12.dp else 24.dp,
            start = 28.dp,
        )
        .onClick { onItemClick() }) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            if (currentPlayList == index) {
                Icon(
                    modifier = Modifier.size(18.dp),
                    painter = painterResource(id = R.drawable.ic_song),
                    tint = color,
                    contentDescription = null,
                )
                Spacer(modifier = Modifier.width(3.dp))
            }
            Text(
                text = model.title,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = color,
            )
        }
    }
}


