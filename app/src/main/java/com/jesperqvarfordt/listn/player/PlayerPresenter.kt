package com.jesperqvarfordt.listn.player

import com.jesperqvarfordt.listn.domain.usecase.*
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class PlayerPresenter
@Inject
constructor(private val play: PlayUseCase,
            private val pause: PauseUseCase,
            private val skipForward: SkipForwardUseCase,
            private val skipBackwards: SkipBackwardsUseCase,
            private val subscribeToPlayer: SubscribeToCombinedInfoUseCase) : PlayerContract.Presenter {

    private var view: PlayerContract.View? = null
    private val disposables = CompositeDisposable()
    private var playing = false

    override fun subscribe(view: PlayerContract.View) {
        this.view = view
        disposables.add(subscribeToPlayer.execute()
                .subscribe({ data ->
                    playing = data.isPlaying
                    view.updateUi(data)
                }))
    }

    override fun unsubscribe() {
        view = null
        disposables.clear()
    }

    override fun playPauseClicked() {
        if (playing) {
            pause.execute()
        } else {
            play.execute()
        }
    }

    override fun skipForwardClicked() {
        skipForward.execute()
    }

    override fun skipBackwardsClicked() {
        skipBackwards.execute()
    }
}