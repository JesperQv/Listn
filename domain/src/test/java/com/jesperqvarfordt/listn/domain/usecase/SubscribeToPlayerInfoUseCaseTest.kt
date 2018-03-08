package com.jesperqvarfordt.listn.domain.usecase

import com.jesperqvarfordt.listn.domain.model.player.PlayerInfo
import com.jesperqvarfordt.listn.domain.player.MusicPlayer
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

class SubscribeToPlayerInfoUseCaseTest {
    private lateinit var useCase: SubscribeToPlayerInfoUseCase

    @Mock
    private lateinit var musicPlayer: MusicPlayer

    private val testPlayerInfo = PlayerInfo(true, 1)


    @Before
    @Throws(Exception::class)
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        useCase = SubscribeToPlayerInfoUseCase(musicPlayer, Schedulers.trampoline(), Schedulers.trampoline())
    }

    @Test
    @Throws(Exception::class)
    fun `execute completes`() {
        Mockito.`when`(musicPlayer.playerInfoObservable).thenReturn(testPlayerInfoObservable())

        useCase.execute().test().assertValue(testPlayerInfo).assertNoErrors()

        Mockito.verify(musicPlayer, Mockito.times(1)).playerInfoObservable
        Mockito.verifyNoMoreInteractions(musicPlayer)
    }

    @Test
    @Throws(Exception::class)
    fun `execute throws error`() {
        val throwable = Throwable("test fail")
        Mockito.`when`(musicPlayer.playerInfoObservable).thenReturn(Observable.error(throwable))

        useCase.execute().test().assertError(throwable)

        Mockito.verify(musicPlayer, Mockito.times(1)).playerInfoObservable
        Mockito.verifyNoMoreInteractions(musicPlayer)
    }

    private fun testPlayerInfoObservable(): Observable<PlayerInfo> {
        return Observable.fromIterable(mutableListOf(testPlayerInfo))
    }
}