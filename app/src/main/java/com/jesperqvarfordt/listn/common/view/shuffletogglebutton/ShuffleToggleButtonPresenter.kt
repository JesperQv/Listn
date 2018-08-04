package com.jesperqvarfordt.listn.common.view.shuffletogglebutton

import com.jesperqvarfordt.listn.domain.model.player.ShuffleMode
import com.jesperqvarfordt.listn.domain.usecase.ShuffleUseCase
import com.jesperqvarfordt.listn.domain.usecase.SubscribeToPlayerInfoUseCase
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class ShuffleToggleButtonPresenter
@Inject
constructor(private val subscribeToPlayer: SubscribeToPlayerInfoUseCase,
            private val shuffle: ShuffleUseCase) :
        ShuffleToggleButtonContract.Presenter {

    private var view: ShuffleToggleButtonContract.View? = null
    private val disposables = CompositeDisposable()

    private var shuffling = false

    override fun shuffleClicked() {
        disposables.add(shuffle.execute(!shuffling)
                .subscribe())
    }

    override fun subscribe(view: ShuffleToggleButtonContract.View) {
        this.view = view
        disposables.add(subscribeToPlayer.execute()
                .subscribe({
                    shuffling = it.shuffleMode != ShuffleMode.SHUFFLE_NONE
                    view.toggleView(shuffling)
                }, {
                    // Nothing
                }))
    }

    override fun unsubscribe() {
        view = null
        disposables.clear()
    }
}