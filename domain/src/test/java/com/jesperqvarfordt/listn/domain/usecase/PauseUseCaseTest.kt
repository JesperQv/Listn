package com.jesperqvarfordt.listn.domain.usecase

import com.jesperqvarfordt.listn.domain.player.MusicPlayer
import io.reactivex.Completable
import io.reactivex.schedulers.Schedulers
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations

class PauseUseCaseTest {

    private lateinit var useCase: PauseUseCase

    @Mock
    private lateinit var musicPlayer: MusicPlayer

    @Before
    @Throws(Exception::class)
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        useCase = PauseUseCase(musicPlayer, Schedulers.trampoline(), Schedulers.trampoline())
    }

    @Test
    @Throws(Exception::class)
    fun `execute completes`() {
        `when`(musicPlayer.pause()).thenReturn(Completable.complete())
        useCase.execute().test().assertComplete().assertNoErrors()
        verify(musicPlayer, times(1)).pause()
        verifyNoMoreInteractions(musicPlayer)
    }

    @Test
    @Throws(Exception::class)
    fun `execute throws error`() {
        val throwable = Throwable("test fail")
        `when`(musicPlayer.pause()).thenReturn(Completable.error(throwable))
        useCase.execute().test().assertError(throwable)
        verify(musicPlayer, times(1)).pause()
        verifyNoMoreInteractions(musicPlayer)
    }

}