package com.jesperqvarfordt.listn.domain.usecase

import com.jesperqvarfordt.listn.domain.model.player.CombinedInfo
import com.jesperqvarfordt.listn.domain.model.player.MediaInfo
import com.jesperqvarfordt.listn.domain.model.player.PlayerInfo
import com.jesperqvarfordt.listn.domain.player.MusicPlayer
import com.jesperqvarfordt.listn.domain.usecase.base.UseCase
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.functions.BiFunction

open class SubscribeToCombinedInfoUseCase(private val player: MusicPlayer,
                                          executionScheduler: Scheduler,
                                          uiScheduler: Scheduler) : UseCase(executionScheduler, uiScheduler) {

    open fun execute(): Observable<CombinedInfo> = Observable.combineLatest(player.mediaInfoObservable,
            player.playerInfoObservable,
            BiFunction<MediaInfo, PlayerInfo, CombinedInfo> { mediaInfo, playerInfo ->
                CombinedInfo(playerInfo, mediaInfo)
            })
            .subscribeOn(executionScheduler)
            .observeOn(uiScheduler)
}