package com.jesperqvarfordt.listn.common.view.shuffletogglebutton.di

import com.jesperqvarfordt.listn.common.view.shuffletogglebutton.ShuffleToggleButton
import com.jesperqvarfordt.listn.dagger.ViewScope
import com.jesperqvarfordt.listn.dagger.component.AppComponent
import dagger.Component

@ViewScope
@Component(dependencies = [AppComponent::class], modules = [ShuffleToggleButtonModule::class])
interface ShuffleToggleButtonComponent {
    fun inject(view: ShuffleToggleButton)
}