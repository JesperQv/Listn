package com.jesperqvarfordt.listn.device.player

import android.support.v4.media.MediaDescriptionCompat
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.gms.cast.MediaQueueItem

interface CastExtendedPlayer: Player {
    fun prepare(mediaSource: MediaSource, castSource: Array<MediaQueueItem>, windowIndex: Int, position: Long)
    fun getMediaDescription(index: Int): MediaDescriptionCompat
    fun releaseBasePlayer()
}