package com.jesperqvarfordt.listn.domain.usecase

import com.jesperqvarfordt.listn.domain.model.Track
import com.jesperqvarfordt.listn.domain.player.MusicPlayer
import io.reactivex.Completable
import io.reactivex.schedulers.Schedulers
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

class SetPlaylistAndPlayUseCaseTest {

    private lateinit var useCase: SetPlaylistAndPlayUseCase

    private val testData = mutableListOf<Track>()

    @Mock
    private lateinit var musicPlayer: MusicPlayer

    @Before
    @Throws(Exception::class)
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        testData.add(Track(1, "title", "artist", "url", "url", "streamUrl", 1))
        testData.add(Track(2, "title2", "artist2", "url2", "url2", "streamUrl2", 2))
        useCase = SetPlaylistAndPlayUseCase(musicPlayer, Schedulers.trampoline(), Schedulers.trampoline())
    }

    @Test
    @Throws(Exception::class)
    fun `execute completes`() {
        Mockito.`when`(musicPlayer.setPlaylistAndPlay(testData, 1)).thenReturn(Completable.complete())
        useCase.execute(testData, 1).test().assertComplete().assertNoErrors()
        Mockito.verify(musicPlayer, Mockito.times(1)).setPlaylistAndPlay(testData, 1)
        Mockito.verifyNoMoreInteractions(musicPlayer)
    }

    @Test
    @Throws(Exception::class)
    fun `execute throws error`() {
        val throwable = Throwable("test fail")
        Mockito.`when`(musicPlayer.setPlaylistAndPlay(testData, 1)).thenReturn(Completable.error(throwable))
        useCase.execute(testData, 1).test().assertError(throwable)
        Mockito.verify(musicPlayer, Mockito.times(1)).setPlaylistAndPlay(testData, 1)
        Mockito.verifyNoMoreInteractions(musicPlayer)
    }
}