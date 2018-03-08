package com.jesperqvarfordt.listn.common.view.shuffletogglebutton.di

import com.jesperqvarfordt.listn.common.view.shuffletogglebutton.ShuffleToggleButtonContract
import com.jesperqvarfordt.listn.common.view.shuffletogglebutton.ShuffleToggleButtonPresenter
import com.jesperqvarfordt.listn.dagger.ViewScope
import com.jesperqvarfordt.listn.domain.usecase.ShuffleUseCase
import com.jesperqvarfordt.listn.domain.usecase.SubscribeToPlayerInfoUseCase
import dagger.Module
import dagger.Provides

@Module
class ShuffleToggleButtonModule {

    @Provides
    @ViewScope
    fun presenter(subscribeToPlayer: SubscribeToPlayerInfoUseCase,
                  shuffle: ShuffleUseCase):
            ShuffleToggleButtonContract.Presenter {
        return ShuffleToggleButtonPresenter(subscribeToPlayer, shuffle)
    }
}