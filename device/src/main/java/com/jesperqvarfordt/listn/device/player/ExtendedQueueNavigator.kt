package com.jesperqvarfordt.listn.device.player

import android.support.v4.media.MediaDescriptionCompat
import android.support.v4.media.session.MediaSessionCompat
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.Timeline
import com.google.android.exoplayer2.ext.mediasession.TimelineQueueNavigator

class ExtendedQueueNavigator(mediaSession: MediaSessionCompat) : TimelineQueueNavigator(mediaSession) {
    private val window = Timeline.Window()
    override fun getMediaDescription(player: Player?, windowIndex: Int): MediaDescriptionCompat {
        /*return player?.currentTimeline
                ?.getWindow(windowIndex, window, true)?.tag as MediaDescriptionCompat*/
        return StreamingMusicPlayer.playlist[windowIndex].description
        //return (player as CastExtendedPlayer).getMediaMetadataAtIndex(windowIndex)
    }
}

class CastQueueNavigator(mediaSession: MediaSessionCompat): TimelineQueueNavigator(mediaSession) {
    private val window = Timeline.Window()
    override fun getMediaDescription(player: Player?, windowIndex: Int): MediaDescriptionCompat {
        return player?.currentTimeline
                ?.getWindow(windowIndex, window, true)?.tag as MediaDescriptionCompat
    }
}