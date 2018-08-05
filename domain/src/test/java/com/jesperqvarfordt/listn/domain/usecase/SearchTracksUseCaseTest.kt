package com.jesperqvarfordt.listn.domain.usecase

import com.jesperqvarfordt.listn.domain.model.Track
import com.jesperqvarfordt.listn.domain.repository.TrackRepository
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

class SearchTracksUseCaseTest {

    private lateinit var useCase: SearchTracksUseCase

    private val testQuery = "testQuery"
    private val testResult = mutableListOf<Track>()

    @Mock
    private lateinit var repository: TrackRepository

    @Before
    @Throws(Exception::class)
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        testResult.add(Track(1, "title", "artist", "url", "url", "streamUrl", 1))
        testResult.add(Track(2, "title2", "artist2", "url2", "url2", "streamUrl2", 2))
        useCase = SearchTracksUseCase(repository, Schedulers.trampoline(), Schedulers.trampoline())
    }

    @Test
    @Throws(Exception::class)
    fun `execute completes`() {
        Mockito.`when`(repository.search(testQuery)).thenReturn(Observable.just(testResult))
        useCase.execute(testQuery).test().assertResult(testResult).assertNoErrors()
        Mockito.verify(repository, Mockito.times(1)).search(testQuery)
        Mockito.verifyNoMoreInteractions(repository)
    }

    @Test
    @Throws(Exception::class)
    fun `execute throws error`() {
        val throwable = Throwable("test fail")
        Mockito.`when`(repository.search(testQuery)).thenReturn(Observable.error(throwable))
        useCase.execute(testQuery).test().assertError(throwable)
        Mockito.verify(repository, Mockito.times(1)).search(testQuery)
        Mockito.verifyNoMoreInteractions(repository)
    }

}