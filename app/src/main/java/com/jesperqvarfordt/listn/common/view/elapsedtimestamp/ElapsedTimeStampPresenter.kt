package com.jesperqvarfordt.listn.common.view.elapsedtimestamp

import com.jesperqvarfordt.listn.common.extensions.msToTimeStamp
import com.jesperqvarfordt.listn.domain.usecase.SubscribeToPlayerInfoUseCase
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class ElapsedTimeStampPresenter
@Inject
constructor(private val subscribeToPlayer: SubscribeToPlayerInfoUseCase) :
        ElapsedTimeStampContract.Presenter {

    private var view: ElapsedTimeStampContract.View? = null
    private val disposables = CompositeDisposable()

    override fun subscribe(view: ElapsedTimeStampContract.View) {
        this.view = view
        disposables.add(subscribeToPlayer.execute()
                .subscribe({
                    info ->
                    val elapsedTime = info.elapsedTimeInMs
                    view.updateText(elapsedTime.toLong().msToTimeStamp())
                }))
    }

    override fun unsubscribe() {
        view = null
        disposables.clear()
    }
}