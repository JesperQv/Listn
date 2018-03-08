package com.jesperqvarfordt.listn.domain.usecase

import com.jesperqvarfordt.listn.domain.model.player.MediaInfo
import com.jesperqvarfordt.listn.domain.player.MusicPlayer
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

class SubscribeToMediaInfoUseCaseTest {

    private lateinit var useCase: SubscribeToMediaInfoUseCase

    @Mock
    private lateinit var musicPlayer: MusicPlayer

    private val testMediaInfo = MediaInfo("title", "artist", "url", 10)


    @Before
    @Throws(Exception::class)
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        useCase = SubscribeToMediaInfoUseCase(musicPlayer, Schedulers.trampoline(), Schedulers.trampoline())
    }

    @Test
    @Throws(Exception::class)
    fun `execute completes`() {
        Mockito.`when`(musicPlayer.mediaInfoObservable).thenReturn(testMediaInfoObservable())

        useCase.execute().test().assertValue(testMediaInfo).assertNoErrors()

        Mockito.verify(musicPlayer, Mockito.times(1)).mediaInfoObservable
        Mockito.verifyNoMoreInteractions(musicPlayer)
    }

    @Test
    @Throws(Exception::class)
    fun `execute throws error`() {
        val throwable = Throwable("test fail")
        Mockito.`when`(musicPlayer.mediaInfoObservable).thenReturn(Observable.error(throwable))

        useCase.execute().test().assertError(throwable)

        Mockito.verify(musicPlayer, Mockito.times(1)).mediaInfoObservable
        Mockito.verifyNoMoreInteractions(musicPlayer)
    }

    private fun testMediaInfoObservable(): Observable<MediaInfo> {
        return Observable.fromIterable(mutableListOf(testMediaInfo))
    }

}