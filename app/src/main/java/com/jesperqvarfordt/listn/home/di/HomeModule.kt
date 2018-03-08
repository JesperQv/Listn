package com.jesperqvarfordt.listn.home.di

import com.jesperqvarfordt.listn.dagger.ViewScope
import com.jesperqvarfordt.listn.domain.usecase.TearDownPlayerUseCase
import com.jesperqvarfordt.listn.home.HomeContract
import com.jesperqvarfordt.listn.home.HomePresenter
import dagger.Module
import dagger.Provides

@Module
class HomeModule {

    @Provides
    @ViewScope
    fun providePresenter(tearDownPlayer: TearDownPlayerUseCase): HomeContract.Presenter {
        return HomePresenter(tearDownPlayer)
    }
}