package com.jesperqvarfordt.listn.common.view.elapsedtimestamp

import com.jesperqvarfordt.listn.common.extensions.msToTimeStamp
import com.jesperqvarfordt.listn.domain.model.player.PlayerInfo
import com.jesperqvarfordt.listn.domain.usecase.SubscribeToPlayerInfoUseCase
import io.reactivex.Observable
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations

class ElapsedTimeStampPresenterTest {

    private lateinit var presenter: ElapsedTimeStampPresenter

    @Mock
    private lateinit var subscribeToPlayerInfo: SubscribeToPlayerInfoUseCase

    @Mock
    private lateinit var view: ElapsedTimeStampContract.View

    private val testTime = 100

    @Before
    @Throws(Exception::class)
    fun setUp() {
        MockitoAnnotations.initMocks(this)

        presenter = ElapsedTimeStampPresenter(subscribeToPlayerInfo)
    }

    @Test
    @Throws(Exception::class)
    fun `subscribe works with data`() {
        Mockito.`when`(subscribeToPlayerInfo.execute()).thenReturn(getTestData())

        presenter.subscribe(view)

        verify(subscribeToPlayerInfo, times(1)).execute()
        verify(view, times(1)).updateText(testTime.toLong().msToTimeStamp())
        verifyNoMoreInteractions(subscribeToPlayerInfo)
        verifyNoMoreInteractions(view)
    }

    @Test
    @Throws(Exception::class)
    fun `subscribe works when throwing exception`() {
        val throwable = Throwable("test fail")
        `when`(subscribeToPlayerInfo.execute()).thenReturn(Observable.error(throwable))

        presenter.subscribe(view)

        verify(subscribeToPlayerInfo, times(1)).execute()
        verify(view, times(0)).updateText(anyString())
        verifyNoMoreInteractions(subscribeToPlayerInfo)
        verifyNoMoreInteractions(view)
    }

    private fun getTestData(): Observable<PlayerInfo> {
        val info = PlayerInfo(true, testTime)
        return Observable.fromIterable(mutableListOf(info))
    }
}