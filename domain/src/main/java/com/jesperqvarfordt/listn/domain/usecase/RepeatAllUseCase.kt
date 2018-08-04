package com.jesperqvarfordt.listn.domain.usecase

import com.jesperqvarfordt.listn.domain.model.player.RepeatMode
import com.jesperqvarfordt.listn.domain.player.MusicPlayer
import com.jesperqvarfordt.listn.domain.usecase.base.UseCase
import io.reactivex.Completable
import io.reactivex.Scheduler

class RepeatAllUseCase(private val player: MusicPlayer,
                       executionScheduler: Scheduler,
                       uiScheduler: Scheduler) : UseCase(executionScheduler, uiScheduler) {

    open fun execute(): Completable = player.repeat(RepeatMode.REPEAT_ALL)
            .subscribeOn(executionScheduler)
            .observeOn(uiScheduler)
}