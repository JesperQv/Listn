package com.jesperqvarfordt.listn.domain.usecase

import com.jesperqvarfordt.listn.domain.player.MusicPlayer
import com.jesperqvarfordt.listn.domain.usecase.base.UseCase
import io.reactivex.Completable
import io.reactivex.Scheduler

open class SeekToUseCase(private val player: MusicPlayer,
                    executionScheduler: Scheduler,
                    uiScheduler: Scheduler) : UseCase(executionScheduler, uiScheduler) {

    open fun execute(pos: Long): Completable = player.seekTo(pos)
            .subscribeOn(executionScheduler)
            .observeOn(uiScheduler)
}