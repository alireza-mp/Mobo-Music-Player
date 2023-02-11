package com.digimoplus.moboplayer.util

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.concurrent.TimeUnit


fun convertMilliSecondsToSecond(currentPosition: Long): String {
    return String.format(
        "%d:%02d",
        TimeUnit.MILLISECONDS.toMinutes(currentPosition),
        TimeUnit.MILLISECONDS.toSeconds(currentPosition) -
                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(currentPosition))
    )
}

//  00:00
fun convertMinuteToMilliSeconds(min: String): Long {
    val time = min.split(":")
    val minute = time[0].toInt()
    val second = time[1].toInt()
    return (((minute * 60) + second) * 1000L)
}

suspend fun convertPercentageToSecond(duration: Long, percentage: Float): String {
    return withContext(Dispatchers.IO) {
        convertMilliSecondsToSecond(((duration / 100f) * percentage).toLong())
    }

}

suspend fun convertPositionToPercentage(duration: Long, currentPosition: Long): Float {
    return withContext(Dispatchers.IO) {
        currentPosition / (duration / 100f)
    }
}

fun convertPositionToPercentageNotSuspend(duration: Long, currentPosition: Long): Float {
    return currentPosition / (duration / 100f)
}

fun convertPercentageToMilliSeconds(duration: Long, percentage: Float): Long {
    return ((duration / 100f) * percentage).toLong()
}