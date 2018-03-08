package com.jesperqvarfordt.listn.domain.usecase

import com.jesperqvarfordt.listn.domain.player.MusicPlayer
import com.jesperqvarfordt.listn.domain.usecase.base.UseCase
import io.reactivex.Completable
import io.reactivex.Scheduler

class SkipBackwardsUseCase(private val player: MusicPlayer,
                           executionScheduler: Scheduler,
                           uiScheduler: Scheduler) : UseCase(executionScheduler, uiScheduler) {

    fun execute(): Completable = player.skipBackwards()
            .subscribeOn(executionScheduler)
            .observeOn(uiScheduler)
}