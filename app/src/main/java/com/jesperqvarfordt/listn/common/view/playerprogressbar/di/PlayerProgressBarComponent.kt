package com.jesperqvarfordt.listn.common.view.playerprogressbar.di

import com.jesperqvarfordt.listn.common.view.playerprogressbar.PlayerProgressBar
import com.jesperqvarfordt.listn.dagger.component.AppComponent
import com.jesperqvarfordt.listn.dagger.ViewScope
import dagger.Component

@ViewScope
@Component(dependencies = [(AppComponent::class)], modules = [(PlayerProgressBarModule::class)])
interface PlayerProgressBarComponent {
    fun inject(playerProgressBar: PlayerProgressBar)
}