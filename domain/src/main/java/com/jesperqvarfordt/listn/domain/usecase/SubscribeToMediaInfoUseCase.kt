package com.jesperqvarfordt.listn.domain.usecase

import com.jesperqvarfordt.listn.domain.model.player.MediaInfo
import com.jesperqvarfordt.listn.domain.player.MusicPlayer
import io.reactivex.Observable
import io.reactivex.Scheduler

class SubscribeToMediaInfoUseCase(private val player: MusicPlayer,
                                  private val executionScheduler: Scheduler,
                                  private val uiScheduler: Scheduler) {

    fun execute(): Observable<MediaInfo> = player.mediaInfoObservable
            .subscribeOn(executionScheduler)
            .observeOn(uiScheduler)
}