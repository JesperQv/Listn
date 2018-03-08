package com.jesperqvarfordt.listn.device.player

import android.content.ComponentName
import android.content.Context
import android.os.Bundle
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaControllerCompat
import android.support.v4.media.session.PlaybackStateCompat
import com.jesperqvarfordt.listn.device.imagecache.ImageCache
import com.jesperqvarfordt.listn.device.service.MusicPlayerService
import com.jesperqvarfordt.listn.device.toMediaMetadata
import com.jesperqvarfordt.listn.domain.model.Track
import com.jesperqvarfordt.listn.domain.model.player.MediaInfo
import com.jesperqvarfordt.listn.domain.model.player.PlayerInfo
import com.jesperqvarfordt.listn.domain.player.MusicPlayer
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject


class StreamingMusicPlayer
constructor(private val context: Context,
            private val notificationConfig: NotificationConfig,
            private val imageCache: ImageCache) : MusicPlayer {

    private var mediaBrowser: MediaBrowserCompat? = null
    private val mediaBrowserConnectionCallback = MediaBrowserConnectionCallback()
    private val mediaBrowserSubscriptionCallback = MediaBrowserSubscriptionCallback()
    private lateinit var mediaController: MediaControllerCompat
    private val mediaControllerCallback = MediaControllerCallback()
    private var playWhenReady = false

    override val playerInfoObservable: Observable<PlayerInfo> = BehaviorSubject.create<PlayerInfo>()
    override val mediaInfoObservable: Observable<MediaInfo> = BehaviorSubject.create<MediaInfo>()

    companion object {
        private var playlist: List<MediaMetadataCompat> = emptyList()
        fun playerResources(): MutableList<MediaBrowserCompat.MediaItem> {
            val result = playlist.map {
                MediaBrowserCompat.MediaItem(
                        it.description, MediaBrowserCompat.MediaItem.FLAG_PLAYABLE)
            }
            return result.toMutableList()
        }

        fun getMediaMetadataById(id: String?): MediaMetadataCompat? {
            playlist.onEach {
                if (it.description.mediaId.equals(id)) {
                    return it
                }
            }
            return null
        }
    }

    override fun tearDown() {
        if (mediaBrowser != null && mediaBrowser!!.isConnected) {
            mediaController.transportControls.stop()
            mediaController.unregisterCallback(mediaControllerCallback)
            mediaBrowser!!.unsubscribe(mediaBrowser!!.root)
            mediaBrowser!!.disconnect()
        }
    }

    override fun play(): Completable {
        mediaController.transportControls.play()
        return Completable.complete()
    }

    override fun pause(): Completable {
        mediaController.transportControls.pause()
        return Completable.complete()
    }

    override fun skipForward(): Completable {
        mediaController.transportControls.skipToNext()
        return Completable.complete()
    }

    override fun skipBackwards(): Completable {
        mediaController.transportControls.skipToPrevious()
        return Completable.complete()
    }

    override fun seekTo(pos: Long): Completable {
        mediaController.transportControls.seekTo(pos)
        return Completable.complete()
    }

    override fun setPlaylistAndPlay(newPlaylist: List<Track>): Completable {
        playlist = newPlaylist.toMediaMetadata(imageCache)
        playWhenReady = true
        if (mediaBrowser == null || !mediaBrowser!!.isConnected) {
            connectMediaBrowser()
        } else {
            mediaController.transportControls.stop()
            mediaBrowserSubscriptionCallback.onChildrenLoaded(mediaBrowser!!.root, playerResources())
        }
        return Completable.complete()
    }

    private fun connectMediaBrowser() {
        mediaBrowser = MediaBrowserCompat(context,
                ComponentName(context, MusicPlayerService::class.java)
                , mediaBrowserConnectionCallback,
                null)
        mediaBrowser?.connect()

    }

    private fun updateNotification() {
        val bundle = Bundle()
        bundle.putParcelable(MusicPlayerService.argNotificationConfig, notificationConfig)
        mediaBrowser?.sendCustomAction(MusicPlayerService.notificationAction, bundle, null)
    }

    inner class MediaBrowserConnectionCallback : MediaBrowserCompat.ConnectionCallback() {

        override fun onConnected() {
            super.onConnected()
            mediaController = MediaControllerCompat(context, mediaBrowser!!.sessionToken)
            mediaController.registerCallback(mediaControllerCallback)
            mediaControllerCallback.onMetadataChanged(mediaController.metadata)
            mediaControllerCallback.onPlaybackStateChanged(mediaController.playbackState)
            mediaBrowser!!.subscribe(mediaBrowser!!.root, mediaBrowserSubscriptionCallback)

            mediaBrowserSubscriptionCallback.onChildrenLoaded(mediaBrowser!!.root, playerResources())
            updateNotification()
        }

    }

    inner class MediaBrowserSubscriptionCallback : MediaBrowserCompat.SubscriptionCallback() {

        override fun onChildrenLoaded(parentId: String, children: MutableList<MediaBrowserCompat.MediaItem>) {
            super.onChildrenLoaded(parentId, children)

            for (mediaItem in children) {
                mediaController.addQueueItem(mediaItem.description)
            }

            mediaController.transportControls.prepare()

            if (playWhenReady) {
                mediaController.transportControls.play()
                playWhenReady = false
            }
        }
    }

    inner class MediaControllerCallback : MediaControllerCompat.Callback() {

        override fun onPlaybackStateChanged(state: PlaybackStateCompat?) {
            super.onPlaybackStateChanged(state)
            val isPlaying = state != null && state.state == PlaybackStateCompat.STATE_PLAYING
            val progress = state?.position?.toInt() ?: 0
            playerInfoObservable as BehaviorSubject<PlayerInfo>
            playerInfoObservable.onNext(PlayerInfo(isPlaying, progress))
        }

        override fun onMetadataChanged(metadata: MediaMetadataCompat?) {
            super.onMetadataChanged(metadata)
            val title = metadata?.getString(MediaMetadataCompat.METADATA_KEY_TITLE)
            val artist = metadata?.getString(MediaMetadataCompat.METADATA_KEY_ARTIST)
            val coverUrl = metadata?.getString(MediaMetadataCompat.METADATA_KEY_ALBUM_ART_URI)
            val durationInMs = metadata?.getLong(MediaMetadataCompat.METADATA_KEY_DURATION) ?: 0
            mediaInfoObservable as BehaviorSubject<MediaInfo>
            mediaInfoObservable.onNext(MediaInfo(title, artist, coverUrl, durationInMs.toInt()))
        }
    }
}