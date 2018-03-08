package com.jesperqvarfordt.listn.domain.usecase

import com.jesperqvarfordt.listn.domain.player.MusicPlayer
import io.reactivex.Completable
import io.reactivex.schedulers.Schedulers
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

class PlayUseCaseTest {

    private lateinit var useCase: PlayUseCase

    @Mock
    private lateinit var musicPlayer: MusicPlayer

    @Before
    @Throws(Exception::class)
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        useCase = PlayUseCase(musicPlayer, Schedulers.trampoline(), Schedulers.trampoline())
    }

    @Test
    @Throws(Exception::class)
    fun `execute completes`() {
        Mockito.`when`(musicPlayer.play()).thenReturn(Completable.complete())
        useCase.execute().test().assertComplete().assertNoErrors()
        Mockito.verify(musicPlayer, Mockito.times(1)).play()
        Mockito.verifyNoMoreInteractions(musicPlayer)
    }

    @Test
    @Throws(Exception::class)
    fun `execute throws error`() {
        val throwable = Throwable("test fail")
        Mockito.`when`(musicPlayer.play()).thenReturn(Completable.error(throwable))
        useCase.execute().test().assertError(throwable)
        Mockito.verify(musicPlayer, Mockito.times(1)).play()
        Mockito.verifyNoMoreInteractions(musicPlayer)
    }
}