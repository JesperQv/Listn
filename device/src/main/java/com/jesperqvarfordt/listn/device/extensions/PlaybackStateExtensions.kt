package com.jesperqvarfordt.listn.device.extensions

import android.support.v4.media.session.PlaybackStateCompat
import com.jesperqvarfordt.listn.domain.model.player.RepeatMode
import com.jesperqvarfordt.listn.domain.model.player.ShuffleMode

fun Int.playbackStateToRepeatMode(): RepeatMode {
    return when (this) {
        PlaybackStateCompat.REPEAT_MODE_ONE -> RepeatMode.REPEAT_ONE
        PlaybackStateCompat.REPEAT_MODE_NONE -> RepeatMode.REPEAT_NONE
        else -> RepeatMode.REPEAT_ALL
    }
}

fun Int.playbackStateToShuffleMode(): ShuffleMode {
    return when (this) {
        PlaybackStateCompat.SHUFFLE_MODE_ALL -> ShuffleMode.SHUFFLE_ALL
        else -> ShuffleMode.SHUFFLE_NONE
    }
}

fun Int.playbackStateToAvailableActions(): Long {
    var actions = (PlaybackStateCompat.ACTION_PLAY_FROM_MEDIA_ID
            or PlaybackStateCompat.ACTION_PLAY_FROM_SEARCH
            or PlaybackStateCompat.ACTION_SKIP_TO_NEXT
            or PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS)
    actions = when (this) {
        PlaybackStateCompat.STATE_STOPPED -> actions or (PlaybackStateCompat.ACTION_PLAY or PlaybackStateCompat.ACTION_PAUSE)
        PlaybackStateCompat.STATE_PLAYING -> actions or (PlaybackStateCompat.ACTION_STOP
                or PlaybackStateCompat.ACTION_PAUSE
                or PlaybackStateCompat.ACTION_SEEK_TO)
        PlaybackStateCompat.STATE_PAUSED -> actions or (PlaybackStateCompat.ACTION_PLAY or PlaybackStateCompat.ACTION_STOP)
        else -> actions or (PlaybackStateCompat.ACTION_PLAY
                or PlaybackStateCompat.ACTION_PLAY_PAUSE
                or PlaybackStateCompat.ACTION_STOP
                or PlaybackStateCompat.ACTION_PAUSE)
    }
    return actions
}