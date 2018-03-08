package com.jesperqvarfordt.listn

import android.app.Application
import com.crashlytics.android.Crashlytics
import com.jesperqvarfordt.listn.dagger.component.AppComponent
import com.jesperqvarfordt.listn.dagger.component.DaggerAppComponent
import com.jesperqvarfordt.listn.dagger.module.AppModule
import com.jesperqvarfordt.listn.dagger.module.MusicPlayerModule
import com.jesperqvarfordt.listn.dagger.module.RepositoryModule
import com.jesperqvarfordt.listn.dagger.module.UseCaseModule
import io.fabric.sdk.android.Fabric



class App : Application() {

    companion object {
        lateinit var instance: App
    }

    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        instance = this

        val fabric = Fabric.Builder(this)
                .kits(Crashlytics())
                .debuggable(true)
                .build()
        Fabric.with(fabric)

        appComponent = DaggerAppComponent.builder()
                .appModule(AppModule(this))
                .musicPlayerModule(MusicPlayerModule())
                .repositoryModule(RepositoryModule())
                .useCaseModule(UseCaseModule())
                .build()
    }



}