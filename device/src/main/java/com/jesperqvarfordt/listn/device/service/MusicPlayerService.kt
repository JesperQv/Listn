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
import com.jesperqvarfordt.listn.device.player.CastExoPlayerAdapter
import com.jesperqvarfordt.listn.device.player.NotificationConfig
import com.jesperqvarfordt.listn.device.player.StreamingMusicPlayer
import java.util.*


class MusicPlayerService : MediaBrowserServiceCompat() {

    private lateinit var mediaSession: MediaSessionCompat
    private lateinit var mediaSessionCallback: MediaSessionCallback
    private val serviceManager = ServiceManager()
    private lateinit var playerAdapter: CastExoPlayerAdapter
    private var mediaNotificationManager: MediaNotificationManager? = null
    private var serviceInStartedState = false

    override fun onCreate() {
        super.onCreate()
        playerAdapter = CastExoPlayerAdapter(this, serviceManager)
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
        playerAdapter.stop()
        mediaSession.release()
    }

    override fun onCustomAction(action: String, extras: Bundle?, result: Result<Bundle>) {
        super.onCustomAction(action, extras, result)
        when (action) {
            notificationAction -> {
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
        private var sortedPlaylist = ArrayList<MediaSessionCompat.QueueItem>()
        private var queueIndex = -1
        var preparedMedia: MediaMetadataCompat? = null
        private var shouldContinue = false
        private var lastSkip = 0L
        private val isReadyToPlay: Boolean
            get() = !playlist.isEmpty()

        override fun onAddQueueItem(description: MediaDescriptionCompat) {
            playlist.add(MediaSessionCompat.QueueItem(description, description.hashCode().toLong()))
            queueIndex = if (queueIndex == -1) 0 else queueIndex
            val distinctPlaylist = playlist.distinctBy { it.description.mediaId }
            playlist.clear()
            playlist.addAll(distinctPlaylist)
        }

        override fun onRemoveQueueItem(description: MediaDescriptionCompat) {
            playlist.remove(MediaSessionCompat.QueueItem(description, description.hashCode().toLong()))
            queueIndex = if (playlist.isEmpty()) -1 else queueIndex
        }

        override fun onPrepare() {
            if (queueIndex < 0 && playlist.isEmpty()) {
                // Nothing to play.
                return
            }

            val mediaId = playlist[queueIndex].description.mediaId
            preparedMedia = StreamingMusicPlayer.getMediaMetadataById(mediaId)
            mediaSession.setMetadata(preparedMedia)
            playerAdapter.prepare(preparedMedia)

            if (!mediaSession.isActive) {
                mediaSession.isActive = true
            }
            shouldContinue = false
        }

        override fun onPlay() {
            if (!isReadyToPlay) {
                return
            }

            if (shouldContinue && preparedMedia != null) {
                playerAdapter.play()
                shouldContinue = false
                return
            }

            if (preparedMedia == null) {
                onPrepare()
            }

            playerAdapter.play()
            shouldContinue = false
        }

        override fun onPause() {
            shouldContinue = true
            playerAdapter.pause()
        }

        override fun onStop() {
            playerAdapter.pause()
            mediaSession.isActive = false
            playlist.clear()
            queueIndex = -1
            preparedMedia = null
            shouldContinue = false
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

            preparedMedia = null

            if (mediaSession.controller.repeatMode == PlaybackStateCompat.REPEAT_MODE_ALL) {
                queueIndex = ++queueIndex % playlist.size
            } else if (mediaSession.controller.repeatMode == PlaybackStateCompat.REPEAT_MODE_NONE) {
                queueIndex = ++queueIndex % playlist.size
                if (queueIndex == 0) {
                    onPause()
                    onPrepare()
                    return
                }
            }
            // REPEAT_MODE_ONE: nothing happens, don't change the queueIndex

            if (playerAdapter.isPlaying) {
                onPlay()
            } else {
                onPrepare()
            }
        }

        override fun onSkipToPrevious() {
            preparedMedia = null
            if (mediaSession.controller.repeatMode == PlaybackStateCompat.REPEAT_MODE_ALL) {
                queueIndex = if (queueIndex > 0) queueIndex - 1 else playlist.size - 1
            } else if (mediaSession.controller.repeatMode == PlaybackStateCompat.REPEAT_MODE_NONE) {
                queueIndex = if (queueIndex > 0) queueIndex - 1 else 0
            }
            // REPEAT_MODE_ONE: nothing happens, don't change the queueIndex

            if (playerAdapter.isPlaying) {
                onPlay()
            } else {
                onPrepare()
            }
        }

        override fun onSeekTo(pos: Long) {
            playerAdapter.seekTo(pos)
        }

        override fun onSetShuffleMode(shuffleMode: Int) {
            mediaSession.setShuffleMode(shuffleMode)
            if (shuffleMode == PlaybackStateCompat.SHUFFLE_MODE_ALL) {
                sortedPlaylist.clear()
                sortedPlaylist.addAll(playlist)
                val thisSong = playlist[queueIndex]
                playlist.remove(thisSong)
                playlist.shuffle()
                playlist.add(queueIndex, thisSong)
            } else if (shuffleMode == PlaybackStateCompat.SHUFFLE_MODE_NONE) {
                val thisSongIndex = sortedPlaylist.indexOf(playlist[queueIndex])
                playlist.clear()
                playlist.addAll(sortedPlaylist)
                queueIndex = thisSongIndex
            }
        }

        override fun onSetRepeatMode(repeatMode: Int) {
            mediaSession.setRepeatMode(repeatMode)
        }

        override fun onSkipToQueueItem(id: Long) {
            super.onSkipToQueueItem(id)
            queueIndex = playlist.indexOf(playlist.first { it.description.mediaId?.toLong() == id })
        }
    }

    private fun getState(exoPlayerState: Int): Int {
        // TODO make sure this doesn't use the exoplayer state
        return when (exoPlayerState) {
            Player.STATE_IDLE -> PlaybackStateCompat.STATE_PAUSED
            Player.STATE_BUFFERING -> PlaybackStateCompat.STATE_BUFFERING
            Player.STATE_READY -> if (playerAdapter.isPlaying)
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

    inner class ServiceManager: CastExoPlayerAdapter.ExoPlayerStateChangeListener {

        override fun onStateChange(playWhenReady: Boolean, playbackState: Int) {
            val state = getState(playbackState)

            val result = PlaybackStateCompat.Builder()
                    .setActions(getAvailableActions(state))
                    .setState(state, playerAdapter.currentPos, 1.0f, SystemClock.elapsedRealtime())
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
        const val notificationAction = "notificationAction"
        const val argNotificationConfig = "notificationConfig"
    }

}