package com.jesperqvarfordt.listn.common.view.shuffletogglebutton

import com.jesperqvarfordt.listn.domain.model.player.PlayerInfo
import com.jesperqvarfordt.listn.domain.usecase.ShuffleUseCase
import com.jesperqvarfordt.listn.domain.usecase.SubscribeToPlayerInfoUseCase
import io.reactivex.Completable
import io.reactivex.Observable
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

class ShuffleToggleButtonPresenterTest {

    private lateinit var presenter: ShuffleToggleButtonPresenter

    @Mock
    private lateinit var subscribeToPlayer: SubscribeToPlayerInfoUseCase

    @Mock
    private lateinit var shuffle: ShuffleUseCase

    @Mock
    private lateinit var view: ShuffleToggleButtonContract.View

    private val testTime = 100

    @Before
    @Throws(Exception::class)
    fun setUp() {
        MockitoAnnotations.initMocks(this)

        presenter = ShuffleToggleButtonPresenter(subscribeToPlayer, shuffle)
    }

    @Test
    @Throws(Exception::class)
    fun `subscribe works with data`() {
        Mockito.`when`(subscribeToPlayer.execute()).thenReturn(getTestData())

        presenter.subscribe(view)

        Mockito.verify(subscribeToPlayer, Mockito.times(1)).execute()
        Mockito.verify(view, Mockito.times(1)).toggleView(false)
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
        Mockito.verify(view, Mockito.times(0)).toggleView(false)
        Mockito.verifyNoMoreInteractions(subscribeToPlayer)
        Mockito.verifyNoMoreInteractions(view)
    }

    @Test
    @Throws(Exception::class)
    fun `shuffle click works`() {
        Mockito.`when`(subscribeToPlayer.execute()).thenReturn(getTestData())
        Mockito.`when`(shuffle.execute(true)).thenReturn(Completable.complete())

        presenter.subscribe(view)
        presenter.shuffleClicked()

        Mockito.verify(subscribeToPlayer, Mockito.times(1)).execute()
        Mockito.verify(shuffle, Mockito.times(1)).execute(true)
        Mockito.verify(view, Mockito.times(1)).toggleView(false)
        Mockito.verifyNoMoreInteractions(subscribeToPlayer)
        Mockito.verifyNoMoreInteractions(shuffle)
        Mockito.verifyNoMoreInteractions(view)
    }

    private fun getTestData(): Observable<PlayerInfo> {
        val playerInfo = PlayerInfo(true, testTime)
        return Observable.fromIterable(mutableListOf(playerInfo))
    }
}