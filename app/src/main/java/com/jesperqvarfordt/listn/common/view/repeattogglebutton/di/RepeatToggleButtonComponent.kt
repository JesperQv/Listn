package com.jesperqvarfordt.listn.common.view.repeattogglebutton.di

import com.jesperqvarfordt.listn.common.view.repeattogglebutton.RepeatToggleButton
import com.jesperqvarfordt.listn.dagger.ViewScope
import com.jesperqvarfordt.listn.dagger.component.AppComponent
import dagger.Component

@ViewScope
@Component(dependencies = [AppComponent::class], modules = [RepeatToggleButtonModule::class])
interface RepeatToggleButtonComponent {
    fun inject(view: RepeatToggleButton)
}