package com.jesperqvarfordt.listn.player.di

import com.jesperqvarfordt.listn.dagger.ViewScope
import com.jesperqvarfordt.listn.dagger.component.AppComponent
import com.jesperqvarfordt.listn.explore.ExploreFragment
import com.jesperqvarfordt.listn.explore.di.TracksModule
import dagger.Component

@ViewScope
@Component(dependencies = [AppComponent::class], modules = [TracksModule::class])
interface TracksComponent {
    fun inject(fragment: ExploreFragment)
}