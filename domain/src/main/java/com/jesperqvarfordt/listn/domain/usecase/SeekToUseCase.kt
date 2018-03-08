package com.jesperqvarfordt.listn.domain.usecase

import com.jesperqvarfordt.listn.domain.player.MusicPlayer
import io.reactivex.Completable
import io.reactivex.Scheduler

class SeekToUseCase (private val player: MusicPlayer,
                     private val executionScheduler: Scheduler,
                     private val uiScheduler: Scheduler) {

    fun execute(pos: Long): Completable = player.seekTo(pos)
            .subscribeOn(executionScheduler)
            .observeOn(uiScheduler)
}