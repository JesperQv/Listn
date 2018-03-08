package com.jesperqvarfordt.listn.common.view.remainingtimestamp

import com.jesperqvarfordt.listn.common.extensions.msToTimeStamp
import com.jesperqvarfordt.listn.domain.model.player.CombinedInfo
import com.jesperqvarfordt.listn.domain.model.player.MediaInfo
import com.jesperqvarfordt.listn.domain.model.player.PlayerInfo
import com.jesperqvarfordt.listn.domain.usecase.SubscribeToCombinedInfoUseCase
import io.reactivex.Observable
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations

class RemainingTimeStampPresenterTest {

    private lateinit var presenter: RemainingTimeStampPresenter

    @Mock
    private lateinit var useCase: SubscribeToCombinedInfoUseCase

    @Mock
    private lateinit var view: RemainingTimeStampContract.View

    private val testTime = 100

    @Before
    @Throws(Exception::class)
    fun setUp() {
        MockitoAnnotations.initMocks(this)

        presenter = RemainingTimeStampPresenter(useCase)
    }

    @Test
    @Throws(Exception::class)
    fun `subscribe works with data`() {
        `when`(useCase.execute()).thenReturn(getTestData())

        presenter.subscribe(view)

        verify(useCase, times(1)).execute()
        verify(view, times(1)).updateText(testTime.toLong().msToTimeStamp())
        verifyNoMoreInteractions(useCase)
        verifyNoMoreInteractions(view)
    }

    @Test
    @Throws(Exception::class)
    fun `subscribe works when throwing exception`() {
        val throwable = Throwable("test fail")
        `when`(useCase.execute()).thenReturn(Observable.error(throwable))

        presenter.subscribe(view)

        verify(useCase, times(1)).execute()
        verify(view, times(0)).updateText(anyString())
        verifyNoMoreInteractions(useCase)
        verifyNoMoreInteractions(view)
    }

    private fun getTestData(): Observable<CombinedInfo> {
        val playerInfo = PlayerInfo(true, testTime)
        val mediaInfo = MediaInfo(1, "a", "a", "a", testTime*2)
        return Observable.fromIterable(mutableListOf(CombinedInfo(playerInfo, mediaInfo)))
    }
}