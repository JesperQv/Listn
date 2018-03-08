package com.jesperqvarfordt.listn.home

import com.jesperqvarfordt.listn.domain.usecase.TearDownPlayerUseCase
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations

class HomePresenterTest {

    private lateinit var presenter: HomePresenter

    @Mock
    private lateinit var tearDown: TearDownPlayerUseCase

    @Mock
    private lateinit var view: HomeContract.View

    @Before
    @Throws(Exception::class)
    fun setUp() {
        MockitoAnnotations.initMocks(this)

        presenter = HomePresenter(tearDown)
        presenter.subscribe(view)
    }

    @Test
    @Throws(Exception::class)
    fun `presenter tears down player as intended on view destroyed`() {
        doNothing().`when`(tearDown).execute()
        presenter.viewDestroyed()

        verify(tearDown, times(1)).execute()
        verifyNoMoreInteractions(tearDown)
    }

}