package com.jesperqvarfordt.listn.domain.usecase

import com.jesperqvarfordt.listn.domain.model.Track
import com.jesperqvarfordt.listn.domain.player.MusicPlayer
import io.reactivex.Completable
import io.reactivex.Scheduler

class SetPlaylistAndPlayUseCase (private val player: MusicPlayer,
                                 private val executionScheduler: Scheduler,
                                 private val uiScheduler: Scheduler) {

    fun execute(playlist: List<Track>): Completable = player.setPlaylistAndPlay(playlist)
            .subscribeOn(executionScheduler)
            .observeOn(uiScheduler)
}