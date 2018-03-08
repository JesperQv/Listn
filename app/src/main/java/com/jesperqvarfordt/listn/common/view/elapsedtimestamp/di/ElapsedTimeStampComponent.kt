package com.jesperqvarfordt.listn.common.view.elapsedtimestamp.di

import com.jesperqvarfordt.listn.common.view.elapsedtimestamp.ElapsedTimeStamp
import com.jesperqvarfordt.listn.dagger.ViewScope
import com.jesperqvarfordt.listn.dagger.component.AppComponent
import dagger.Component

@ViewScope
@Component(dependencies = [AppComponent::class], modules = [ElapsedTimeStampModule::class])
interface ElapsedTimeStampComponent {
    fun inject(view: ElapsedTimeStamp)
}