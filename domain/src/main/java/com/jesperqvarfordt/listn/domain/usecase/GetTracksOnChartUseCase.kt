package com.jesperqvarfordt.listn.domain.usecase

import com.jesperqvarfordt.listn.domain.model.Chart
import com.jesperqvarfordt.listn.domain.model.Track
import com.jesperqvarfordt.listn.domain.repository.TrackRepository
import com.jesperqvarfordt.listn.domain.usecase.base.UseCase
import io.reactivex.Observable
import io.reactivex.Scheduler

open class GetTracksOnChartUseCase(private val trackRepository: TrackRepository,
                                   executionScheduler: Scheduler,
                                   uiScheduler: Scheduler) : UseCase(executionScheduler, uiScheduler) {

    open fun execute(chart: Chart): Observable<List<Track>> =
            trackRepository.getTracksOnChart(chart.topTracksUrl)
                    .subscribeOn(executionScheduler)
                    .observeOn(uiScheduler)
}