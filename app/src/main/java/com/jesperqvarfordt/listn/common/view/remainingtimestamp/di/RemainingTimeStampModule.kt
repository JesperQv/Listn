package com.jesperqvarfordt.listn.common.view.remainingtimestamp.di

import com.jesperqvarfordt.listn.common.view.remainingtimestamp.RemainingTimeStampContract
import com.jesperqvarfordt.listn.common.view.remainingtimestamp.RemainingTimeStampPresenter
import com.jesperqvarfordt.listn.dagger.ViewScope
import com.jesperqvarfordt.listn.domain.usecase.SubscribeToCombinedInfoUseCase
import dagger.Module
import dagger.Provides

@Module
class RemainingTimeStampModule {

    @Provides
    @ViewScope
    fun presenter(subscribeToPlayer: SubscribeToCombinedInfoUseCase):
            RemainingTimeStampContract.Presenter {
        return RemainingTimeStampPresenter(subscribeToPlayer)
    }
}