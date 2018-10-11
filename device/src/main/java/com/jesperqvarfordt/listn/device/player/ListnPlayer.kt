package com.jesperqvarfordt.listn.device.player

import android.content.Context
import android.os.Handler
import android.os.Looper
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.ext.cast.CastPlayer
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.TrackGroupArray
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.trackselection.TrackSelectionArray

class ListnPlayer(context: Context,
                  private val stateChanged: (playWhenReady: Boolean, currentPos: Long, playbackState: Int) -> Unit) :
        ExtendedPlayer, CastPlayer.SessionAvailabilityListener {

    private val exoPlayer = ExoPlayerFactory.newSimpleInstance(context, DefaultTrackSelector())
    //private val castPlayer = CastPlayer(CastContext.getSharedInstance())

    private lateinit var currentPlayer: Player

    private val handler = Handler()

    init {
        //TODO make sure we add audio attributes for focus changes
        setCurrentPlayer(exoPlayer)
    }

    private val tickRunnable = Runnable {
        run {
            stateChanged.invoke(currentPlayer.playWhenReady, currentPlayer.currentPosition, currentPlayer.playbackState)
            postTick()
        }
    }

    private fun postTick() {
        handler.removeCallbacks(tickRunnable)
        handler.postDelayed(tickRunnable, 1000)
    }

    override fun prepare(mediaSource: MediaSource) {
        //TODO support castPlayer aswell
        exoPlayer.prepare(mediaSource)
    }

    private fun setCurrentPlayer(newPlayer: Player) {
        currentPlayer = newPlayer
        currentPlayer.addListener(object : Player.EventListener {
            override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
                stateChanged.invoke(playWhenReady, currentPlayer.currentPosition, playbackState)
            }
        })
    }

    override fun onCastSessionAvailable() {
        //setCurrentPlayer(castPlayer)
    }

    override fun onCastSessionUnavailable() {
        setCurrentPlayer(exoPlayer)
    }

    override fun getContentDuration(): Long {
        return currentPlayer.contentDuration
    }

    override fun isPlayingAd(): Boolean {
        return currentPlayer.isPlayingAd
    }

    override fun getDuration(): Long {
        return currentPlayer.duration
    }

    override fun getTotalBufferedDuration(): Long {
        return currentPlayer.totalBufferedDuration
    }

    override fun addListener(listener: Player.EventListener?) {
        //TODO this probably have to be tweaked
        currentPlayer.addListener(listener)
    }

    override fun getCurrentPeriodIndex(): Int {
        return currentPlayer.currentPeriodIndex
    }

    override fun isCurrentWindowSeekable(): Boolean {
        return currentPlayer.isCurrentWindowSeekable
    }

    override fun getPlaybackError(): ExoPlaybackException? {
        return currentPlayer.playbackError
    }

    override fun getCurrentPosition(): Long {
        return currentPlayer.currentPosition
    }

    override fun removeListener(listener: Player.EventListener?) {
        //TODO same with this
        return currentPlayer.removeListener(listener)
    }

    override fun getContentPosition(): Long {
        return currentPlayer.contentPosition
    }

    override fun seekToDefaultPosition() {
        currentPlayer.seekToDefaultPosition()
    }

    override fun seekToDefaultPosition(windowIndex: Int) {
        currentPlayer.seekToDefaultPosition(windowIndex)
    }

    override fun getCurrentTrackGroups(): TrackGroupArray {
        return currentPlayer.currentTrackGroups
    }

    override fun getPlaybackParameters(): PlaybackParameters {
        return currentPlayer.playbackParameters
    }

    override fun getContentBufferedPosition(): Long {
        return currentPlayer.contentBufferedPosition
    }

    override fun setPlayWhenReady(playWhenReady: Boolean) {
        currentPlayer.playWhenReady = playWhenReady
        when (playWhenReady) {
            true -> postTick()
            false -> handler.removeCallbacks(tickRunnable)
        }
    }

    override fun getPlayWhenReady(): Boolean {
        return currentPlayer.playWhenReady
    }

    override fun getRepeatMode(): Int {
        return currentPlayer.repeatMode
    }

    override fun getCurrentTag(): Any? {
        return currentPlayer.currentTag
    }

    override fun setPlaybackParameters(playbackParameters: PlaybackParameters?) {
        currentPlayer.playbackParameters = playbackParameters
    }

    override fun isCurrentWindowDynamic(): Boolean {
        return currentPlayer.isCurrentWindowDynamic
    }

    override fun stop() {
        currentPlayer.stop()
        handler.removeCallbacks(tickRunnable)
    }

    override fun stop(reset: Boolean) {
        currentPlayer.stop(reset)
        handler.removeCallbacks(tickRunnable)
    }

    override fun getCurrentManifest(): Any? {
        return currentPlayer.currentManifest
    }

    override fun getBufferedPosition(): Long {
        return currentPlayer.bufferedPosition
    }

    override fun getCurrentTrackSelections(): TrackSelectionArray {
        return currentPlayer.currentTrackSelections
    }

    override fun getCurrentAdGroupIndex(): Int {
        return currentPlayer.currentAdGroupIndex
    }

    override fun getRendererCount(): Int {
        return currentPlayer.rendererCount
    }

    override fun getCurrentAdIndexInAdGroup(): Int {
        return currentPlayer.currentAdIndexInAdGroup
    }

    override fun getCurrentTimeline(): Timeline {
        return currentPlayer.currentTimeline
    }

    override fun seekTo(positionMs: Long) {
        currentPlayer.seekTo(positionMs)
    }

    override fun seekTo(windowIndex: Int, positionMs: Long) {
        currentPlayer.seekTo(windowIndex, positionMs)
    }

    override fun isLoading(): Boolean {
        return currentPlayer.isLoading
    }

    override fun setShuffleModeEnabled(shuffleModeEnabled: Boolean) {
        currentPlayer.shuffleModeEnabled = shuffleModeEnabled
    }

    override fun setRepeatMode(repeatMode: Int) {
        currentPlayer.repeatMode = repeatMode
    }

    override fun getVideoComponent(): Player.VideoComponent? {
        return currentPlayer.videoComponent
    }

    override fun getPlaybackState(): Int {
        return currentPlayer.playbackState
    }

    override fun getAudioComponent(): Player.AudioComponent? {
        return currentPlayer.audioComponent
    }

    override fun getNextWindowIndex(): Int {
        return currentPlayer.nextWindowIndex
    }

    override fun getApplicationLooper(): Looper {
        return currentPlayer.applicationLooper
    }

    override fun getCurrentWindowIndex(): Int {
        return currentPlayer.currentWindowIndex
    }

    override fun getRendererType(index: Int): Int {
        return currentPlayer.getRendererType(index)
    }

    override fun getTextComponent(): Player.TextComponent? {
        return currentPlayer.textComponent
    }

    override fun getShuffleModeEnabled(): Boolean {
        return currentPlayer.shuffleModeEnabled
    }

    override fun getBufferedPercentage(): Int {
        return currentPlayer.bufferedPercentage
    }

    override fun release() {
        currentPlayer.release()
        handler.removeCallbacks(tickRunnable)
    }

    override fun getPreviousWindowIndex(): Int {
        return currentPlayer.previousWindowIndex
    }

}