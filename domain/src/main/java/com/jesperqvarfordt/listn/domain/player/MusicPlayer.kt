package com.jesperqvarfordt.listn.domain.player

import com.jesperqvarfordt.listn.domain.model.Track
import com.jesperqvarfordt.listn.domain.model.player.MediaInfo
import com.jesperqvarfordt.listn.domain.model.player.PlayerInfo
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
    fun setPlaylistAndPlay(newPlaylist: List<Track>): Completable
    fun tearDown()

}