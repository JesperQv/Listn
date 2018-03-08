package com.jesperqvarfordt.listn.player

import com.jesperqvarfordt.listn.domain.model.player.CombinedInfo
import com.jesperqvarfordt.listn.common.base.BasePresenter
import com.jesperqvarfordt.listn.common.base.BaseView

interface PlayerContract {

    interface View : BaseView {
        fun updateUi(data: CombinedInfo)
    }

    interface Presenter : BasePresenter<View> {
        fun playPauseClicked()
        fun skipForwardClicked()
        fun skipBackwardsClicked()
    }

}