package com.jesperqvarfordt.listn.domain.usecase

import com.jesperqvarfordt.listn.domain.model.player.ShuffleMode
import com.jesperqvarfordt.listn.domain.player.MusicPlayer
import com.jesperqvarfordt.listn.domain.usecase.base.UseCase
import io.reactivex.Completable
import io.reactivex.Scheduler

open class ShuffleUseCase(private val player: MusicPlayer,
                     executionScheduler: Scheduler,
                     uiScheduler: Scheduler) : UseCase(executionScheduler, uiScheduler) {

    open fun execute(shuffle: Boolean): Completable = player.shuffle(
            when (shuffle) {
                true -> ShuffleMode.SHUFFLE_ALL
                false -> ShuffleMode.SHUFFLE_NONE
            })
            .subscribeOn(executionScheduler)
            .observeOn(uiScheduler)
}