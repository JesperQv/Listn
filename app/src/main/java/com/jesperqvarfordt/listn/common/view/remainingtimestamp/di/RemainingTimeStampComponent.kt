package com.jesperqvarfordt.listn.common.view.remainingtimestamp.di

import com.jesperqvarfordt.listn.common.view.remainingtimestamp.RemainingTimeStamp
import com.jesperqvarfordt.listn.dagger.ViewScope
import com.jesperqvarfordt.listn.dagger.component.AppComponent
import dagger.Component

@ViewScope
@Component(dependencies = [AppComponent::class], modules = [RemainingTimeStampModule::class])
interface RemainingTimeStampComponent {
    fun inject(view: RemainingTimeStamp)
}