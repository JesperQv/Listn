package com.jesperqvarfordt.listn.domain.usecase

import com.jesperqvarfordt.listn.domain.player.MusicPlayer
import com.jesperqvarfordt.listn.domain.usecase.base.UseCase
import io.reactivex.Completable
import io.reactivex.Scheduler

class SkipForwardUseCase(private val player: MusicPlayer,
                         executionScheduler: Scheduler,
                         uiScheduler: Scheduler) : UseCase(executionScheduler, uiScheduler) {

    fun execute(): Completable = player.skipForward()
            .subscribeOn(executionScheduler)
            .observeOn(uiScheduler)
}