package com.digimoplus.moboplayer.device.player

class MusicNotificationActions {

    companion object {
        const val NOTIFICATION_REMOVE_TIME = 300000L // 5 minute
        const val NOTIFICATION_ID = 10001
        const val NOTIFICATION_CHANEL_ID = "101010"
        const val NOTIFICATION_CHANEL_NAME = "music_player"

        // actions
        const val ACTION_PLAY = "MUSIC_ACTION_PLAY"
        const val ACTION_PAUSE = "MUSIC_ACTION_PAUSE"
        const val ACTION_NEXT = "MUSIC_ACTION_NEXT"
        const val ACTION_PREVIOUS = "MUSIC_ACTION_PREVIOUS"
        const val ACTION_REMOVE = "MUSIC_ACTION_REMOVE"
        const val ACTION_SHUFFLE = "MUSIC_ACTION_SHUFFLE"
        const val ACTION_LOOP = "MUSIC_ACTION_LOOP"
        const val ACTION_CURRENT_PLAYLIST = "MUSIC_ACTION_CURRENT_PLAYLIST"

    }

}