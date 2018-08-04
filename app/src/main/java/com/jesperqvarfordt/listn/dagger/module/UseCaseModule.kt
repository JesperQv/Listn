package com.jesperqvarfordt.listn.dagger.module

import com.jesperqvarfordt.listn.domain.player.MusicPlayer
import com.jesperqvarfordt.listn.domain.repository.ChartRepository
import com.jesperqvarfordt.listn.domain.repository.TrackRepository
import com.jesperqvarfordt.listn.domain.usecase.*
import dagger.Module
import dagger.Provides
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Singleton

@Module
class UseCaseModule {

    @Provides
    @Singleton
    fun searchTracksUseCase(repo: TrackRepository): SearchTracksUseCase {
        return SearchTracksUseCase(repo, Schedulers.io(), AndroidSchedulers.mainThread())
    }

    @Provides
    @Singleton
    fun playUseCase(player: MusicPlayer): PlayUseCase {
        return PlayUseCase(player, Schedulers.io(), AndroidSchedulers.mainThread())
    }

    @Provides
    @Singleton
    fun pauseUseCase(player: MusicPlayer): PauseUseCase {
        return PauseUseCase(player, Schedulers.io(), AndroidSchedulers.mainThread())
    }

    @Provides
    @Singleton
    fun skipForwardUseCase(player: MusicPlayer): SkipForwardUseCase {
        return SkipForwardUseCase(player, Schedulers.io(), AndroidSchedulers.mainThread())
    }

    @Provides
    @Singleton
    fun skipBackwardsUseCase(player: MusicPlayer): SkipBackwardsUseCase {
        return SkipBackwardsUseCase(player, Schedulers.io(), AndroidSchedulers.mainThread())
    }

    @Provides
    @Singleton
    fun seekToUseCase(player: MusicPlayer): SeekToUseCase {
        return SeekToUseCase(player, Schedulers.io(), AndroidSchedulers.mainThread())
    }

    @Provides
    @Singleton
    fun setPlaylistAndPlayUseCase(player: MusicPlayer): SetPlaylistAndPlayUseCase {
        return SetPlaylistAndPlayUseCase(player, Schedulers.io(), AndroidSchedulers.mainThread())
    }

    @Provides
    @Singleton
    fun subscribeToPlayerInfoUseCase(player: MusicPlayer): SubscribeToPlayerInfoUseCase {
        return SubscribeToPlayerInfoUseCase(player, Schedulers.io(), AndroidSchedulers.mainThread())
    }

    @Provides
    @Singleton
    fun subscribeToMediaInfoUseCase(player: MusicPlayer): SubscribeToMediaInfoUseCase {
        return SubscribeToMediaInfoUseCase(player, Schedulers.io(), AndroidSchedulers.mainThread())
    }

    @Provides
    @Singleton
    fun subscribeToCombinedInfoUseCase(player: MusicPlayer): SubscribeToCombinedInfoUseCase {
        return SubscribeToCombinedInfoUseCase(player, Schedulers.io(), AndroidSchedulers.mainThread())
    }

    @Provides
    @Singleton
    fun getShuffleUseCase(player: MusicPlayer): ShuffleUseCase {
        return ShuffleUseCase(player, Schedulers.io(), AndroidSchedulers.mainThread())
    }

    @Provides
    @Singleton
    fun getRepeatNoneUseCase(player: MusicPlayer): RepeatNoneUseCase {
        return RepeatNoneUseCase(player, Schedulers.io(), AndroidSchedulers.mainThread())
    }

    @Provides
    @Singleton
    fun getRepeatAllUseCase(player: MusicPlayer): RepeatAllUseCase {
        return RepeatAllUseCase(player, Schedulers.io(), AndroidSchedulers.mainThread())
    }

    @Provides
    @Singleton
    fun getRepeatOneUseCase(player: MusicPlayer): RepeatOneUseCase {
        return RepeatOneUseCase(player, Schedulers.io(), AndroidSchedulers.mainThread())
    }

    @Provides
    @Singleton
    fun tearDownPlayerUseCase(player: MusicPlayer): TearDownPlayerUseCase {
        return TearDownPlayerUseCase(player)
    }

    @Provides
    @Singleton
    fun getTracksOnChartUseCase(trackRepository: TrackRepository): GetTracksOnChartUseCase {
        return GetTracksOnChartUseCase(trackRepository, Schedulers.io(), AndroidSchedulers.mainThread())
    }

    @Provides
    @Singleton
    fun getChartsUseCase(chartRepository: ChartRepository): GetChartsUseCase {
        return GetChartsUseCase(chartRepository, Schedulers.io(), AndroidSchedulers.mainThread())
    }

}