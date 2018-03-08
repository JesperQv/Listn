package com.jesperqvarfordt.listn.common.view.remainingtimestamp

import com.jesperqvarfordt.listn.common.extensions.msToTimeStamp
import com.jesperqvarfordt.listn.domain.usecase.SubscribeToCombinedInfoUseCase
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class RemainingTimeStampPresenter
@Inject
constructor(private val subscribeToPlayer: SubscribeToCombinedInfoUseCase) :
        RemainingTimeStampContract.Presenter {

    private var view: RemainingTimeStampContract.View? = null
    private val disposables = CompositeDisposable()

    override fun subscribe(view: RemainingTimeStampContract.View) {
        this.view = view
        disposables.add(subscribeToPlayer.execute()
                .subscribe({ info ->
                    val remainingTime = info.durationInMs - info.elapsedTimeInMs
                    view.updateText(remainingTime.toLong().msToTimeStamp())
                }))
    }

    override fun unsubscribe() {
        this.view = null
        disposables.clear()
    }


}