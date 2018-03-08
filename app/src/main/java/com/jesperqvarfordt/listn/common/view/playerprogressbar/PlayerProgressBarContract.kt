package com.jesperqvarfordt.listn.common.view.playerprogressbar

import com.jesperqvarfordt.listn.common.base.BasePresenter
import com.jesperqvarfordt.listn.common.base.BaseView

interface PlayerProgressBarContract {

    interface View : BaseView {
        fun animateProgress(isPlaying: Boolean, currentPos: Int, maxPos: Int)
        fun seekTo(pos: Int)
    }

    interface Presenter : BasePresenter<View> {
        fun progressAnimationUpdated(pos: Int)
    }

}