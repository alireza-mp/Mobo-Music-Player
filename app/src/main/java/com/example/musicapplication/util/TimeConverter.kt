package com.example.musicapplication.util

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.concurrent.TimeUnit


suspend fun convertMilliSecondsToSecond(duration: Int): String {
    return withContext(Dispatchers.IO) {
        String.format("%d:%02d",
            TimeUnit.MILLISECONDS.toMinutes(duration.toLong()),
            TimeUnit.MILLISECONDS.toSeconds(duration.toLong()) -
                    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(duration.toLong()))
        )
    }
}

suspend fun convertPercentageToSecond(duration: Int, percentage: Float): String {
    return withContext(Dispatchers.IO) {
        convertMilliSecondsToSecond(((duration / 100f) * percentage).toInt())
    }
}

suspend fun convertDurationToPercentage(duration: Int, currentPosition: Int): Float {
    return withContext(Dispatchers.IO) {
        currentPosition / (duration / 100f)
    }
}

fun convertPercentageToMilliSeconds(duration: Int, percentage: Float): Int {
    return ((duration / 100f) * percentage).toInt()
}