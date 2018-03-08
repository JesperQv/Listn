package com.jesperqvarfordt.listn.domain.usecase

import com.jesperqvarfordt.listn.domain.player.MusicPlayer
import io.reactivex.Completable
import io.reactivex.Scheduler

class SkipForwardUseCase(private val player: MusicPlayer,
                         private val executionScheduler: Scheduler,
                         private val uiScheduler: Scheduler) {

    fun execute(): Completable = player.skipForward()
            .subscribeOn(executionScheduler)
            .observeOn(uiScheduler)
}