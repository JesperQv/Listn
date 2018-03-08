package com.jesperqvarfordt.listn.domain.usecase.base

import io.reactivex.Scheduler

abstract class UseCase(protected val executionScheduler: Scheduler,
                       protected val uiScheduler: Scheduler)