package com.jesperqvarfordt.listn.domain.player

import com.jesperqvarfordt.listn.domain.model.Track
import com.jesperqvarfordt.listn.domain.model.player.MediaInfo
import com.jesperqvarfordt.listn.domain.model.player.PlayerInfo
import com.jesperqvarfordt.listn.domain.model.player.RepeatMode
import com.jesperqvarfordt.listn.domain.model.player.ShuffleMode
import io.reactivex.Completable
import io.reactivex.Observable

interface MusicPlayer {

    val playerInfoObservable: Observable<PlayerInfo>
    val mediaInfoObservable: Observable<MediaInfo>

    fun play(): Completable
    fun pause(): Completable
    fun skipForward(): Completable
    fun skipBackwards(): Completable
    fun seekTo(pos: Long): Completable
    fun setPlaylistAndPlay(newPlaylist: List<Track>, startPlayingIndex: Int): Completable
    fun repeat(repeatMode: RepeatMode): Completable
    fun shuffle(shuffleMode: ShuffleMode): Completable
    fun tearDown()

}