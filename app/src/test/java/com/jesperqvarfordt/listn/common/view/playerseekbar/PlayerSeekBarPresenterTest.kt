package com.jesperqvarfordt.listn.common.view.playerseekbar

import com.jesperqvarfordt.listn.domain.model.player.CombinedInfo
import com.jesperqvarfordt.listn.domain.model.player.MediaInfo
import com.jesperqvarfordt.listn.domain.model.player.PlayerInfo
import com.jesperqvarfordt.listn.domain.usecase.SeekToUseCase
import com.jesperqvarfordt.listn.domain.usecase.SubscribeToCombinedInfoUseCase
import io.reactivex.Observable
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

class PlayerSeekBarPresenterTest {

    private lateinit var presenter: PlayerSeekBarPresenter

    @Mock
    private lateinit var subscribeToPlayer: SubscribeToCombinedInfoUseCase

    @Mock
    private lateinit var seekTo: SeekToUseCase

    @Mock
    private lateinit var view: PlayerSeekBarContract.View

    private val testPlayerInfo = PlayerInfo(true, 1)
    private val testMediaInfo = MediaInfo(1, "title", "artist", "url", 10)
    private val testCombinedInfo = CombinedInfo(testPlayerInfo, testMediaInfo)

    @Before
    @Throws(Exception::class)
    fun setUp() {
        MockitoAnnotations.initMocks(this)

        presenter = PlayerSeekBarPresenter(seekTo, subscribeToPlayer)
    }

    @Test
    @Throws(Exception::class)
    fun `subscribe works with data`() {
        Mockito.`when`(subscribeToPlayer.execute()).thenReturn(getTestData())

        presenter.subscribe(view)

        Mockito.verify(subscribeToPlayer, Mockito.times(1)).execute()
        Mockito.verify(view, Mockito.times(1)).animateProgress(testCombinedInfo.isPlaying,
                testCombinedInfo.elapsedTimeInMs,
                testCombinedInfo.durationInMs)
        Mockito.verifyNoMoreInteractions(subscribeToPlayer)
        Mockito.verifyNoMoreInteractions(view)
    }

    @Test
    @Throws(Exception::class)
    fun `subscribe works when throwing exception`() {
        val throwable = Throwable("test fail")
        Mockito.`when`(subscribeToPlayer.execute()).thenReturn(Observable.error(throwable))

        presenter.subscribe(view)

        Mockito.verify(subscribeToPlayer, Mockito.times(1)).execute()
        Mockito.verify(view, Mockito.times(0)).animateProgress(Mockito.anyBoolean(), Mockito.anyInt(), Mockito.anyInt())
        Mockito.verifyNoMoreInteractions(subscribeToPlayer)
        Mockito.verifyNoMoreInteractions(view)
    }

    @Test
    @Throws(Exception::class)
    fun `updates view when view animation is done`() {
        Mockito.`when`(subscribeToPlayer.execute()).thenReturn(getTestData())

        presenter.subscribe(view)

        presenter.progressAnimationUpdated(1)

        Mockito.verify(view, Mockito.times(1)).seekTo(1)
    }

    @Test
    @Throws(Exception::class)
    fun `stop scrubbing works`() {
        Mockito.`when`(subscribeToPlayer.execute()).thenReturn(getTestData())

        presenter.subscribe(view)

        presenter.stopScrubbing(1)

        Mockito.verify(seekTo, Mockito.times(1)).execute(1)
        Mockito.verifyNoMoreInteractions(seekTo)
    }

    private fun getTestData(): Observable<CombinedInfo> {
        return Observable.fromIterable(mutableListOf(testCombinedInfo))
    }
}