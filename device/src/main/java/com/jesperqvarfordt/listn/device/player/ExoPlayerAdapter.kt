package com.jesperqvarfordt.listn.device.player

import android.content.Context
import android.net.Uri
import android.os.Handler
import android.support.v4.media.MediaMetadataCompat
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.source.TrackGroupArray
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.trackselection.TrackSelectionArray
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util

class ExoPlayerAdapter(val context: Context,
                       private val listener: ExoPlayerStateChangeListener) : PlayerAdapter(context) {

    interface ExoPlayerStateChangeListener {
        fun onStateChange(playWhenReady: Boolean, playbackState: Int)
    }

    override val isPlaying: Boolean
        get() = exoPlayer.playWhenReady

    val currentPos: Long
        get() = exoPlayer.currentPosition

    private val exoPlayer: SimpleExoPlayer = ExoPlayerFactory.newSimpleInstance(context, DefaultTrackSelector())
    private var lastKnownState: Int? = null
    private val dataSourceFactory: DefaultDataSourceFactory
    private val extractorsFactory = DefaultExtractorsFactory()
    private val defaultBandwidthMeter = DefaultBandwidthMeter()

    private val tickRunnable = Runnable {
        run {
            if (lastKnownState != null) {
                listener.onStateChange(exoPlayer.playWhenReady, lastKnownState!!)
            }
            postTick()
        }
    }

    private val handler = Handler()

    init {
        //TODO fix name here
        dataSourceFactory = DefaultDataSourceFactory(context,
                Util.getUserAgent(context, "StreamingMusicPlayer"), defaultBandwidthMeter)
        exoPlayer.addListener(ExoPlayerListener())
    }

    override fun prepare(metadata: MediaMetadataCompat?) {
        val uri = Uri.parse(metadata?.getString(MediaMetadataCompat.METADATA_KEY_MEDIA_URI))
        val mediaSource = ExtractorMediaSource.Factory(dataSourceFactory)
                .setExtractorsFactory(extractorsFactory)
                .setCustomCacheKey("ExoPlayerAdapter")
                .createMediaSource(uri)
        exoPlayer.prepare(mediaSource)
    }

    private fun postTick() {
        handler.removeCallbacks(tickRunnable)
        handler.postDelayed(tickRunnable, 1000)
    }

    override fun onPlay() {
        exoPlayer.playWhenReady = true
        postTick()
    }

    override fun onPause() {
        exoPlayer.playWhenReady = false
        handler.removeCallbacks(tickRunnable)
    }

    override fun onStop() {
        exoPlayer.playWhenReady = false
        exoPlayer.release()
        handler.removeCallbacks(tickRunnable)
    }

    override fun seekTo(position: Long) {
        exoPlayer.seekTo(position)
    }

    override fun setVolume(volume: Float) {
        exoPlayer.volume = volume
    }

    inner class ExoPlayerListener : Player.EventListener {
        override fun onPlaybackParametersChanged(playbackParameters: PlaybackParameters?) {}
        override fun onSeekProcessed() {}
        override fun onTracksChanged(trackGroups: TrackGroupArray?, trackSelections: TrackSelectionArray?) {}
        override fun onPlayerError(error: ExoPlaybackException?) {}
        override fun onLoadingChanged(isLoading: Boolean) {}
        override fun onPositionDiscontinuity(reason: Int) {}
        override fun onRepeatModeChanged(repeatMode: Int) {}
        override fun onShuffleModeEnabledChanged(shuffleModeEnabled: Boolean) {}
        override fun onTimelineChanged(timeline: Timeline?, manifest: Any?) {}

        override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
            lastKnownState = playbackState
            listener.onStateChange(playWhenReady, playbackState)
        }

    }
}