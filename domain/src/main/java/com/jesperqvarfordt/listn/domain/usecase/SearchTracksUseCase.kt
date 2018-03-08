package com.jesperqvarfordt.listn.domain.usecase

import com.jesperqvarfordt.listn.domain.model.Track
import com.jesperqvarfordt.listn.domain.repository.TrackRepository
import io.reactivex.Observable
import io.reactivex.Scheduler

class SearchTracksUseCase(private val repository: TrackRepository,
                          private val executionScheduler: Scheduler,
                          private val uiScheduler: Scheduler){
    fun execute(query: String): Observable<List<Track>> = repository.search(query)
            .subscribeOn(executionScheduler)
            .observeOn(uiScheduler)
}