package com.jesperqvarfordt.listn.device.extensions

import android.support.v4.media.session.PlaybackStateCompat
import com.jesperqvarfordt.listn.domain.model.player.ShuffleMode

fun ShuffleMode.toPlaybackStateShuffleMode(): Int {
    return when (this) {
        ShuffleMode.SHUFFLE_ALL -> PlaybackStateCompat.SHUFFLE_MODE_ALL
        ShuffleMode.SHUFFLE_NONE -> PlaybackStateCompat.SHUFFLE_MODE_NONE
    }
}