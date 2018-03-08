package com.jesperqvarfordt.listn.domain.usecase

import com.jesperqvarfordt.listn.domain.model.Chart
import com.jesperqvarfordt.listn.domain.repository.ChartRepository
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

class GetChartsUseCaseTest {

    private lateinit var useCase: GetChartsUseCase

    private var testResult = mutableListOf<Chart>()

    @Mock
    private lateinit var repository: ChartRepository

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        testResult.add(Chart("Chart 1", "url", "uri"))
        testResult.add(Chart("Chart 2", "url2", "uri2"))
        useCase = GetChartsUseCase(repository, Schedulers.trampoline(), Schedulers.trampoline())
    }

    @Test
    @Throws(Exception::class)
    fun `execute completes`() {
        Mockito.`when`(repository.getCharts()).thenReturn(Observable.just(testResult))
        useCase.execute().test().assertResult(testResult).assertNoErrors()
        Mockito.verify(repository, Mockito.times(1)).getCharts()
        Mockito.verifyNoMoreInteractions(repository)
    }

    @Test
    @Throws(Exception::class)
    fun `execute throws error`() {
        val throwable = Throwable("test fail")
        Mockito.`when`(repository.getCharts()).thenReturn(Observable.error(throwable))
        useCase.execute().test().assertError(throwable)
        Mockito.verify(repository, Mockito.times(1)).getCharts()
        Mockito.verifyNoMoreInteractions(repository)
    }

}