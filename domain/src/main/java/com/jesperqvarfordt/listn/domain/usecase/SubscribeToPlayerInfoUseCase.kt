package com.jesperqvarfordt.listn.domain.usecase

import com.jesperqvarfordt.listn.domain.model.player.PlayerInfo
import com.jesperqvarfordt.listn.domain.player.MusicPlayer
import com.jesperqvarfordt.listn.domain.usecase.base.UseCase
import io.reactivex.Observable
import io.reactivex.Scheduler

open class SubscribeToPlayerInfoUseCase(private val player: MusicPlayer,
                                   executionScheduler: Scheduler,
                                   uiScheduler: Scheduler) : UseCase(executionScheduler, uiScheduler) {

    open fun execute(): Observable<PlayerInfo> = player.playerInfoObservable
            .subscribeOn(executionScheduler)
            .observeOn(uiScheduler)
}