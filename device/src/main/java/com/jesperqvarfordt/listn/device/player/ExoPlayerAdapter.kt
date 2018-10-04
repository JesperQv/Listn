package com.jesperqvarfordt.listn.device.player

import android.content.Context
import android.os.Handler
import android.support.v4.media.MediaDescriptionCompat
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.audio.AudioAttributes
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory
import com.google.android.exoplayer2.source.ConcatenatingMediaSource
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.TrackGroupArray
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.trackselection.TrackSelectionArray
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util


class ExoPlayerAdapter(val context: Context,
                       private val listener: ExoPlayerStateChangeListener) : PlayerAdapter {

    interface ExoPlayerStateChangeListener {
        fun onStateChange(playWhenReady: Boolean, playbackState: Int)
        fun onMediaChanged(newMediaIndex: Int)
    }

    override val isPlaying: Boolean
        get() = exoPlayer.playWhenReady

    override val currentPos: Long
        get() = exoPlayer.currentPosition

    private val exoPlayer: SimpleExoPlayer = ExoPlayerFactory.newSimpleInstance(context, DefaultTrackSelector())
    private var lastKnownState: Int? = null
    private val dataSourceFactory: DefaultDataSourceFactory
    private val extractorsFactory = DefaultExtractorsFactory()
    private val defaultBandwidthMeter = DefaultBandwidthMeter()
    private val concatenatingMediaSource = ConcatenatingMediaSource()

    private val tickRunnable = Runnable {
        run {
            //TODO maybe use this
            // lastKnownState?.apply { listener.onStateChange(exoPlayer.playWhenReady, this) }
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
                Util.getUserAgent(context, "Listn"), defaultBandwidthMeter)
        exoPlayer.addListener(ExoPlayerListener())
        val audioAttributes = AudioAttributes.Builder()
                .setUsage(C.USAGE_MEDIA)
                .setContentType(C.CONTENT_TYPE_MOVIE)
                .build()
        exoPlayer.setAudioAttributes(audioAttributes, true)
    }

    override fun prepare() {
        exoPlayer.prepare(concatenatingMediaSource)
    }

    override fun addItem(description: MediaDescriptionCompat?) {
        concatenatingMediaSource.addMediaSource(buildMediaSource(description))
    }

    private fun postTick() {
        handler.removeCallbacks(tickRunnable)
        handler.postDelayed(tickRunnable, 1000)
    }

    override fun play() {
        exoPlayer.playWhenReady = true
        postTick()
    }

    override fun pause() {
        exoPlayer.playWhenReady = false
        handler.removeCallbacks(tickRunnable)
    }

    override fun stop() {
        exoPlayer.playWhenReady = false
        exoPlayer.release()
        handler.removeCallbacks(tickRunnable)
    }

    override fun next() {
        exoPlayer.seekTo(exoPlayer.nextWindowIndex, 0)
    }

    override fun previous() {
        exoPlayer.seekTo(exoPlayer.previousWindowIndex, 0)
    }

    override fun skipToQueueItem(index: Int) {
        exoPlayer.seekTo(index, C.TIME_UNSET)
    }

    override fun seekTo(position: Long) {
        exoPlayer.seekTo(position)
    }

    override fun setVolume(volume: Float) {
        exoPlayer.volume = volume
    }

    private fun buildMediaSource(description: MediaDescriptionCompat?): MediaSource = ExtractorMediaSource.Factory(dataSourceFactory)
            .setExtractorsFactory(extractorsFactory)
            .setCustomCacheKey("ExoPlayerAdapter")
            .createMediaSource(description?.mediaUri)


    inner class ExoPlayerListener : Player.EventListener {
        override fun onPlaybackParametersChanged(playbackParameters: PlaybackParameters?) {}
        override fun onSeekProcessed() {}
        override fun onTimelineChanged(timeline: Timeline?, manifest: Any?, reason: Int) {}
        override fun onPlayerError(error: ExoPlaybackException?) {}
        override fun onLoadingChanged(isLoading: Boolean) {}
        override fun onPositionDiscontinuity(reason: Int) {}
        override fun onRepeatModeChanged(repeatMode: Int) {
            // Not needed for service
        }


        override fun onShuffleModeEnabledChanged(shuffleModeEnabled: Boolean) {
            // Not needed for service
        }

        override fun onTracksChanged(trackGroups: TrackGroupArray?, trackSelections: TrackSelectionArray?) {
            listener.onMediaChanged(exoPlayer.currentWindowIndex)
        }

        override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
            lastKnownState = playbackState
            listener.onStateChange(playWhenReady, playbackState)
        }
    }
}