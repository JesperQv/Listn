package com.jesperqvarfordt.listn.domain.usecase

import com.jesperqvarfordt.listn.domain.model.Track
import com.jesperqvarfordt.listn.domain.repository.TrackRepository
import com.jesperqvarfordt.listn.domain.usecase.base.UseCase
import io.reactivex.Scheduler

class SearchTracksUseCase(private val repository: TrackRepository,
                          executionScheduler: Scheduler,
                          uiScheduler: Scheduler) : UseCase(executionScheduler, uiScheduler) {

    suspend fun execute(query: String): List<Track> = repository.search(query)
}