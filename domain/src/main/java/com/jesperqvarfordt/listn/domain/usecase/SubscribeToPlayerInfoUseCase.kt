package com.jesperqvarfordt.listn.domain.usecase

import com.jesperqvarfordt.listn.domain.model.player.PlayerInfo
import com.jesperqvarfordt.listn.domain.player.MusicPlayer
import io.reactivex.Observable
import io.reactivex.Scheduler

class SubscribeToPlayerInfoUseCase (private val player: MusicPlayer,
                                    private val executionScheduler: Scheduler,
                                    private val uiScheduler: Scheduler) {

    fun execute(): Observable<PlayerInfo> = player.playerInfoObservable
            .subscribeOn(executionScheduler)
            .observeOn(uiScheduler)
}