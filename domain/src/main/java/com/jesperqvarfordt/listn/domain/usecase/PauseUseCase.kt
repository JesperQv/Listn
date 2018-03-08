package com.jesperqvarfordt.listn.domain.usecase

import com.jesperqvarfordt.listn.domain.player.MusicPlayer
import io.reactivex.Completable
import io.reactivex.Scheduler

open class PauseUseCase(private val player: MusicPlayer,
                   private val executionScheduler: Scheduler,
                   private val uiScheduler: Scheduler) {

    open fun execute(): Completable = player.pause()
            .subscribeOn(executionScheduler)
            .observeOn(uiScheduler)
}