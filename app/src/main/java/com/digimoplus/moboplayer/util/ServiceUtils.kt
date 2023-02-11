package com.digimoplus.moboplayer.util

import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import com.digimoplus.moboplayer.device.player.MusicPlayerService

fun Service.stopForeground() {
    stopForeground(Service.STOP_FOREGROUND_REMOVE)
}

// use this in try-catch block
fun Uri.toBitmap(context: Context): Bitmap {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
        ImageDecoder.decodeBitmap(
            ImageDecoder.createSource(
                /* cr = */ context.contentResolver,
                /* uri = */ this
            ),
        )
    } else {
        MediaStore.Images.Media.getBitmap(
            /* cr = */ context.contentResolver,
            /* url = */ this
        )
    }
}

fun Context.startMediaPlayerService() {
    this.startForegroundService(Intent(this, MusicPlayerService::class.java))
}