package com.jesperqvarfordt.listn.domain.usecase

import com.jesperqvarfordt.listn.domain.model.Chart
import com.jesperqvarfordt.listn.domain.repository.ChartRepository
import com.jesperqvarfordt.listn.domain.usecase.base.UseCase
import io.reactivex.Observable
import io.reactivex.Scheduler

open class GetChartsUseCase(private val chartRepository: ChartRepository,
                            executionScheduler: Scheduler,
                            uiScheduler: Scheduler) : UseCase(executionScheduler, uiScheduler) {

    open fun execute(): Observable<List<Chart>> = chartRepository.getCharts()
            .subscribeOn(executionScheduler)
            .observeOn(uiScheduler)
}