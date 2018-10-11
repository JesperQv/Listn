package com.jesperqvarfordt.listn.device.player

import android.net.Uri
import android.os.Bundle
import android.os.ResultReceiver
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.PlaybackStateCompat
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.ext.mediasession.MediaSessionConnector
import com.google.android.exoplayer2.upstream.DataSource
import com.jesperqvarfordt.listn.device.extensions.id
import com.jesperqvarfordt.listn.device.extensions.toMediaSource

class ExtendedPlaybackPreparer(private val player: ExtendedPlayer,
                               private val dataSourceFactory: DataSource.Factory) : MediaSessionConnector.PlaybackPreparer {

    override fun getSupportedPrepareActions(): Long =
            PlaybackStateCompat.ACTION_PREPARE_FROM_MEDIA_ID or
                    PlaybackStateCompat.ACTION_PLAY_FROM_MEDIA_ID

    override fun onPrepareFromMediaId(mediaId: String?, extras: Bundle?) {
        val itemToPlay: MediaMetadataCompat? = StreamingMusicPlayer.playlist.find { item ->
            item.id == mediaId
        }
        player.prepare(StreamingMusicPlayer.playlist.toMediaSource(dataSourceFactory))
        val index = StreamingMusicPlayer.playlist.indexOf(itemToPlay)
        player.seekTo(index, 0)
    }

    override fun onPrepareFromSearch(query: String?, extras: Bundle?) = Unit

    override fun onCommand(player: Player?, command: String?, extras: Bundle?, cb: ResultReceiver?) = Unit

    override fun getCommands(): Array<String> = emptyArray()

    override fun onPrepareFromUri(uri: Uri?, extras: Bundle?) = Unit

    override fun onPrepare() = Unit

}