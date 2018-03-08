package com.jesperqvarfordt.listn.dagger.component

import com.jesperqvarfordt.listn.device.imagecache.ImageCache
import com.jesperqvarfordt.listn.dagger.module.*
import com.jesperqvarfordt.listn.domain.player.MusicPlayer
import com.jesperqvarfordt.listn.domain.usecase.*
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class,
    MusicPlayerModule::class,
    RepositoryModule::class,
    UseCaseModule::class,
    SCModule::class])
interface AppComponent {

    fun musicPlayer(): MusicPlayer
    fun imageCache(): ImageCache
    fun searchTracksUseCase(): SearchTracksUseCase
    fun playUseCase(): PlayUseCase
    fun pauseUseCase(): PauseUseCase
    fun skipForwardUseCase(): SkipForwardUseCase
    fun skipBackwardsUseCase(): SkipBackwardsUseCase
    fun seekToUseCase(): SeekToUseCase
    fun setPlaylistAndPlayUseCase(): SetPlaylistAndPlayUseCase
    fun subscribeToPlayerInfoUseCase(): SubscribeToPlayerInfoUseCase
    fun subscribeToMediaInfoUseCase(): SubscribeToMediaInfoUseCase
    fun subscribeToCombinedInfoUseCase(): SubscribeToCombinedInfoUseCase
    fun tearDownPlayerUseCase(): TearDownPlayerUseCase
    fun getTracksOnChartUseCase(): GetTracksOnChartUseCase
    fun getChartsUseCase(): GetChartsUseCase

}