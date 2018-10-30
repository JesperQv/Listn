package com.jesperqvarfordt.listn.device.player

import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.gms.cast.MediaQueueItem

interface CastExtendedPlayer: Player {
    fun prepare(mediaSource: MediaSource, castSource: Array<MediaQueueItem>)
}