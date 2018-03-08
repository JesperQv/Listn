package com.jesperqvarfordt.listn.common.view.miniplayer

import com.jesperqvarfordt.listn.domain.model.player.CombinedInfo
import com.jesperqvarfordt.listn.domain.model.player.MediaInfo
import com.jesperqvarfordt.listn.domain.model.player.PlayerInfo
import com.jesperqvarfordt.listn.domain.usecase.PauseUseCase
import com.jesperqvarfordt.listn.domain.usecase.PlayUseCase
import com.jesperqvarfordt.listn.domain.usecase.SubscribeToCombinedInfoUseCase
import io.reactivex.Completable
import io.reactivex.Observable
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations

class MiniPlayerPresenterTest {

    private lateinit var presenter: MiniPlayerPresenter

    @Mock
    private lateinit var play: PlayUseCase

    @Mock
    private lateinit var pause: PauseUseCase

    @Mock
    private lateinit var subscribeToPlayer: SubscribeToCombinedInfoUseCase

    @Mock
    private lateinit var view: MiniPlayerContract.View

    private val testPlayerInfo = PlayerInfo(true, 1)
    private val testMediaInfo = MediaInfo(1,"title", "artist", "url", 10)
    private val testCombinedInfo = CombinedInfo(testPlayerInfo, testMediaInfo)

    @Before
    @Throws(Exception::class)
    fun setUp() {
        MockitoAnnotations.initMocks(this)

        presenter = MiniPlayerPresenter(play, pause, subscribeToPlayer)
    }

    @Test
    @Throws(Exception::class)
    fun `subscribe works with data`() {
        `when`(subscribeToPlayer.execute()).thenReturn(getTestDataPlaying())

        presenter.subscribe(view)

        verify(subscribeToPlayer, times(1)).execute()
        verify(view, times(1)).updateUi(testCombinedInfo)
        verifyNoMoreInteractions(subscribeToPlayer)
        verifyNoMoreInteractions(view)
    }

    @Test
    @Throws(Exception::class)
    fun `subscribe works when throwing exception`() {
        val throwable = Throwable("test fail")
        `when`(subscribeToPlayer.execute()).thenReturn(Observable.error(throwable))

        presenter.subscribe(view)

        verify(subscribeToPlayer, times(1)).execute()
        verify(view, times(0)).updateUi(testCombinedInfo)
        verifyNoMoreInteractions(subscribeToPlayer)
        verifyNoMoreInteractions(view)
    }

    @Test
    @Throws(Exception::class)
    fun `pause works with complete`() {
        `when`(subscribeToPlayer.execute()).thenReturn(getTestDataPlaying())
        presenter.subscribe(view)

        `when`(pause.execute()).thenReturn(Completable.complete())

        presenter.playPausedClicked()

        verify(pause, times(1)).execute()
        verifyNoMoreInteractions(pause)
    }

    @Test
    @Throws(Exception::class)
    fun `play works with complete`() {
        `when`(subscribeToPlayer.execute()).thenReturn(getTestDataPaused())
        presenter.subscribe(view)

        `when`(play.execute()).thenReturn(Completable.complete())

        presenter.playPausedClicked()

        verify(play, times(1)).execute()
        verifyNoMoreInteractions(play)
    }

    @Test
    @Throws(Exception::class)
    fun `opens player when self clicked`() {
        `when`(subscribeToPlayer.execute()).thenReturn(getTestDataPaused())
        presenter.subscribe(view)

        presenter.miniPlayerClicked()

        verify(view, times(1)).openMusicPlayer()
    }

    private fun getTestDataPlaying(): Observable<CombinedInfo> {
        return Observable.fromIterable(mutableListOf(testCombinedInfo))
    }

    private fun getTestDataPaused(): Observable<CombinedInfo> {
        val pausedInfo = PlayerInfo(false, 1)
        return Observable.fromIterable(mutableListOf(CombinedInfo(pausedInfo, testMediaInfo)))
    }
}