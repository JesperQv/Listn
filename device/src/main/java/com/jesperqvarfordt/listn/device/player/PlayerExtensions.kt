package com.jesperqvarfordt.listn.device.player

import android.support.v4.media.session.MediaControllerCompat
import com.jesperqvarfordt.listn.device.service.MusicPlayerService.Companion.CLEAR_LIST_ACTION

fun MediaControllerCompat.TransportControls.clearPlaylist() {
    this.sendCustomAction(CLEAR_LIST_ACTION, null)
}