package com.jesperqvarfordt.listn.device.player

import android.content.ComponentName
import android.content.Context
import android.os.Bundle
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaControllerCompat
import android.support.v4.media.session.PlaybackStateCompat
import com.jesperqvarfordt.listn.device.casttest.*
import com.jesperqvarfordt.listn.device.extensions.playbackStateToRepeatMode
import com.jesperqvarfordt.listn.device.extensions.playbackStateToShuffleMode
import com.jesperqvarfordt.listn.device.extensions.toPlaybackStateRepeatMode
import com.jesperqvarfordt.listn.device.extensions.toPlaybackStateShuffleMode
import com.jesperqvarfordt.listn.device.imagecache.ImageCache
import com.jesperqvarfordt.listn.device.isSameTracks
import com.jesperqvarfordt.listn.device.service.MusicPlayerService
import com.jesperqvarfordt.listn.device.toMediaMetadata
import com.jesperqvarfordt.listn.domain.model.Track
import com.jesperqvarfordt.listn.domain.model.player.MediaInfo
import com.jesperqvarfordt.listn.domain.model.player.PlayerInfo
import com.jesperqvarfordt.listn.domain.model.player.RepeatMode
import com.jesperqvarfordt.listn.domain.model.player.ShuffleMode
import com.jesperqvarfordt.listn.domain.player.MusicPlayer
import io.reactivex.Completable
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

    override val playerInfoObservable: BehaviorSubject<PlayerInfo> = BehaviorSubject.create<PlayerInfo>()
    override val mediaInfoObservable: BehaviorSubject<MediaInfo> = BehaviorSubject.create<MediaInfo>()

    private val disposables = CompositeDisposable()

    companion object {
        var playlist: List<MediaMetadataCompat> = emptyList()
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
        mediaController.transportControls.setRepeatMode(repeatMode.toPlaybackStateRepeatMode())
        return Completable.complete()
    }

    override fun shuffle(shuffleMode: ShuffleMode): Completable {
        mediaController.transportControls.setShuffleMode(shuffleMode.toPlaybackStateShuffleMode())
        return Completable.complete()
    }

    override fun setPlaylistAndPlay(newPlaylist: List<Track>, startPlayingId: Int): Completable {
        this.startPlayingId = startPlayingId
        if (playlist.isSameTracks(newPlaylist)) {
            mediaController.transportControls.skipToQueueItem(startPlayingId.toLong())
            mediaController.transportControls.play()
            return Completable.complete()
        }
        playlist = newPlaylist.toMediaMetadata(imageCache)
        playWhenReady = true
        if (mediaBrowser == null || !mediaBrowser!!.isConnected) {
            connectMediaBrowser()
        } else {
            mediaController.transportControls.pause()
            mediaBrowserSubscriptionCallback.onChildrenLoaded(mediaBrowser!!.root, playerResources())
        }
        loadAlbumArtAsync(newPlaylist)

        return Completable.complete()
    }

    private fun loadAlbumArtAsync(newPlaylist: List<Track>) {
        disposables.add(imageCache.loadAllImagesAndThenComplete(newPlaylist).subscribe {
            playlist = newPlaylist.toMediaMetadata(imageCache)
        })
    }

    private fun connectMediaBrowser() {
        mediaBrowser = MediaBrowserCompat(context,
                ComponentName(context, PlayerService::class.java)
                , mediaBrowserConnectionCallback,
                null)
        mediaBrowser?.connect()

    }

    private fun updateNotification() {
        val bundle = Bundle()
        bundle.putParcelable(MusicPlayerService.argNotificationConfig, notificationConfig)
        mediaBrowser?.sendCustomAction(MusicPlayerService.NOTIFICATION_ACTION, bundle, null)
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
            mediaController.apply {
                transportControls.playFromMediaId(playlist[0].id, null)
                if (shuffleMode == PlaybackStateCompat.SHUFFLE_MODE_ALL) {
                    transportControls.setShuffleMode(PlaybackStateCompat.SHUFFLE_MODE_ALL)
                    transportControls.prepare()
                    transportControls.skipToQueueItem(startPlayingId.toLong())
                }
                if (playWhenReady) {
                    transportControls.play()
                    playWhenReady = false
                }
            }
        }
    }

    inner class MediaControllerCallback : MediaControllerCompat.Callback() {

        private var isPlaying = false

        override fun onPlaybackStateChanged(state: PlaybackStateCompat?) {
            super.onPlaybackStateChanged(state)
            isPlaying = state != null && state.state == PlaybackStateCompat.STATE_PLAYING
            playerInfoObservable.onNext(PlayerInfo(isPlaying,
                    state?.position?.toInt() ?: 0,
                    mediaController.shuffleMode.playbackStateToShuffleMode(),
                    mediaController.repeatMode.playbackStateToRepeatMode()))
        }

        override fun onMetadataChanged(metadata: MediaMetadataCompat?) {
            super.onMetadataChanged(metadata)
            mediaInfoObservable.onNext(MediaInfo(metadata?.id?.toInt() ?: 0,
                    metadata?.title,
                    metadata?.displaySubtitle,
                    metadata?.displayIconUri.toString(),
                    metadata?.duration?.toInt() ?: 0))
        }

        override fun onShuffleModeChanged(shuffleMode: Int) {
            super.onShuffleModeChanged(shuffleMode)
            playerInfoObservable.onNext(PlayerInfo(isPlaying,
                    mediaController.playbackState?.position?.toInt() ?: 0,
                    mediaController.shuffleMode.playbackStateToShuffleMode(),
                    mediaController.repeatMode.playbackStateToRepeatMode()))
        }

        override fun onRepeatModeChanged(repeatMode: Int) {
            super.onRepeatModeChanged(repeatMode)
            playerInfoObservable.onNext(PlayerInfo(isPlaying, mediaController.playbackState?.position?.toInt() ?: 0,
                    mediaController.shuffleMode.playbackStateToShuffleMode(),
                    mediaController.repeatMode.playbackStateToRepeatMode()))
        }
    }
}