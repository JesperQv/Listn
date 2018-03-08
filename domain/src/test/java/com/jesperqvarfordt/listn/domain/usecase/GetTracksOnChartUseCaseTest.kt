package com.jesperqvarfordt.listn.domain.usecase

import com.jesperqvarfordt.listn.domain.model.Chart
import com.jesperqvarfordt.listn.domain.model.Track
import com.jesperqvarfordt.listn.domain.repository.TrackRepository
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

class GetTracksOnChartUseCaseTest {

    private lateinit var useCase: GetTracksOnChartUseCase

    private val testChart = Chart("test", "testUrl", "testUri")

    private var testResult = mutableListOf<Track>()

    @Mock
    private lateinit var repository: TrackRepository

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        testResult.add(Track(1, "title", "artist", "url", "url", "streamUrl", 1))
        testResult.add(Track(2, "title2", "artist2", "url2", "url2", "streamUrl2", 2))
        useCase = GetTracksOnChartUseCase(repository, Schedulers.trampoline(), Schedulers.trampoline())
    }

    @Test
    @Throws(Exception::class)
    fun `execute completes`() {
        Mockito.`when`(repository.getTracksOnChart(testChart.topTracksUrl)).thenReturn(Observable.just(testResult))
        useCase.execute(testChart).test().assertResult(testResult).assertNoErrors()
        Mockito.verify(repository, Mockito.times(1)).getTracksOnChart(testChart.topTracksUrl)
        Mockito.verifyNoMoreInteractions(repository)
    }

    @Test
    @Throws(Exception::class)
    fun `execute throws error`() {
        val throwable = Throwable("test fail")
        Mockito.`when`(repository.getTracksOnChart(testChart.topTracksUrl)).thenReturn(Observable.error(throwable))
        useCase.execute(testChart).test().assertError(throwable)
        Mockito.verify(repository, Mockito.times(1)).getTracksOnChart(testChart.topTracksUrl)
        Mockito.verifyNoMoreInteractions(repository)
    }

}