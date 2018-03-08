package com.jesperqvarfordt.listn.dagger.module

import com.jesperqvarfordt.listn.data.api.SCApi
import com.jesperqvarfordt.listn.data.mapper.TrackMapper
import com.jesperqvarfordt.listn.data.repository.LocalChartRepository
import com.jesperqvarfordt.listn.data.repository.SCTrackRepository
import com.jesperqvarfordt.listn.domain.repository.ChartRepository
import com.jesperqvarfordt.listn.domain.repository.TrackRepository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class RepositoryModule {

    @Provides
    @Singleton
    fun trackRepository(api: SCApi,
                        mapper: TrackMapper): TrackRepository {
        return SCTrackRepository(api, mapper)
    }

    @Provides
    @Singleton
    fun chartRepository(): ChartRepository {
        return LocalChartRepository()
    }
}