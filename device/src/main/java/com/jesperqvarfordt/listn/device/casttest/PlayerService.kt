package com.jesperqvarfordt.listn.device.casttest

import android.os.Bundle
import android.os.SystemClock
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaBrowserServiceCompat
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import com.google.android.exoplayer2.ext.mediasession.MediaSessionConnector
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import com.jesperqvarfordt.listn.device.R
import com.jesperqvarfordt.listn.device.extensions.playbackStateToAvailableActions
import com.jesperqvarfordt.listn.device.extensions.playerStateToPlaybackStateCompat
import com.jesperqvarfordt.listn.device.player.StreamingMusicPlayer

class PlayerService: MediaBrowserServiceCompat() {

    private lateinit var mediaSession: MediaSessionCompat
    private lateinit var mediaSessionConnector: MediaSessionConnector

    private lateinit var player: ExtendedPlayer

    override fun onCreate() {
        super.onCreate()
        player = ListnPlayer(applicationContext, stateChanged = {playWhenReady, playbackState ->
            onStateChange(playWhenReady, playbackState)
        })
        mediaSession = MediaSessionCompat(applicationContext, "ListnMediaSession")

        mediaSession.setFlags(MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS or
                MediaSessionCompat.FLAG_HANDLES_QUEUE_COMMANDS or
                MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS)
        mediaSession.isActive = true
        sessionToken = mediaSession.sessionToken

        mediaSessionConnector = MediaSessionConnector(mediaSession).also {
            val dataSourceFactory = DefaultDataSourceFactory(
                    this, Util.getUserAgent(this, "Listn"), null)
            it.setPlayer(player, ListnPlaybackPreparer(player, dataSourceFactory))
            it.setQueueNavigator(ListnQueueNavigator(mediaSession))
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaSession.apply {
            isActive = false
            release()
        }
    }

    override fun onLoadChildren(parentId: String, result: Result<MutableList<MediaBrowserCompat.MediaItem>>) {
        result.sendResult(StreamingMusicPlayer.playerResources())
    }

    override fun onGetRoot(clientPackageName: String, clientUid: Int, rootHints: Bundle?): BrowserRoot? {
        return BrowserRoot(getString(R.string.app_name), null)
    }

    private fun onStateChange(playWhenReady: Boolean, playbackState: Int) {
        val state = playbackState.playerStateToPlaybackStateCompat(playWhenReady)
        val result = PlaybackStateCompat.Builder()
                .setActions(state.playbackStateToAvailableActions())
                .setState(state, player.currentPosition, 1.0f, SystemClock.elapsedRealtime())
                .build()

        mediaSession.setPlaybackState(result)
    }

}