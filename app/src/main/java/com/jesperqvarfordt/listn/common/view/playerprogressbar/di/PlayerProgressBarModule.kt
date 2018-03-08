package com.jesperqvarfordt.listn.common.view.playerprogressbar.di

import com.jesperqvarfordt.listn.common.view.playerprogressbar.PlayerProgressBarContract
import com.jesperqvarfordt.listn.common.view.playerprogressbar.PlayerProgressBarPresenter
import com.jesperqvarfordt.listn.dagger.ViewScope
import com.jesperqvarfordt.listn.domain.usecase.SubscribeToCombinedInfoUseCase
import dagger.Module
import dagger.Provides

@Module
class PlayerProgressBarModule {

    @Provides
    @ViewScope
    fun presenter(subscribeToPlayer: SubscribeToCombinedInfoUseCase) : PlayerProgressBarContract.Presenter {
        return PlayerProgressBarPresenter(subscribeToPlayer)
    }

}