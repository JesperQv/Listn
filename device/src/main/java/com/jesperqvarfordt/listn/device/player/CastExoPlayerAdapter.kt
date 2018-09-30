package com.jesperqvarfordt.listn.device.player

import android.content.Context
import android.net.Uri
import android.os.Handler
import android.support.v4.media.MediaMetadataCompat
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.ext.cast.CastPlayer
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.source.TrackGroupArray
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.trackselection.TrackSelectionArray
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.MimeTypes
import com.google.android.exoplayer2.util.Util
import com.google.android.gms.cast.MediaInfo
import com.google.android.gms.cast.MediaMetadata
import com.google.android.gms.cast.MediaMetadata.MEDIA_TYPE_MUSIC_TRACK
import com.google.android.gms.cast.MediaQueueItem
import com.google.android.gms.cast.framework.CastContext
import com.google.android.gms.common.images.WebImage


class CastExoPlayerAdapter(val context: Context,
                           private val listener: ExoPlayerStateChangeListener) : PlayerAdapter(context), CastPlayer.SessionAvailabilityListener {

    interface ExoPlayerStateChangeListener {
        fun onStateChange(playWhenReady: Boolean, playbackState: Int)
    }

    override val isPlaying: Boolean
        get() = currentPlayer.playWhenReady

    val currentPos: Long
        get() = currentPlayer.currentPosition

    private val exoPlayer: SimpleExoPlayer = ExoPlayerFactory.newSimpleInstance(context, DefaultTrackSelector())
    private val castPlayer: CastPlayer
    private lateinit var currentPlayer: Player

    private val castContext: CastContext = CastContext.getSharedInstance(context)

    private var lastKnownState: Int? = null
    private val dataSourceFactory: DefaultDataSourceFactory
    private val extractorsFactory = DefaultExtractorsFactory()
    private val defaultBandwidthMeter = DefaultBandwidthMeter()

    private val tickRunnable = Runnable {
        run {
            if (lastKnownState != null) {
                listener.onStateChange(currentPlayer.playWhenReady, lastKnownState!!)
            }
            postTick()
        }
    }

    private val handler = Handler()

    init {
        //TODO fix name here
        dataSourceFactory = DefaultDataSourceFactory(context,
                Util.getUserAgent(context, "Listn"), defaultBandwidthMeter)
        exoPlayer.addListener(CastExoPlayerListener())

        castPlayer = CastPlayer(castContext)
        castPlayer.addListener(CastExoPlayerListener())
        castPlayer.setSessionAvailabilityListener(this)
        setCurrentPlayer(if (castPlayer.isCastSessionAvailable) castPlayer else exoPlayer)
    }

    override fun prepare(metadata: MediaMetadataCompat?) {
        if (currentPlayer == exoPlayer) {
            val uri = Uri.parse(metadata?.getString(MediaMetadataCompat.METADATA_KEY_MEDIA_URI))
            val mediaSource = ExtractorMediaSource.Factory(dataSourceFactory)
                    .setExtractorsFactory(extractorsFactory)
                    .setCustomCacheKey("CastExoPlayerAdapter")
                    .createMediaSource(uri)
            exoPlayer.prepare(mediaSource)
        } else if (currentPlayer == castPlayer) {
            exoPlayer.playWhenReady = false
            val audioMetadata = MediaMetadata(MEDIA_TYPE_MUSIC_TRACK)
            audioMetadata.putString(MediaMetadata.KEY_TITLE, metadata?.getString(MediaMetadataCompat.METADATA_KEY_TITLE))
            audioMetadata.putString(MediaMetadata.KEY_ALBUM_ARTIST, metadata?.getString(MediaMetadataCompat.METADATA_KEY_ARTIST))
            audioMetadata.addImage(WebImage(Uri.parse(MediaMetadataCompat.METADATA_KEY_ALBUM_ART_URI)))
            val m = MediaInfo.Builder(metadata?.getString(MediaMetadataCompat.METADATA_KEY_MEDIA_URI))
                    .setStreamType(MediaInfo.STREAM_TYPE_BUFFERED).setContentType(MimeTypes.AUDIO_UNKNOWN)
                    .setMetadata(audioMetadata).build()
            val mediaInfo = MediaInfo.Builder(MediaMetadataCompat.METADATA_KEY_MEDIA_URI)
                    .setStreamType(MediaInfo.STREAM_TYPE_BUFFERED)
                    .setContentType("audio/mp3")
                    .setMetadata(audioMetadata).build()
            //TODO for testing purposes, i will need the entire playlist here
            val x = MediaQueueItem.Builder(m).build()
            castPlayer.loadItems(arrayOf(x), 0,  currentPos, Player.REPEAT_MODE_OFF)
            castPlayer.addItems(x)
        }
    }

    private fun buildMediaQueueItem(sample: DemoUtil.Sample): MediaQueueItem {
        val movieMetadata = MediaMetadata(MediaMetadata.MEDIA_TYPE_MUSIC_TRACK)
        movieMetadata.putString(MediaMetadata.KEY_TITLE, sample.name)
        val mediaInfo = MediaInfo.Builder(sample.uri)
                .setStreamType(MediaInfo.STREAM_TYPE_BUFFERED).setContentType(sample.mimeType)
                .setMetadata(movieMetadata).build()
        return MediaQueueItem.Builder(mediaInfo).build()
    }

    override fun onCastSessionAvailable() {
        setCurrentPlayer(castPlayer)
    }

    override fun onCastSessionUnavailable() {
        setCurrentPlayer(exoPlayer)
    }

    private fun setCurrentPlayer(newPlayer: Player) {
        currentPlayer = newPlayer
    }

    private fun postTick() {
        handler.removeCallbacks(tickRunnable)
        handler.postDelayed(tickRunnable, 1000)
    }

    override fun onPlay() {
        currentPlayer.playWhenReady = true
        postTick()
    }

    override fun onPause() {
        currentPlayer.playWhenReady = false
        handler.removeCallbacks(tickRunnable)
    }

    override fun onStop() {
        currentPlayer.playWhenReady = false
        currentPlayer.release()
        handler.removeCallbacks(tickRunnable)
    }

    override fun seekTo(position: Long) {
        currentPlayer.seekTo(position)
    }

    override fun setVolume(volume: Float) {
        exoPlayer.volume = volume
    }

    inner class CastExoPlayerListener : Player.EventListener {

        override fun onPlaybackParametersChanged(playbackParameters: PlaybackParameters?) {}
        override fun onSeekProcessed() {}
        override fun onTracksChanged(trackGroups: TrackGroupArray?, trackSelections: TrackSelectionArray?) {}
        override fun onPlayerError(error: ExoPlaybackException?) {}
        override fun onLoadingChanged(isLoading: Boolean) {}
        override fun onPositionDiscontinuity(reason: Int) {}
        override fun onRepeatModeChanged(repeatMode: Int) {
            // Not needed for service
        }

        override fun onShuffleModeEnabledChanged(shuffleModeEnabled: Boolean) {
            // Not needed for service
        }

        override fun onTimelineChanged(timeline: Timeline?, manifest: Any?, reason: Int) {}

        override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
            lastKnownState = playbackState
            listener.onStateChange(playWhenReady, playbackState)
        }

    }
}