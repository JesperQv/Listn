package com.jesperqvarfordt.listn.device.player

import android.support.v4.media.MediaDescriptionCompat

interface PlayerAdapter {

    val isPlaying: Boolean
    val currentPos: Long

    fun prepare()
    fun addItem(description: MediaDescriptionCompat?)
    fun play()
    fun pause()
    fun stop()
    fun next()
    fun previous()
    fun skipToQueueItem(index: Int)
    fun seekTo(position: Long)
    fun setVolume(volume: Float)

}
