package com.jesperqvarfordt.listn.common.view.elapsedtimestamp.di

import com.jesperqvarfordt.listn.common.view.elapsedtimestamp.ElapsedTimeStampContract
import com.jesperqvarfordt.listn.common.view.elapsedtimestamp.ElapsedTimeStampPresenter
import com.jesperqvarfordt.listn.dagger.ViewScope
import com.jesperqvarfordt.listn.domain.usecase.SubscribeToPlayerInfoUseCase
import dagger.Module
import dagger.Provides

@Module
class ElapsedTimeStampModule {

    @Provides
    @ViewScope
    fun presenter(subscribeToPlayer: SubscribeToPlayerInfoUseCase):
            ElapsedTimeStampContract.Presenter {
        return ElapsedTimeStampPresenter(subscribeToPlayer)
    }
}