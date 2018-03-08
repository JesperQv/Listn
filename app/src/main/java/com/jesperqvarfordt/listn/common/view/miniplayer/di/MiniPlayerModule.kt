package com.jesperqvarfordt.listn.common.view.miniplayer.di

import com.jesperqvarfordt.listn.common.view.miniplayer.MiniPlayerContract
import com.jesperqvarfordt.listn.common.view.miniplayer.MiniPlayerPresenter
import com.jesperqvarfordt.listn.dagger.ViewScope
import com.jesperqvarfordt.listn.domain.usecase.PauseUseCase
import com.jesperqvarfordt.listn.domain.usecase.PlayUseCase
import com.jesperqvarfordt.listn.domain.usecase.SubscribeToCombinedInfoUseCase
import dagger.Module
import dagger.Provides

@Module
class MiniPlayerModule {

    @Provides
    @ViewScope
    fun presenter(play: PlayUseCase,
                  pause: PauseUseCase,
                  subscribeToPlayer: SubscribeToCombinedInfoUseCase): MiniPlayerContract.Presenter {
        return MiniPlayerPresenter(play, pause, subscribeToPlayer)
    }
}