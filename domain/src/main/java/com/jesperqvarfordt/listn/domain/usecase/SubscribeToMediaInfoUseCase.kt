package com.jesperqvarfordt.listn.domain.usecase

import com.jesperqvarfordt.listn.domain.model.player.MediaInfo
import com.jesperqvarfordt.listn.domain.player.MusicPlayer
import com.jesperqvarfordt.listn.domain.usecase.base.UseCase
import io.reactivex.Observable
import io.reactivex.Scheduler

class SubscribeToMediaInfoUseCase(private val player: MusicPlayer,
                                  executionScheduler: Scheduler,
                                  uiScheduler: Scheduler) : UseCase(executionScheduler, uiScheduler) {

    fun execute(): Observable<MediaInfo> = player.mediaInfoObservable
            .subscribeOn(executionScheduler)
            .observeOn(uiScheduler)
}