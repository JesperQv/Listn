package com.jesperqvarfordt.listn.common.view.repeattogglebutton.di

import com.jesperqvarfordt.listn.common.view.repeattogglebutton.RepeatToggleButtonContract
import com.jesperqvarfordt.listn.common.view.repeattogglebutton.RepeatToggleButtonPresenter
import com.jesperqvarfordt.listn.dagger.ViewScope
import com.jesperqvarfordt.listn.domain.usecase.RepeatAllUseCase
import com.jesperqvarfordt.listn.domain.usecase.RepeatNoneUseCase
import com.jesperqvarfordt.listn.domain.usecase.RepeatOneUseCase
import com.jesperqvarfordt.listn.domain.usecase.SubscribeToPlayerInfoUseCase
import dagger.Module
import dagger.Provides

@Module
class RepeatToggleButtonModule {

    @Provides
    @ViewScope
    fun providePresenter(subscribeToPlayerInfo: SubscribeToPlayerInfoUseCase,
                         repeatNone: RepeatNoneUseCase,
                         repeatAll: RepeatAllUseCase,
                         repeatOne: RepeatOneUseCase) : RepeatToggleButtonContract.Presenter {
        return RepeatToggleButtonPresenter(subscribeToPlayerInfo, repeatNone, repeatAll, repeatOne)
    }
}