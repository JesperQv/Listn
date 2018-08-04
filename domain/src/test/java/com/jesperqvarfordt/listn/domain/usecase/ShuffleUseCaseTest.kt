package com.jesperqvarfordt.listn.domain.usecase

import com.jesperqvarfordt.listn.domain.model.player.ShuffleMode
import com.jesperqvarfordt.listn.domain.player.MusicPlayer
import io.reactivex.Completable
import io.reactivex.schedulers.Schedulers
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

class ShuffleUseCaseTest {

    private lateinit var useCase: ShuffleUseCase

    @Mock
    private lateinit var musicPlayer: MusicPlayer

    @Before
    @Throws(Exception::class)
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        useCase = ShuffleUseCase(musicPlayer, Schedulers.trampoline(), Schedulers.trampoline())
    }

    @Test
    @Throws(Exception::class)
    fun `execute false completes`() {
        Mockito.`when`(musicPlayer.shuffle(ShuffleMode.SHUFFLE_NONE)).thenReturn(Completable.complete())
        useCase.execute(false).test().assertComplete().assertNoErrors()
        Mockito.verify(musicPlayer, Mockito.times(1)).shuffle(ShuffleMode.SHUFFLE_NONE)
        Mockito.verifyNoMoreInteractions(musicPlayer)
    }

    @Test
    @Throws(Exception::class)
    fun `execute true completes`() {
        Mockito.`when`(musicPlayer.shuffle(ShuffleMode.SHUFFLE_ALL)).thenReturn(Completable.complete())
        useCase.execute(true).test().assertComplete().assertNoErrors()
        Mockito.verify(musicPlayer, Mockito.times(1)).shuffle(ShuffleMode.SHUFFLE_ALL)
        Mockito.verifyNoMoreInteractions(musicPlayer)
    }

    @Test
    @Throws(Exception::class)
    fun `execute false throws error`() {
        val throwable = Throwable("test fail")
        Mockito.`when`(musicPlayer.shuffle(ShuffleMode.SHUFFLE_NONE)).thenReturn(Completable.error(throwable))
        useCase.execute(false).test().assertError(throwable)
        Mockito.verify(musicPlayer, Mockito.times(1)).shuffle(ShuffleMode.SHUFFLE_NONE)
        Mockito.verifyNoMoreInteractions(musicPlayer)
    }

    @Test
    @Throws(Exception::class)
    fun `execute true throws error`() {
        val throwable = Throwable("test fail")
        Mockito.`when`(musicPlayer.shuffle(ShuffleMode.SHUFFLE_ALL)).thenReturn(Completable.error(throwable))
        useCase.execute(true).test().assertError(throwable)
        Mockito.verify(musicPlayer, Mockito.times(1)).shuffle(ShuffleMode.SHUFFLE_ALL)
        Mockito.verifyNoMoreInteractions(musicPlayer)
    }
}