package com.jesperqvarfordt.listn.player.di

import com.jesperqvarfordt.listn.dagger.ViewScope
import com.jesperqvarfordt.listn.domain.usecase.*
import com.jesperqvarfordt.listn.player.PlayerContract
import com.jesperqvarfordt.listn.player.PlayerPresenter
import dagger.Module
import dagger.Provides

@Module
class PlayerModule {

    @Provides
    @ViewScope
    fun presenter(play: PlayUseCase,
                  pause: PauseUseCase,
                  skipForward: SkipForwardUseCase,
                  skipBackwards: SkipBackwardsUseCase,
                  subscribeToPlayer: SubscribeToCombinedInfoUseCase): PlayerContract.Presenter {
        return PlayerPresenter(play, pause, skipForward, skipBackwards, subscribeToPlayer)
    }

}