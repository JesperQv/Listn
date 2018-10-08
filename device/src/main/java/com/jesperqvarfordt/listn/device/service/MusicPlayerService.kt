package com.jesperqvarfordt.listn.device.service

import android.content.Intent
import android.os.Bundle
import android.os.SystemClock
import android.support.v4.content.ContextCompat
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaBrowserServiceCompat
import android.support.v4.media.MediaDescriptionCompat
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import com.google.android.exoplayer2.Player
import com.jesperqvarfordt.listn.device.R
import com.jesperqvarfordt.listn.device.player.ExoPlayerAdapter
import com.jesperqvarfordt.listn.device.player.NotificationConfig
import com.jesperqvarfordt.listn.device.player.PlayerAdapter
import com.jesperqvarfordt.listn.device.player.StreamingMusicPlayer
import java.util.*


class MusicPlayerService : MediaBrowserServiceCompat() {

    private lateinit var mediaSession: MediaSessionCompat
    private lateinit var mediaSessionCallback: MediaSessionCallback

    private val serviceManager = ServiceManager()
    private lateinit var player: PlayerAdapter

    private var mediaNotificationManager: MediaNotificationManager? = null
    private var serviceInStartedState = false

    override fun onCreate() {
        super.onCreate()
        player = ExoPlayerAdapter(this, serviceManager)
        mediaSession = MediaSessionCompat(applicationContext, "ListnMediaSession")
        mediaSessionCallback = MediaSessionCallback()
        mediaSession.setCallback(mediaSessionCallback)
        mediaSession.setFlags(MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS or
                MediaSessionCompat.FLAG_HANDLES_QUEUE_COMMANDS or
                MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS)
        sessionToken = mediaSession.sessionToken
    }

    override fun onDestroy() {
        super.onDestroy()
        player.stop()
        mediaSession.release()
    }

    override fun onCustomAction(action: String, extras: Bundle?, result: Result<Bundle>) {
        super.onCustomAction(action, extras, result)
        when (action) {
            NOTIFICATION_ACTION -> {
                val config = extras?.getParcelable<NotificationConfig>(argNotificationConfig) ?: return
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

    inner class MediaSessionCallback : MediaSessionCompat.Callback() {
        private var playlist = ArrayList<MediaSessionCompat.QueueItem>()
        var preparedMedia: MediaMetadataCompat? = null
        private var lastSkip = 0L

        fun updateCurrentMedia(newMediaIndex: Int) {
            preparedMedia = StreamingMusicPlayer.getMediaMetadataById(playlist[newMediaIndex].description.mediaId)
            mediaSession.setMetadata(preparedMedia)
        }

        override fun onAddQueueItem(description: MediaDescriptionCompat) {
            playlist.add(MediaSessionCompat.QueueItem(description, description.hashCode().toLong()))
            player.addItem(description) //new
            //queueIndex = if (queueIndex == -1) 0 else queueIndex
            val distinctPlaylist = playlist.distinctBy { it.description.mediaId }
            playlist.clear()
            playlist.addAll(distinctPlaylist)
        }

        override fun onPrepare() {
            player.prepare()

            if (!mediaSession.isActive) {
                mediaSession.isActive = true
            }
        }

        override fun onPlay() {
            player.play()
        }

        override fun onPause() {
            player.pause()
        }

        override fun onStop() {
            player.release()
            mediaSession.isActive = false
            playlist.clear()
            mediaNotificationManager?.notificationManager?.cancelAll()
            stopSelf()
        }

        override fun onSkipToNext() {
            // This is an ugly workaround for a bug in ExoPlayer that sometimes skip twice fast
            val now = System.currentTimeMillis()
            if (now-lastSkip < 100) {
                lastSkip = now
                return
            }
            lastSkip = now
            player.next()
        }

        override fun onSkipToPrevious() {
            player.previous()
        }

        override fun onSeekTo(pos: Long) {
            player.seekTo(pos)
        }

        override fun onSetShuffleMode(shuffleMode: Int) {
            player.setShuffleMode(shuffleMode)
            mediaSession.setShuffleMode(shuffleMode)
        }

        override fun onSetRepeatMode(repeatMode: Int) {
            player.setRepeatMode(repeatMode)
            mediaSession.setRepeatMode(repeatMode)
        }

        override fun onSkipToQueueItem(id: Long) {
            player.skipToQueueItem(playlist.indexOf(playlist.first { it.description.mediaId?.toLong() == id }))
        }

        override fun onCustomAction(action: String?, extras: Bundle?) {
            when (action) {
                CLEAR_LIST_ACTION -> {
                    playlist.clear()
                    player.clearItems()
                }
            }
        }
    }

    private fun getState(exoPlayerState: Int): Int {
        // TODO make sure this doesn't use the exoplayer state
        return when (exoPlayerState) {
            Player.STATE_IDLE -> PlaybackStateCompat.STATE_PAUSED
            Player.STATE_BUFFERING -> PlaybackStateCompat.STATE_BUFFERING
            Player.STATE_READY -> if (player.isPlaying)
                PlaybackStateCompat.STATE_PLAYING
            else
                PlaybackStateCompat.STATE_PAUSED
            Player.STATE_ENDED -> PlaybackStateCompat.STATE_PAUSED
            else -> PlaybackStateCompat.STATE_NONE
        }
    }

    private fun getAvailableActions(state: Int): Long {
        var actions = (PlaybackStateCompat.ACTION_PLAY_FROM_MEDIA_ID
                or PlaybackStateCompat.ACTION_PLAY_FROM_SEARCH
                or PlaybackStateCompat.ACTION_SKIP_TO_NEXT
                or PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS)
        actions = when (state) {
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

    inner class ServiceManager: ExoPlayerAdapter.ExoPlayerStateChangeListener {

        override fun onMediaChanged(newMediaIndex: Int) {
            mediaSessionCallback.updateCurrentMedia(newMediaIndex)
        }

        override fun onStateChange(playWhenReady: Boolean, playbackState: Int) {
            val state = getState(playbackState)

            val result = PlaybackStateCompat.Builder()
                    .setActions(getAvailableActions(state))
                    .setState(state, player.currentPos, 1.0f, SystemClock.elapsedRealtime())
                    .build()

            when (result.state) {
                PlaybackStateCompat.STATE_PLAYING -> moveServiceToStartedState(result)
                PlaybackStateCompat.STATE_PAUSED -> updateNotificationForPause(result)
                PlaybackStateCompat.STATE_STOPPED -> moveServiceOutOfStartedState()
            }

            mediaSession.setPlaybackState(result)
            if (playbackState == Player.STATE_ENDED) {
                mediaSession.controller.transportControls.skipToNext()
            }
        }

        private fun moveServiceToStartedState(state: PlaybackStateCompat) {
            if (mediaSessionCallback.preparedMedia == null || sessionToken == null) {
                return
            }
            val notification = mediaNotificationManager?.getNotification(
                    mediaSessionCallback.preparedMedia!!, state, sessionToken!!)

            if (!serviceInStartedState) {
                ContextCompat.startForegroundService(
                        this@MusicPlayerService,
                        Intent(this@MusicPlayerService, MusicPlayerService::class.java))
                serviceInStartedState = true
            }

            startForeground(MediaNotificationManager.notificationId, notification)
        }

        private fun updateNotificationForPause(state: PlaybackStateCompat) {
            stopForeground(false)
            if (mediaSessionCallback.preparedMedia == null || sessionToken == null) {
                return
            }
            val notification = mediaNotificationManager?.getNotification(
                    mediaSessionCallback.preparedMedia!!, state, sessionToken!!)
            mediaNotificationManager?.notificationManager?.notify(MediaNotificationManager.notificationId, notification)
        }

        private fun moveServiceOutOfStartedState() {
            stopForeground(true)
            stopSelf()
            serviceInStartedState = false
        }
    }

    companion object {
        const val NOTIFICATION_ACTION = "NOTIFICATION_ACTION"
        const val CLEAR_LIST_ACTION = "CLEAR_LIST"
        const val argNotificationConfig = "notificationConfig"
    }

}