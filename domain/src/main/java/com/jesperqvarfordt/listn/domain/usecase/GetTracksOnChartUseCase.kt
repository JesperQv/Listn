package com.jesperqvarfordt.listn.domain.usecase

import com.jesperqvarfordt.listn.domain.model.Chart
import com.jesperqvarfordt.listn.domain.model.Track
import com.jesperqvarfordt.listn.domain.repository.TrackRepository
import io.reactivex.Observable
import io.reactivex.Scheduler

open class GetTracksOnChartUseCase(private val trackRepository: TrackRepository,
                                   private val executionScheduler: Scheduler,
                                   private val uiScheduler: Scheduler) {

    open fun execute(chart: Chart): Observable<List<Track>> =
            trackRepository.getTracksOnChart(chart.topTracksUrl)
                    .subscribeOn(executionScheduler)
                    .observeOn(uiScheduler)
}