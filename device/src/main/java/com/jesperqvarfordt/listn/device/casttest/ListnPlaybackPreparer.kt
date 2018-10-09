package com.jesperqvarfordt.listn.device.casttest

import android.net.Uri
import android.os.Bundle
import android.os.ResultReceiver
import android.support.v4.media.session.PlaybackStateCompat
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.ext.mediasession.MediaSessionConnector
import com.google.android.exoplayer2.upstream.DataSource
import com.jesperqvarfordt.listn.device.player.StreamingMusicPlayer

class ListnPlaybackPreparer(private val player: MyPlayer,
                            private val dataSourceFactory: DataSource.Factory) : MediaSessionConnector.PlaybackPreparer {

    override fun getSupportedPrepareActions(): Long =
            PlaybackStateCompat.ACTION_PREPARE_FROM_MEDIA_ID or
                    PlaybackStateCompat.ACTION_PLAY_FROM_MEDIA_ID

    override fun onPrepareFromMediaId(mediaId: String?, extras: Bundle?) {
        StreamingMusicPlayer.playerResources()
        //TODO seek to start index
        player.prepare(StreamingMusicPlayer.playlist.toMediaSource(dataSourceFactory))
    }

    override fun onPrepareFromSearch(query: String?, extras: Bundle?) = Unit

    override fun onCommand(player: Player?, command: String?, extras: Bundle?, cb: ResultReceiver?) = Unit

    override fun getCommands(): Array<String> = emptyArray()

    override fun onPrepareFromUri(uri: Uri?, extras: Bundle?) = Unit

    override fun onPrepare() = Unit

}