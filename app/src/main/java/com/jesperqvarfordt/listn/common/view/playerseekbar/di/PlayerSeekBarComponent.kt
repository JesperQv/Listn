package com.jesperqvarfordt.listn.common.view.playerseekbar.di

import com.jesperqvarfordt.listn.common.view.playerseekbar.PlayerSeekBar
import com.jesperqvarfordt.listn.dagger.component.AppComponent
import com.jesperqvarfordt.listn.dagger.ViewScope
import dagger.Component

@ViewScope
@Component(dependencies = [(AppComponent::class)], modules = [(PlayerSeekBarModule::class)])
interface PlayerSeekBarComponent {
    fun inject(playerSeekBar: PlayerSeekBar)
}