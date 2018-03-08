package com.jesperqvarfordt.listn.common.view.miniplayer.di

import com.jesperqvarfordt.listn.common.view.miniplayer.MiniPlayer
import com.jesperqvarfordt.listn.dagger.component.AppComponent
import com.jesperqvarfordt.listn.dagger.ViewScope
import dagger.Component

@ViewScope
@Component(dependencies = [(AppComponent::class)], modules = [(MiniPlayerModule::class)])
interface MiniPlayerComponent {
    fun inject(view: MiniPlayer)
}