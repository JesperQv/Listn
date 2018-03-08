package com.jesperqvarfordt.listn.tracks.di

import com.jesperqvarfordt.listn.device.imagecache.ImageCache
import com.jesperqvarfordt.listn.dagger.ViewScope
import com.jesperqvarfordt.listn.domain.usecase.GetChartsUseCase
import com.jesperqvarfordt.listn.domain.usecase.GetTracksOnChartUseCase
import com.jesperqvarfordt.listn.domain.usecase.SearchTracksUseCase
import com.jesperqvarfordt.listn.domain.usecase.SetPlaylistAndPlayUseCase
import com.jesperqvarfordt.listn.tracks.ExploreContract
import com.jesperqvarfordt.listn.tracks.ExplorePresenter
import dagger.Module
import dagger.Provides

@Module
class TracksModule {

    @Provides
    @ViewScope
    fun presenter(search: SearchTracksUseCase,
                  setPlaylistAndPlay: SetPlaylistAndPlayUseCase,
                  getChartsUseCase: GetChartsUseCase,
                  getTracksOnChartUseCase: GetTracksOnChartUseCase,
                  imageCache: ImageCache): ExploreContract.Presenter {
        return ExplorePresenter(search, setPlaylistAndPlay, getChartsUseCase, getTracksOnChartUseCase, imageCache)
    }

}