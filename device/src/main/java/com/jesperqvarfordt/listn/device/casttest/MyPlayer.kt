package com.jesperqvarfordt.listn.device.casttest

import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.source.MediaSource

interface MyPlayer: Player {
    fun prepare(mediaSource: MediaSource)
}