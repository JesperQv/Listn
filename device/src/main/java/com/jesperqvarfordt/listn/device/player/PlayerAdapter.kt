package com.jesperqvarfordt.listn.device.player

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.media.AudioManager
import android.support.v4.media.MediaDescriptionCompat
import android.support.v4.media.MediaMetadataCompat

interface PlayerAdapter {

    val isPlaying: Boolean
    val currentPos: Long

    fun prepare(description: MediaDescriptionCompat?)
    fun addItem(description: MediaDescriptionCompat?)
    fun play()
    fun pause()
    fun stop()
    fun next()
    fun previous()
    fun seekTo(position: Long)
    fun setVolume(volume: Float)

}
