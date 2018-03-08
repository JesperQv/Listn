package com.jesperqvarfordt.listn.domain.usecase

import com.jesperqvarfordt.listn.domain.player.MusicPlayer
import io.reactivex.Completable
import io.reactivex.Scheduler

class SkipBackwardsUseCase (private val player: MusicPlayer,
                            private val executionScheduler: Scheduler,
                            private val uiScheduler: Scheduler) {

    fun execute(): Completable = player.skipBackwards()
            .subscribeOn(executionScheduler)
            .observeOn(uiScheduler)
}