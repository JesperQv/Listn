package com.jesperqvarfordt.listn.device.service

import android.content.Intent
import android.os.Bundle
import android.os.SystemClock
import android.support.v4.content.ContextCompat
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaBrowserServiceCompat
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaControllerCompat
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import com.google.android.exoplayer2.ext.mediasession.MediaSessionConnector
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import com.jesperqvarfordt.listn.device.R
import com.jesperqvarfordt.listn.device.extensions.playbackStateToAvailableActions
import com.jesperqvarfordt.listn.device.extensions.playerStateToPlaybackStateCompat
import com.jesperqvarfordt.listn.device.player.*

class PlayerService : MediaBrowserServiceCompat() {

    private lateinit var mediaSession: MediaSessionCompat
    private lateinit var mediaSessionConnector: MediaSessionConnector
    private lateinit var mediaController: MediaControllerCompat
    private val mediaControllerCallback = MediaControllerCallback()
    private var mediaNotificationManager: MediaNotificationManager? = null

    private lateinit var player: CastExtendedPlayer

    override fun onCreate() {
        super.onCreate()
        player = ListnPlayer(applicationContext, stateChanged = { playWhenReady, currentPos, playbackState ->
            mediaControllerCallback.stateChanged(playWhenReady, currentPos, playbackState)
        })
        mediaSession = MediaSessionCompat(applicationContext, "ListnMediaSession")
        mediaSession.setFlags(MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS or
                MediaSessionCompat.FLAG_HANDLES_QUEUE_COMMANDS or
                MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS)
        mediaSession.isActive = true
        sessionToken = mediaSession.sessionToken

        mediaController = MediaControllerCompat(this, mediaSession).also {
            it.registerCallback(mediaControllerCallback)
        }

        mediaSessionConnector = MediaSessionConnector(mediaSession).also {
            val dataSourceFactory = DefaultDataSourceFactory(
                    this, Util.getUserAgent(this, "Listn"), null)
            it.setPlayer(player, ExtendedPlaybackPreparer(player, dataSourceFactory))
            it.setQueueNavigator(ExtendedQueueNavigator(mediaSession))
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaSession.apply {
            isActive = false
            release()
        }
    }

    override fun onCustomAction(action: String, extras: Bundle?, result: Result<Bundle>) {
        super.onCustomAction(action, extras, result)
        when (action) {
            NOTIFICATION_ACTION -> {
                val config = extras?.getParcelable<NotificationConfig>(argNotificationConfig)
                        ?: return
                mediaNotificationManager = MediaNotificationManager(this, config)
            }
        }
    }

    override fun onLoadChildren(parentId: String, result: Result<MutableList<MediaBrowserCompat.MediaItem>>) {
        result.sendResult(StreamingMusicPlayer.playerResources())
    }

    override fun onGetRoot(clientPackageName: String, clientUid: Int, rootHints: Bundle?): BrowserRoot? {
        return BrowserRoot(getString(R.string.app_name), null)
    }

    private inner class MediaControllerCallback : MediaControllerCompat.Callback() {

        private var preparedMedia: MediaMetadataCompat? = null
        private var isInStartedState = false

        override fun onMetadataChanged(metadata: MediaMetadataCompat?) {
            super.onMetadataChanged(metadata)
            preparedMedia = metadata
        }

        fun stateChanged(playWhenReady: Boolean, currentPos: Long, playbackState: Int) {
            val state = playbackState.playerStateToPlaybackStateCompat(playWhenReady)
            val result = PlaybackStateCompat.Builder()
                    .setActions(state.playbackStateToAvailableActions())
                    .setState(state, currentPos, 1.0f, SystemClock.elapsedRealtime())
                    .build()

            mediaSession.setPlaybackState(result)
            when (result.state) {
                PlaybackStateCompat.STATE_PLAYING -> moveServiceToStartedState(result)
                PlaybackStateCompat.STATE_PAUSED -> updateNotificationForPause(result)
                PlaybackStateCompat.STATE_STOPPED -> moveServiceOutOfStartedState()
            }
        }

        private fun moveServiceToStartedState(state: PlaybackStateCompat) {
            if (preparedMedia == null || sessionToken == null) {
                return
            }
            val notification = mediaNotificationManager?.getNotification(
                    preparedMedia!!, state, sessionToken!!)

            if (!isInStartedState) {
                ContextCompat.startForegroundService(
                        this@PlayerService,
                        Intent(this@PlayerService, PlayerService::class.java))
                isInStartedState = true
            }

            startForeground(MediaNotificationManager.notificationId, notification)
        }

        private fun updateNotificationForPause(state: PlaybackStateCompat) {
            stopForeground(false)
            if (preparedMedia == null || sessionToken == null) {
                return
            }
            val notification = mediaNotificationManager?.getNotification(
                    preparedMedia!!, state, sessionToken!!)
            mediaNotificationManager?.notificationManager?.notify(MediaNotificationManager.notificationId, notification)
        }

        private fun moveServiceOutOfStartedState() {
            stopForeground(true)
            stopSelf()
            isInStartedState = false
        }
    }

    companion object {
        const val NOTIFICATION_ACTION = "NOTIFICATION_ACTION"
        const val argNotificationConfig = "notificationConfig"
    }
}