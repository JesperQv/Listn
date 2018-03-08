package com.jesperqvarfordt.listn.data.repository

import com.jesperqvarfordt.listn.data.api.SCApi
import com.jesperqvarfordt.listn.data.mapper.TrackMapper
import com.jesperqvarfordt.listn.data.model.SCTrack
import io.reactivex.Observable
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations

class SCTrackRepositoryTest {

    private lateinit var repository: SCTrackRepository

    @Mock
    private lateinit var api: SCApi

    @Mock
    private lateinit var mapper: TrackMapper

    private val testQuery = "test"

    @Before
    @Throws(Exception::class)
    fun setUp() {
        MockitoAnnotations.initMocks(this)

        repository = SCTrackRepository(api, mapper)
    }

    @Test
    @Throws(Exception::class)
    fun `search fails`() {
        val throwable = Throwable("test fail")
        `when`(api.searchTracks(anyString())).thenReturn(Observable.error(throwable))

        repository.search(testQuery).test().assertError(throwable)

        verify(api, times(1)).searchTracks(testQuery)
        verifyNoMoreInteractions(mapper)
        verifyNoMoreInteractions(api)
    }

    @Test
    @Throws(Exception::class)
    fun `search completes`() {
        val result = listOf(SCTrack(1, "test", "test", "test", 10, null))
        `when`(api.searchTracks(anyString())).thenReturn(Observable.just(result))

        val mappedResult = result.map { mapper.map(it) }.toList()
        repository.search(testQuery).test().assertValue(mappedResult).assertNoErrors()

        verify(api, times(1)).searchTracks(testQuery)
        verifyNoMoreInteractions(mapper)
        verifyNoMoreInteractions(api)
    }


}