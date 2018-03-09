package com.jesperqvarfordt.listn.common.view.playerprogressbar

import com.jesperqvarfordt.listn.domain.model.player.CombinedInfo
import com.jesperqvarfordt.listn.domain.model.player.MediaInfo
import com.jesperqvarfordt.listn.domain.model.player.PlayerInfo
import com.jesperqvarfordt.listn.domain.usecase.SubscribeToCombinedInfoUseCase
import io.reactivex.Observable
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

class PlayerProgressBarPresenterTest {

    private lateinit var presenter: PlayerProgressBarPresenter

    @Mock
    private lateinit var useCase: SubscribeToCombinedInfoUseCase

    @Mock
    private lateinit var view: PlayerProgressBarContract.View

    private val testPlayerInfo = PlayerInfo(true, 1)
    private val testMediaInfo = MediaInfo("title", "artist", "url", 10)
    private val testCombinedInfo = CombinedInfo(testPlayerInfo, testMediaInfo)

    @Before
    @Throws(Exception::class)
    fun setUp() {
        MockitoAnnotations.initMocks(this)

        presenter = PlayerProgressBarPresenter(useCase)
    }

    @Test
    @Throws(Exception::class)
    fun `subscribe works with data`() {
        Mockito.`when`(useCase.execute()).thenReturn(getTestData())

        presenter.subscribe(view)

        Mockito.verify(useCase, Mockito.times(1)).execute()
        Mockito.verify(view, Mockito.times(1)).animateProgress(testCombinedInfo.isPlaying,
                testCombinedInfo.elapsedTimeInMs,
                testCombinedInfo.durationInMs)
        Mockito.verifyNoMoreInteractions(useCase)
        Mockito.verifyNoMoreInteractions(view)
    }

    @Test
    @Throws(Exception::class)
    fun `subscribe works when throwing exception`() {
        val throwable = Throwable("test fail")
        Mockito.`when`(useCase.execute()).thenReturn(Observable.error(throwable))

        presenter.subscribe(view)

        Mockito.verify(useCase, Mockito.times(1)).execute()
        Mockito.verify(view, Mockito.times(0)).animateProgress(Mockito.anyBoolean(), Mockito.anyInt(), Mockito.anyInt())
        Mockito.verifyNoMoreInteractions(useCase)
        Mockito.verifyNoMoreInteractions(view)
    }

    @Test
    @Throws(Exception::class)
    fun `updates view when view animation is done`() {
        Mockito.`when`(useCase.execute()).thenReturn(getTestData())

        presenter.subscribe(view)

        presenter.progressAnimationUpdated(1)

        Mockito.verify(view, Mockito.times(1)).seekTo(1)
    }


    private fun getTestData(): Observable<CombinedInfo> {
        return Observable.fromIterable(mutableListOf(testCombinedInfo))
    }

}