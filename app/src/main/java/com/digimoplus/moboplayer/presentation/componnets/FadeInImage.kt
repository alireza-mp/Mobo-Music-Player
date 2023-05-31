package com.digimoplus.moboplayer.presentation.componnets

import android.net.Uri
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import com.digimoplus.moboplayer.R
import com.digimoplus.moboplayer.domain.models.Music
import com.digimoplus.moboplayer.presentation.ui.home.HomeViewModel
import kotlinx.coroutines.launch

// fade in image composable
@Composable
fun FadeInImage(
    currentMusic: Music,
) {
    // image uri
    val imageUri = remember { mutableStateOf<Uri?>(null) }
    // alpha animation
    val alpha = remember { Animatable(0f) }
    // image painter
    val painter = rememberAsyncImagePainter(imageUri.value)

    // listening to change current music
    LaunchedEffect(key1 = currentMusic.imageUri) {
        alpha.snapTo(0f) // gone image view
        imageUri.value = currentMusic.imageUri //after image gone load image
    }

    // loading state
    LaunchedEffect(key1 = painter.state, block = {
        launch {
            when (painter.state) {
                // if success full start animation
                // if error set default image and state animation
                is AsyncImagePainter.State.Success, is AsyncImagePainter.State.Error -> {
                    alpha.animateTo(
                        targetValue = 1f,
                        animationSpec = tween(
                            durationMillis = 1000,
                            delayMillis = 0
                        )
                    )
                }

                else -> {} // loading state
            }
        }
    })

    Image(
        painter = if (painter.state is AsyncImagePainter.State.Error) // if error load default image
            painterResource(id = R.drawable.default_image) else painter, // else load image
        modifier = Modifier
            .alpha(alpha.value)
            .fillMaxSize(),
        contentDescription = null,
        contentScale = ContentScale.Crop,
    )

}