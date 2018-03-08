package com.jesperqvarfordt.listn.common.view.playerprogressbar

import com.jesperqvarfordt.listn.domain.usecase.SubscribeToCombinedInfoUseCase
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class PlayerProgressBarPresenter
@Inject
constructor(private val subscribeToPlayer: SubscribeToCombinedInfoUseCase) : PlayerProgressBarContract.Presenter {

    private var view: PlayerProgressBarContract.View? = null
    private val disposables = CompositeDisposable()

    override fun subscribe(view: PlayerProgressBarContract.View) {
        this.view = view
        disposables.add(subscribeToPlayer.execute()
                .subscribe({ data ->
                    view.animateProgress(data.isPlaying,
                            data.elapsedTimeInMs,
                            data.durationInMs)
                }, {
                    // no error implementation needed
                }))
    }

    override fun unsubscribe() {
        disposables.clear()
        view = null

    }

    override fun progressAnimationUpdated(pos: Int) {
        view?.seekTo(pos)
    }
}