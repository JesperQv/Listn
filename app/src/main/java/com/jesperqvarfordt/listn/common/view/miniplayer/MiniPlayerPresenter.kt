package com.jesperqvarfordt.listn.common.view.miniplayer

import com.jesperqvarfordt.listn.domain.usecase.PauseUseCase
import com.jesperqvarfordt.listn.domain.usecase.PlayUseCase
import com.jesperqvarfordt.listn.domain.usecase.SubscribeToCombinedInfoUseCase
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class MiniPlayerPresenter
@Inject
constructor(private val play: PlayUseCase,
            private val pause: PauseUseCase,
            private val subscribeToPlayer: SubscribeToCombinedInfoUseCase) : MiniPlayerContract.Presenter {

    private var view: MiniPlayerContract.View? = null
    private val disposables = CompositeDisposable()
    private var playing = false

    override fun subscribe(view: MiniPlayerContract.View) {
        this.view = view
        disposables.add(subscribeToPlayer.execute()
                .subscribe({ data ->
                    playing = data.isPlaying
                    this@MiniPlayerPresenter.view?.updateUi(data)
                }, {
                    // TODO implementation needed? maybe do nothing
                }))
    }

    override fun unsubscribe() {
        view = null
        disposables.clear()
    }

    override fun playPausedClicked() {
        if (playing) {
            pause.execute()
        } else {
            play.execute()
        }
    }

    override fun miniPlayerClicked() {
        view?.openMusicPlayer()
    }
}