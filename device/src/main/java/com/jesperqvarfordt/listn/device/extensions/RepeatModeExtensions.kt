package com.jesperqvarfordt.listn.device.extensions

import android.support.v4.media.session.PlaybackStateCompat
import com.jesperqvarfordt.listn.domain.model.player.RepeatMode

fun RepeatMode.toPlaybackStateRepeatMode(): Int {
    return when (this) {
        RepeatMode.REPEAT_ALL -> PlaybackStateCompat.REPEAT_MODE_ALL
        RepeatMode.REPEAT_ONE -> PlaybackStateCompat.REPEAT_MODE_ONE
        RepeatMode.REPEAT_NONE -> PlaybackStateCompat.REPEAT_MODE_NONE
    }
}