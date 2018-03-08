package com.jesperqvarfordt.listn.domain.usecase

import com.jesperqvarfordt.listn.domain.model.Chart
import com.jesperqvarfordt.listn.domain.repository.ChartRepository
import io.reactivex.Observable
import io.reactivex.Scheduler

open class GetChartsUseCase(private val chartRepository: ChartRepository,
                            private val executionScheduler: Scheduler,
                            private val uiScheduler: Scheduler) {

    open fun execute(): Observable<List<Chart>> = chartRepository.getCharts()
            .subscribeOn(executionScheduler)
            .observeOn(uiScheduler)
}