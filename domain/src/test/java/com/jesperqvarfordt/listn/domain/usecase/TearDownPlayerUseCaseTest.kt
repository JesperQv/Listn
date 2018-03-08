package com.jesperqvarfordt.listn.domain.usecase

import com.jesperqvarfordt.listn.domain.player.MusicPlayer
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

class TearDownPlayerUseCaseTest {
    private lateinit var useCase: TearDownPlayerUseCase

    @Mock
    private lateinit var musicPlayer: MusicPlayer


    @Before
    @Throws(Exception::class)
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        useCase = TearDownPlayerUseCase(musicPlayer)
    }

    @Test
    @Throws(Exception::class)
    fun `test use case`() {
        useCase.execute()

        Mockito.verify(musicPlayer, Mockito.times(1)).tearDown()
        Mockito.verifyNoMoreInteractions(musicPlayer)
    }
}