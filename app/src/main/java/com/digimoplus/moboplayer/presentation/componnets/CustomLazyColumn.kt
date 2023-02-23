@file:OptIn(ExperimentalFoundationApi::class)

package com.digimoplus.moboplayer.presentation.componnets

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.digimoplus.moboplayer.domain.models.Music


// add one item first of a lazy column list
@Composable
fun CustomLazyColumn(
    musics: List<Music>,
    firstIndexContent: @Composable () -> Unit,
    itemsContent: @Composable (index: Int) -> Unit,
) {

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 20.dp),
    ) {
        items(
            count = musics.size + 1,
            key = { if (it == 0) -1010 else (it - 1).let { index -> musics[index].id } }
        ) {

            if (it == 0)
                firstIndexContent()
            else Box(modifier = Modifier.animateItemPlacement()) {
                itemsContent((it - 1))
            }

        }
    }

}