package com.jesperqvarfordt.listn.domain.usecase

import com.jesperqvarfordt.listn.domain.model.player.CombinedInfo
import com.jesperqvarfordt.listn.domain.model.player.MediaInfo
import com.jesperqvarfordt.listn.domain.model.player.PlayerInfo
import com.jesperqvarfordt.listn.domain.player.MusicPlayer
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations

class SubscribeToCombinedInfoUseCaseTest {

    private lateinit var useCase: SubscribeToCombinedInfoUseCase

    @Mock
    private lateinit var musicPlayer: MusicPlayer

    private val testPlayerInfo = PlayerInfo(true, 1)
    private val testMediaInfo = MediaInfo(1, "title", "artist", "url", 10)


    @Before
    @Throws(Exception::class)
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        useCase = SubscribeToCombinedInfoUseCase(musicPlayer, Schedulers.trampoline(), Schedulers.trampoline())
    }

    @Test
    @Throws(Exception::class)
    fun `execute completes`() {
        Mockito.`when`(musicPlayer.playerInfoObservable).thenReturn(testPlayerInfoObservable())
        Mockito.`when`(musicPlayer.mediaInfoObservable).thenReturn(testMediaInfoObservable())

        useCase.execute().test().assertValue(CombinedInfo(testPlayerInfo, testMediaInfo)).assertNoErrors()

        verify(musicPlayer, times(1)).playerInfoObservable
        verify(musicPlayer, times(1)).mediaInfoObservable
        verifyNoMoreInteractions(musicPlayer)
    }

    @Test
    @Throws(Exception::class)
    fun `execute fails playerInfoObservable fails`() {
        val throwable = Throwable("test fail")
        Mockito.`when`(musicPlayer.playerInfoObservable).thenReturn(Observable.error(throwable))
        Mockito.`when`(musicPlayer.mediaInfoObservable).thenReturn(testMediaInfoObservable())

        useCase.execute().test().assertError(throwable)

        verify(musicPlayer, times(1)).playerInfoObservable
        verify(musicPlayer, times(1)).mediaInfoObservable
        verifyNoMoreInteractions(musicPlayer)
    }

    @Test
    @Throws(Exception::class)
    fun `execute fails mediaInfoObservable fails`() {
        val throwable = Throwable("test fail")
        Mockito.`when`(musicPlayer.playerInfoObservable).thenReturn(testPlayerInfoObservable())
        Mockito.`when`(musicPlayer.mediaInfoObservable).thenReturn(Observable.error(throwable))

        useCase.execute().test().assertError(throwable)

        verify(musicPlayer, times(1)).playerInfoObservable
        verify(musicPlayer, times(1)).mediaInfoObservable
        verifyNoMoreInteractions(musicPlayer)
    }

    @Test
    @Throws(Exception::class)
    fun `execute fails both observable fails`() {
        val throwable = Throwable("test fail")
        Mockito.`when`(musicPlayer.playerInfoObservable).thenReturn(Observable.error(throwable))
        Mockito.`when`(musicPlayer.mediaInfoObservable).thenReturn(Observable.error(throwable))

        useCase.execute().test().assertError(throwable)

        verify(musicPlayer, times(1)).playerInfoObservable
        verify(musicPlayer, times(1)).mediaInfoObservable
        verifyNoMoreInteractions(musicPlayer)
    }

    private fun testPlayerInfoObservable(): Observable<PlayerInfo> {
        return Observable.fromIterable(mutableListOf(testPlayerInfo))
    }

    private fun testMediaInfoObservable(): Observable<MediaInfo> {
        return Observable.fromIterable(mutableListOf(testMediaInfo))
    }
}