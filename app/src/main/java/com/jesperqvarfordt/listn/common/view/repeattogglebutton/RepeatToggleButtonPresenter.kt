package com.jesperqvarfordt.listn.common.view.repeattogglebutton

import com.jesperqvarfordt.listn.domain.model.player.RepeatMode
import com.jesperqvarfordt.listn.domain.usecase.RepeatAllUseCase
import com.jesperqvarfordt.listn.domain.usecase.RepeatNoneUseCase
import com.jesperqvarfordt.listn.domain.usecase.RepeatOneUseCase
import com.jesperqvarfordt.listn.domain.usecase.SubscribeToPlayerInfoUseCase
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class RepeatToggleButtonPresenter
@Inject
constructor(private val subscribeToPlayerInfo: SubscribeToPlayerInfoUseCase,
            private val repeatNone: RepeatNoneUseCase,
            private val repeatAll: RepeatAllUseCase,
            private val repeatOne: RepeatOneUseCase) :
        RepeatToggleButtonContract.Presenter {

    private val disposables = CompositeDisposable()
    private var view: RepeatToggleButtonContract.View? = null

    private var repeatMode = RepeatMode.REPEAT_NONE

    override fun repeatClicked() {
        when (repeatMode) {
            RepeatMode.REPEAT_NONE -> disposables.add(repeatAll.execute().subscribe())
            RepeatMode.REPEAT_ALL -> disposables.add(repeatOne.execute().subscribe())
            RepeatMode.REPEAT_ONE -> disposables.add(repeatNone.execute().subscribe())
        }
    }

    override fun subscribe(view: RepeatToggleButtonContract.View) {
        this.view = view
        disposables.add(subscribeToPlayerInfo.execute()
                .subscribe({
                    repeatMode = it.repeatMode
                    when (repeatMode) {
                        RepeatMode.REPEAT_NONE -> view.showRepeatNone()
                        RepeatMode.REPEAT_ALL -> view.showRepeatAll()
                        RepeatMode.REPEAT_ONE -> view.showRepeatOne()
                    }
                }, {
                    // Nothing
                }))
    }

    override fun unsubscribe() {
        view = null
        disposables.clear()
    }
}