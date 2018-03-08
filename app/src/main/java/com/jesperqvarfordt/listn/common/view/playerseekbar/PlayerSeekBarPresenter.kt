package com.jesperqvarfordt.listn.common.view.playerseekbar

import com.jesperqvarfordt.listn.domain.usecase.SeekToUseCase
import com.jesperqvarfordt.listn.domain.usecase.SubscribeToCombinedInfoUseCase
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class PlayerSeekBarPresenter
@Inject
constructor(private val seekTo: SeekToUseCase,
            private val subscribeToPlayer: SubscribeToCombinedInfoUseCase) : PlayerSeekBarContract.Presenter {

    private var view: PlayerSeekBarContract.View? = null
    private val disposables = CompositeDisposable()
    private var scrubbing = false

    override fun subscribe(view: PlayerSeekBarContract.View) {
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
        view = null
        disposables.clear()
    }

    override fun startScrubbing() {
        scrubbing = true
    }

    override fun stopScrubbing(pos: Int) {
        scrubbing = false
        seekTo.execute(pos.toLong())
    }

    override fun progressAnimationUpdated(pos: Int) {
        if (scrubbing) {
            view?.cancelAnimation()
            return
        }
        view?.seekTo(pos)
    }
}