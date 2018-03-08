package com.jesperqvarfordt.listn.common.view.playerseekbar

import com.jesperqvarfordt.listn.common.base.BasePresenter
import com.jesperqvarfordt.listn.common.base.BaseView

interface PlayerSeekBarContract {

    interface View : BaseView {
        fun animateProgress(isPlaying: Boolean, currentPos: Int, maxPos: Int)
        fun seekTo(pos: Int)
        fun cancelAnimation()
    }

    interface Presenter : BasePresenter<View> {
        fun startScrubbing()
        fun stopScrubbing(pos: Int)
        fun progressAnimationUpdated(pos: Int)
    }

}