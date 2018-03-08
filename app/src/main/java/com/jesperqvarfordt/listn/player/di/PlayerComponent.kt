package com.jesperqvarfordt.listn.player.di

import com.jesperqvarfordt.listn.dagger.component.AppComponent
import com.jesperqvarfordt.listn.dagger.ViewScope
import com.jesperqvarfordt.listn.player.PlayerActivity
import dagger.Component

@ViewScope
@Component(dependencies = [(AppComponent::class)], modules = [(PlayerModule::class)])
interface PlayerComponent {
    fun inject(activity: PlayerActivity)
}