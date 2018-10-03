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
import com.jesperqvarfordt.listn.domain.model.player.RepeatMode
import com.jesperqvarfordt.listn.domain.model.player.ShuffleMode
import com.jesperqvarfordt.listn.domain.player.MusicPlayer
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
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
    private var startPlayingId = 0

    override val playerInfoObservable: Observable<PlayerInfo> = BehaviorSubject.create<PlayerInfo>()
    override val mediaInfoObservable: Observable<MediaInfo> = BehaviorSubject.create<MediaInfo>()

    private val disposables = CompositeDisposable()

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
            mediaBrowser?.unsubscribe(mediaBrowser!!.root)
            mediaBrowser?.disconnect()
        }
        disposables.clear()
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
        if (mediaController.repeatMode == PlaybackStateCompat.REPEAT_MODE_ONE) {
            mediaController.transportControls.setRepeatMode(PlaybackStateCompat.REPEAT_MODE_ALL)
        }
        mediaController.transportControls.skipToNext()
        return Completable.complete()
    }

    override fun skipBackwards(): Completable {
        if (mediaController.repeatMode == PlaybackStateCompat.REPEAT_MODE_ONE) {
            mediaController.transportControls.setRepeatMode(PlaybackStateCompat.REPEAT_MODE_ALL)
        }
        mediaController.transportControls.skipToPrevious()
        return Completable.complete()
    }

    override fun seekTo(pos: Long): Completable {
        mediaController.transportControls.seekTo(pos)
        return Completable.complete()
    }

    override fun repeat(repeatMode: RepeatMode): Completable {
        mediaController.transportControls.setRepeatMode(when (repeatMode) {
            RepeatMode.REPEAT_ALL -> PlaybackStateCompat.REPEAT_MODE_ALL
            RepeatMode.REPEAT_ONE -> PlaybackStateCompat.REPEAT_MODE_ONE
            RepeatMode.REPEAT_NONE -> PlaybackStateCompat.REPEAT_MODE_NONE
        })
        return Completable.complete()
    }

    override fun shuffle(shuffleMode: ShuffleMode): Completable {
        mediaController.transportControls.setShuffleMode(when (shuffleMode) {
            ShuffleMode.SHUFFLE_ALL -> PlaybackStateCompat.SHUFFLE_MODE_ALL
            ShuffleMode.SHUFFLE_NONE -> PlaybackStateCompat.SHUFFLE_MODE_NONE
        })
        return Completable.complete()
    }

    override fun setPlaylistAndPlay(newPlaylist: List<Track>, startPlayingId: Int): Completable {
        this.startPlayingId = startPlayingId
        playlist = newPlaylist.toMediaMetadata(imageCache)
        playWhenReady = true
        if (mediaBrowser == null || !mediaBrowser!!.isConnected) {
            connectMediaBrowser()
        } else {
            mediaController.transportControls.stop()
            mediaBrowserSubscriptionCallback.onChildrenLoaded(mediaBrowser!!.root, playerResources())
        }
        loadAlbumArtAsync(newPlaylist)

        return Completable.complete()
    }

    private fun loadAlbumArtAsync(newPlaylist: List<Track>) {
        disposables.add(imageCache.loadAllImagesAndThenComplete(newPlaylist).subscribe {
            playlist = newPlaylist.toMediaMetadata(imageCache)
            if (mediaBrowser != null && mediaBrowser!!.isConnected) {
                mediaController.transportControls.prepare()
            }
        })
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
            mediaController.transportControls.skipToQueueItem(startPlayingId.toLong())

            if (mediaController.shuffleMode == PlaybackStateCompat.SHUFFLE_MODE_ALL) {
                mediaController.transportControls.setShuffleMode(PlaybackStateCompat.SHUFFLE_MODE_ALL)
            }

            mediaController.transportControls.prepare()

            if (playWhenReady) {
                mediaController.transportControls.play()
                playWhenReady = false
            }
        }
    }

    inner class MediaControllerCallback : MediaControllerCompat.Callback() {

        private var isPlaying = false

        override fun onPlaybackStateChanged(state: PlaybackStateCompat?) {
            super.onPlaybackStateChanged(state)
            isPlaying = state != null && state.state == PlaybackStateCompat.STATE_PLAYING
            val progress = state?.position?.toInt() ?: 0
            val shuffle = getShuffleMode(mediaController.shuffleMode)
            val repeat = getRepeatMode(mediaController.repeatMode)
            playerInfoObservable as BehaviorSubject<PlayerInfo>
            playerInfoObservable.onNext(PlayerInfo(isPlaying, progress, shuffle, repeat))
        }

        override fun onMetadataChanged(metadata: MediaMetadataCompat?) {
            super.onMetadataChanged(metadata)
            val id = metadata?.getString(MediaMetadataCompat.METADATA_KEY_MEDIA_ID)?.toInt() ?: 0
            val title = metadata?.getString(MediaMetadataCompat.METADATA_KEY_TITLE)
            val artist = metadata?.getString(MediaMetadataCompat.METADATA_KEY_ARTIST)
            val coverUrl = metadata?.getString(MediaMetadataCompat.METADATA_KEY_ALBUM_ART_URI)
            val durationInMs = metadata?.getLong(MediaMetadataCompat.METADATA_KEY_DURATION) ?: 0
            mediaInfoObservable as BehaviorSubject<MediaInfo>
            mediaInfoObservable.onNext(MediaInfo(id, title, artist, coverUrl, durationInMs.toInt()))
        }

        override fun onShuffleModeChanged(shuffleMode: Int) {
            super.onShuffleModeChanged(shuffleMode)
            val progress = mediaController.playbackState?.position?.toInt() ?: 0
            val shuffle = getShuffleMode(shuffleMode)
            val repeat = getRepeatMode(mediaController.repeatMode)
            playerInfoObservable as BehaviorSubject<PlayerInfo>
            playerInfoObservable.onNext(PlayerInfo(isPlaying, progress, shuffle, repeat))
        }

        override fun onRepeatModeChanged(repeatMode: Int) {
            super.onRepeatModeChanged(repeatMode)
            val progress = mediaController.playbackState?.position?.toInt() ?: 0
            val shuffle = getShuffleMode(mediaController.shuffleMode)
            val repeat = getRepeatMode(repeatMode)
            playerInfoObservable as BehaviorSubject<PlayerInfo>
            playerInfoObservable.onNext(PlayerInfo(isPlaying, progress, shuffle, repeat))
        }

        private fun getShuffleMode(shuffleMode: Int): ShuffleMode {
            return when (shuffleMode) {
                PlaybackStateCompat.SHUFFLE_MODE_ALL -> ShuffleMode.SHUFFLE_ALL
                else -> ShuffleMode.SHUFFLE_NONE
            }
        }

        private fun getRepeatMode(repeatMode: Int): RepeatMode {
            return when (repeatMode) {
                PlaybackStateCompat.REPEAT_MODE_ONE -> RepeatMode.REPEAT_ONE
                PlaybackStateCompat.REPEAT_MODE_NONE -> RepeatMode.REPEAT_NONE
                else -> RepeatMode.REPEAT_ALL
            }
        }
    }
}