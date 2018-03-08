package com.jesperqvarfordt.listn.domain.usecase

import com.jesperqvarfordt.listn.domain.model.Track
import com.jesperqvarfordt.listn.domain.player.MusicPlayer
import com.jesperqvarfordt.listn.domain.usecase.base.UseCase
import io.reactivex.Completable
import io.reactivex.Scheduler

class SetPlaylistAndPlayUseCase(private val player: MusicPlayer,
                                executionScheduler: Scheduler,
                                uiScheduler: Scheduler) : UseCase(executionScheduler, uiScheduler) {

    fun execute(playlist: List<Track>): Completable = player.setPlaylistAndPlay(playlist)
            .subscribeOn(executionScheduler)
            .observeOn(uiScheduler)
}