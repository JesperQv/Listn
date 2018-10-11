package com.jesperqvarfordt.listn.device.player

import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.source.MediaSource

interface ExtendedPlayer: Player {
    fun prepare(mediaSource: MediaSource)
}