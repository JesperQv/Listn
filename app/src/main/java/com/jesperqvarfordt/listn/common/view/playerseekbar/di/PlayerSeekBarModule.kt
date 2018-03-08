package com.jesperqvarfordt.listn.common.view.playerseekbar.di

import com.jesperqvarfordt.listn.common.view.playerseekbar.PlayerSeekBarContract
import com.jesperqvarfordt.listn.common.view.playerseekbar.PlayerSeekBarPresenter
import com.jesperqvarfordt.listn.dagger.ViewScope
import com.jesperqvarfordt.listn.domain.usecase.SeekToUseCase
import com.jesperqvarfordt.listn.domain.usecase.SubscribeToCombinedInfoUseCase
import dagger.Module
import dagger.Provides

@Module
class PlayerSeekBarModule {

    @Provides
    @ViewScope
    fun presenter(seekTo: SeekToUseCase,
                  subscribeToPlayer: SubscribeToCombinedInfoUseCase) : PlayerSeekBarContract.Presenter {
        return PlayerSeekBarPresenter(seekTo, subscribeToPlayer)
    }

}