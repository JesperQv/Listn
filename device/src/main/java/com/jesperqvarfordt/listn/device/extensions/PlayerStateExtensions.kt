package com.jesperqvarfordt.listn.device.extensions

import android.support.v4.media.session.PlaybackStateCompat
import com.google.android.exoplayer2.Player

fun Int.playerStateToPlaybackStateCompat(player: Player): Int {
    return when (this) {
        Player.STATE_IDLE -> PlaybackStateCompat.STATE_PAUSED
        Player.STATE_BUFFERING -> PlaybackStateCompat.STATE_BUFFERING
        Player.STATE_READY -> if (player.playWhenReady)
            PlaybackStateCompat.STATE_PLAYING
        else
            PlaybackStateCompat.STATE_PAUSED
        Player.STATE_ENDED -> PlaybackStateCompat.STATE_PAUSED
        else -> PlaybackStateCompat.STATE_NONE
    }
}