package com.jesperqvarfordt.listn.common.view.miniplayer

import com.jesperqvarfordt.listn.common.base.BasePresenter
import com.jesperqvarfordt.listn.common.base.BaseView
import com.jesperqvarfordt.listn.domain.model.player.CombinedInfo

interface MiniPlayerContract {

    interface View: BaseView {
        fun updateUi(info: CombinedInfo)
        fun openMusicPlayer()
    }

    interface Presenter: BasePresenter<View> {
        fun playPausedClicked()
        fun miniPlayerClicked()
    }

}